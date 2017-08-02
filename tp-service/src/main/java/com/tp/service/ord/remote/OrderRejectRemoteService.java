package com.tp.service.ord.remote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.common.vo.ord.OrderErrorCodes.CUSTOMER_SERVICE_ERROR_CODE;
import com.tp.dao.ord.OrderItemDao;
import com.tp.dao.ord.OrderPointDao;
import com.tp.dao.ord.RejectInfoDao;
import com.tp.dao.ord.RejectItemDao;
import com.tp.dao.ord.RejectLogDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.RejectDetailDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.stg.Warehouse;
import com.tp.query.ord.RejectQuery;
import com.tp.service.IDocumentNumberGenerator;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.IOrderRejectRemoteService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.BigDecimalUtil;

/**
 * 订单取消服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service("orderRejectRemoteService")
@Transactional
public class OrderRejectRemoteService implements IOrderRejectRemoteService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RejectInfoDao  rejectInfoDao;

	@Autowired
	private RejectItemDao  rejectItemDao;

	@Autowired
	private RejectLogDao  rejectLogDao;

	@Autowired
	private OrderItemDao  orderItemDao;
	
	@Autowired
	private OrderPointDao orderPointDao;

	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IRejectInfoService rejectInfoService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IDocumentNumberGenerator documentNumberGenerator;

	@Override
	public void applyReturnGoods(MemberInfo user, RejectInfo rejectInfo,	RejectItem rejectItem) {
		try {
			if (null == user) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.value );
			}
			if(null == user.getId()) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.USERID_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.USERID_NULL.value  );
			}
			if (null == rejectInfo) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.value  );
			}
			if (null == rejectItem) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.value  );
			}
			
			ResultInfo<Boolean> msg  = rejectInfoService.validRejectInfo(rejectInfo);
			if(!msg.success){
				throw new OrderServiceException(msg.getMsg().getMessage());
			}
			
			OrderItem orderItem = new OrderItem();
			orderItem.setId(rejectItem.getOrderItemId());
			orderItem.setMemberId(user.getId());
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("id", rejectItem.getOrderItemId());
			param.put("memberId", user.getId());
			List<OrderItem> lineList = orderItemDao.queryByParam(param);
			
			orderItem = null!=lineList&&!lineList.isEmpty()?lineList.get(0):null;
			if (null == orderItem) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.ORDERLINE_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.ORDERLINE_NULL.value  );
			}
			Integer quantity = orderItem.getQuantity();
			Integer itemRefundQuantity = rejectItem.getItemRefundQuantity();
			if (itemRefundQuantity > quantity) {// 判断退货商品数量是否大于购买商品数量
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJEITEM_ITEMREFUNDQUANTITY_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.REJEITEM_ITEMREFUNDQUANTITY_ERROR.value  );
			}
			//计算下单时间是否超过30天
			SubOrder subOrder = new SubOrder();
			subOrder.setOrderCode(rejectInfo.getOrderCode());
			subOrder = subOrderService.queryUniqueByObject(subOrder);
			if(null==subOrder){
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.SUBORDER_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.SUBORDER_NULL.value  );
			}
			Date doneTime = subOrder.getDoneTime();
			if(null!=doneTime){
				BigDecimal timeBigDecimal = calculateOrderDonetime(doneTime);
				if(timeBigDecimal.longValue()>2592000000L){	//订单完成时间超过30天
					throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.ORDER_DONETIME_TIMEOUT.code, CUSTOMER_SERVICE_ERROR_CODE.ORDER_DONETIME_TIMEOUT.value  );
				}				
			}
			
			Double subTotal = orderItem.getSubTotal();
			Double refundAmount = BigDecimalUtil.formatToPrice(BigDecimalUtil.divide(subTotal, quantity,6).multiply( BigDecimal.valueOf(itemRefundQuantity))).doubleValue();
			if(refundAmount<0){
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_PRICE_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_PRICE_ERROR.value  );
			}
			rejectInfo.setRefundAmount(refundAmount);
			Integer refundPoint = BigDecimalUtil.multiply(BigDecimalUtil.divide(orderItem.getPoints(), quantity,6),itemRefundQuantity).intValue();
			rejectInfo.setPoints(refundPoint);
			rejectInfo.setCustomCode(subOrder.getCustomCode());
			
			////////
			//  `item_status` tinyint(4) DEFAULT NULL COMMENT '商品状态',
			////////
			rejectItem.setItemSkuCode(orderItem.getSkuCode());
			// rejectItem.setItemName(orderItem.getBrandName());
			rejectItem.setItemQuantity(orderItem.getQuantity());
			rejectItem.setItemUnitPrice(orderItem.getPrice());						
			rejectItem.setItemImgUrl(orderItem.getImg());
			rejectItem.setItemUnitPrice(orderItem.getPrice());
			rejectItem.setProductCode(orderItem.getProductCode());
			rejectItem.setItemName(orderItem.getSpuName());	
			if (rejectItem.getOrderCode() == null || !rejectInfo.getOrderCode().equals(rejectItem.getOrderCode()) ) {
				rejectItem.setOrderCode(rejectInfo.getOrderCode());
			}
			if (rejectItem.getItemRefundAmount() == null) {
				rejectItem.setItemRefundAmount(rejectInfo.getRefundAmount());
			}
			Long orderNo = rejectItem.getOrderCode();
			Long orderItemId = rejectItem.getOrderItemId();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderCode", orderNo);
			params.put("orderItemId", orderItemId);
			List<RejectItem> rejectItemList = rejectItemDao.queryByParam(params);
			
			if (CollectionUtils.isNotEmpty(rejectItemList)) {//验证该商品是否已经申请退货过
				List<Long> rejectNos = new ArrayList<Long>();
				// 遍历获取记录rejectNos
				for (RejectItem rejectItemDO2 : rejectItemList) {
					rejectNos.add(rejectItemDO2.getRejectCode());
				}
	
				List<RejectInfo> rejectInfoList = rejectInfoDao.selectByRejectNo(rejectNos);
				if (RejectConstant.REJECT_STATUS.CANCELED.code.intValue() != rejectInfoList.get(0)
						.getRejectStatus()
						&& RejectConstant.REJECT_STATUS.REJECTFAIL.code.intValue() != rejectInfoList.get(0)
								.getRejectStatus()) {
					throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REPEAT_APPLY_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.REPEAT_APPLY_ERROR.value  );
				}
			}		
			Integer count = rejectItemDao.queryByParamCount(params);
			// 退货次数
			if (count >= 3){
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_TIMES.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_TIMES.value  );				
			}

			checkField(user, rejectInfo, rejectItem, orderItem);			
			
			Long rejectNo = documentNumberGenerator.generate(Constant.DOCUMENT_TYPE.REJECT);
			rejectInfo.setRejectCode(rejectNo);
			rejectInfo.setSupplierName(orderItem.getSupplierName());
			rejectInfo.setSupplierId(orderItem.getSupplierId());
			rejectInfo.setUserId(user.getId());
			
			Warehouse warehouse = warehouseService.queryById(orderItem.getWarehouseId());
			if(null==warehouse) {
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.WAREHOUSE_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.WAREHOUSE_NULL.value  );				
			}
			rejectInfo.setReturnAddress(warehouse.getAddress());
			
			//获取到退货的积分
			initPoints(rejectInfo,orderItem,rejectItem);
			rejectInfoDao.insert(rejectInfo);

			rejectItem.setRejectCode(rejectNo);
			rejectItemDao.insert(rejectItem);

			OrderItem orderItemDO1 = new OrderItem();
			orderItemDO1.setRefundStatus(RejectConstant.REJECT_STATUS.APPLYING.code);
			orderItemDO1.setId(orderItem.getId());
			orderItemDao.updateNotNullById(orderItemDO1);
			
			saveRejectLog(user, rejectInfo, rejectItem);
		} catch (Exception e) {
			if(e instanceof OrderServiceException)
				throw new OrderServiceException(e.getMessage());
			else{
				logger.error("保存退货商品信息失败", e);
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.SYSTEM_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.SYSTEM_ERROR.value  );				
			}
		}
	}

	private BigDecimal calculateOrderDonetime(Date doneTime) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.setTime(doneTime);
		long lastly = c.getTimeInMillis();
		BigDecimal timeBigDecimal = BigDecimalUtil.subtract(now, lastly);
		return timeBigDecimal;
	}

	private void checkField(MemberInfo user, RejectInfo rejectInfo,
			RejectItem rejectItem, OrderItem orderItem) {
		if (null == rejectInfo.getAuditStatus())
			rejectInfo.setAuditStatus(RejectConstant.REJECT_AUDIT_STATUS.XG_AUDITING.code);
		if (null == rejectInfo.getRejectStatus())
			rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code);//申请退货
		if (null == rejectItem.getItemSkuCode())
			rejectItem.setItemSkuCode(orderItem.getSkuCode());
		if (null == rejectItem.getSupplierCode())
//			rejectItem.setSupplierCode(String.valueOf(orderItem.getSupplierId())); 
			rejectItem.setSupplierCode("0"); 
		if(null == rejectInfo.getOffsetAmount())
			rejectInfo.setOffsetAmount(0.0);
		if(null == rejectInfo.getCreateUser())
			rejectInfo.setCreateUser(user.getNickName());
		if(null == rejectInfo.getUpdateUser())
			rejectInfo.setUpdateUser(user.getNickName());
		if(null == rejectInfo.getCreateTime())
			rejectInfo.setCreateTime(new Date());
		if(null == rejectInfo.getUpdateTime())
			rejectInfo.setUpdateTime(new Date());
		if(null == rejectItem.getCreateUser())
			rejectItem.setCreateUser(user.getNickName());
		if(null == rejectItem.getUpdateUser())
			rejectItem.setUpdateUser(user.getNickName());
		if(null == rejectItem.getCreateTime())
			rejectItem.setCreateTime(new Date());
		if(null == rejectItem.getUpdateTime())
			rejectItem.setUpdateTime(new Date());
	}

	private void saveRejectLog(MemberInfo user, RejectInfo rejectInfo,
			RejectItem rejectItem) {
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(rejectInfo.getRejectCode());
		rejectLog.setOrderCode(rejectItem.getOrderCode());
		rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.APPLY.code);// 申请
		//rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code);// 申请退货
		rejectLog.setCurrentRejectStatus(rejectInfo.getRejectStatus());
		rejectLog.setLogContent(rejectInfo.getBuyerRemarks());
		rejectLog.setImgUrls(rejectInfo.getBuyerImgUrl());
		rejectLog.setOperatorName(rejectInfo.getCreateUser());
		rejectLog.setOperatorUserId(user.getNickName());
		rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
		rejectLog.setCreateTime(new Date());
		rejectLog.setCreateUser(rejectInfo.getCreateUser());
		rejectLogDao.insert(rejectLog);
	}

	public RejectDetailDTO showRejectHistory(RejectItem rejectItem) {
		if (null == rejectItem) {
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.code);
		}
		Long orderNo = rejectItem.getOrderCode();
		Long orderItemId = rejectItem.getOrderItemId();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderNo", orderNo);
		params.put("orderItemId", orderItemId);
		List<RejectItem> rejectItemList = rejectItemDao.queryByParam(params);

		if (null == rejectItemList || rejectItemList.isEmpty())
			return null;

		List<Long> rejectNos = new ArrayList<Long>();
		// 遍历获取记录rejectNos
		for (RejectItem rejectItemDO2 : rejectItemList) {
			rejectNos.add(rejectItemDO2.getRejectCode());
		}

		List<RejectInfo> rejectInfoList = rejectInfoDao.selectByRejectNo(rejectNos);

		if (CollectionUtils.isEmpty(rejectInfoList)) return null;

		if (rejectInfoList.size() != rejectItemList.size())
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.SYSTEM_ERROR.code);

		RejectDetailDTO detailDTO = new RejectDetailDTO(rejectInfoList.get(0),rejectItemList.get(0));
		
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(rejectItemList.get(0).getRejectCode());
		List<RejectLog> logList = rejectLogDao.queryByObject(rejectLog);

		List<RejectDetailDTO> detailList = new ArrayList<RejectDetailDTO>();
		
		for (int i = 1; i < rejectInfoList.size(); i++) {
			RejectDetailDTO detail = new RejectDetailDTO(rejectInfoList.get(i),rejectItemList.get(i));
			detailList.add(detail);			
		}
		detailDTO.setDetailList(detailList);
		detailDTO.setRejectLogList(logList);		
		
		return detailDTO;
		
	}
	
	public RejectDetailDTO showRejectHistoryForMemberId(RejectItem rejectItem,Long memberId) {
		if (null == rejectItem) {
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.value);
		}
		if(null == memberId){
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.value);
		}
		Long orderNo = rejectItem.getOrderCode();
		Long orderItemId = rejectItem.getOrderItemId();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderNo);
		List<RejectInfo> list = rejectInfoDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			RejectInfo info = list.get(0);
			if(info!= null){
				Long userId=info.getUserId();
				if(null != userId){
					if(userId.equals(memberId)){
						rejectItem.setOrderItemId(orderItemId);
						params.put("orderItemId", orderItemId);
						List<RejectItem> rejectItemList = rejectItemDao.queryByParam(params);
						
						if (null == rejectItemList || rejectItemList.isEmpty())
							return null;
						
						List<Long> rejectNos = new ArrayList<Long>();
						// 遍历获取记录rejectNos
						for (RejectItem rejectItemDO2 : rejectItemList) {
							rejectNos.add(rejectItemDO2.getRejectCode());
						}
						
						List<RejectInfo> rejectInfoList = rejectInfoDao.selectByRejectNo(rejectNos);
						
						if (CollectionUtils.isEmpty(rejectInfoList)) return null;
						
						if (rejectInfoList.size() != rejectItemList.size())
							throw new OrderServiceException(
									CUSTOMER_SERVICE_ERROR_CODE.SYSTEM_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.SYSTEM_ERROR.value);
						
						RejectDetailDTO detailDTO = new RejectDetailDTO(rejectInfoList.get(0),rejectItemList.get(0));
						
						RejectLog rejectLog = new RejectLog();
						rejectLog.setRejectCode(rejectItemList.get(0).getRejectCode());
						List<RejectLog> logList = rejectLogDao.queryByObject(rejectLog);
						
						List<RejectDetailDTO> detailList = new ArrayList<RejectDetailDTO>();
						
						for (int i = 1; i < rejectInfoList.size(); i++) {
							RejectDetailDTO detail = new RejectDetailDTO(rejectInfoList.get(i),rejectItemList.get(i));
							detailList.add(detail);			
						}
						detailDTO.setDetailList(detailList);
						detailDTO.setRejectLogList(logList);		
						
						return detailDTO;
					}
				}
			}
		}
		return null;		
	}

	
	

	public void cancelRejectInfo(Long rejectId, MemberInfo user ) {
		if (null == rejectId) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.value  );				

		RejectInfo rejectInfo = rejectInfoDao.queryById(rejectId);
		if(null == rejectInfo) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.value  );				
		
		if(!rejectInfo.getUserId().equals(user.getId())){
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.code,  CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.value );				
		}
		
		if(!RejectConstant.REJECT_STATUS.APPLYING.code.equals(rejectInfo.getRejectStatus())){
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_REJECTSTATUS_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_REJECTSTATUS_ERROR.value  );				
		}
		
		// 获取orderitemId //////////////////////////////////////////////////////
		RejectItem queryitem = new RejectItem();
		queryitem.setRejectCode(rejectInfo.getRejectCode());
		List<RejectItem> listItem = rejectItemDao.queryByObject(queryitem);
		if (null == listItem || listItem.isEmpty())
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.value  );				
		RejectItem rejItem =  listItem.get(0);
		////////////////////////////////////////////////////////////////////
		
		rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.CANCELED.code);
		rejectInfo.setAuditStatus(null);
		rejectInfoDao.updateById(rejectInfo);
		
		OrderItem orderItem = orderItemDao.queryById( rejItem.getOrderItemId() );		
		if(null == orderItem) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.ORDERLINE_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.ORDERLINE_NULL.value  );				
		
		orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.CANCELED.code);
		orderItemDao.updateNotNullById(orderItem);
		
		//记录日志
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(rejectInfo.getRejectCode());
		rejectLog.setOrderCode(rejectInfo.getOrderCode());
		rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.CANCEL.code);// 取消
		rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code);// 申请退货
		rejectLog.setCurrentRejectStatus(rejectInfo.getRejectStatus());
		rejectLog.setLogContent(rejectInfo.getBuyerRemarks());
		rejectLog.setImgUrls(rejectInfo.getBuyerImgUrl());
		rejectLog.setOperatorName(rejectInfo.getCreateUser());
		rejectLog.setOperatorUserId(rejectInfo.getCreateUser());
		rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
		rejectLog.setCreateTime(new Date());
		rejectLog.setCreateUser(rejectInfo.getCreateUser());
		rejectLogDao.insert(rejectLog);
	}
	
	public int updateBuyerRemarks(RejectInfo rejectInfo,MemberInfo user){
		int code=1;
		if(null==rejectInfo){
			return CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code;
		}
		if(null==rejectInfo.getRejectId()) return CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code;
		
		RejectInfo info = rejectInfoService.queryById(rejectInfo.getRejectId());
		if(null==info) return CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code;
		
		if(!info.getUserId().equals(user.getId())){
			return CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.code;
		}
		rejectInfoDao.updateNotNullById(rejectInfo);
		//记录日志
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(info.getRejectCode());
		rejectLog.setOrderCode(info.getOrderCode());
		rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.APPLY.code);// 申请
		//rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code);// 申请退货
		rejectLog.setCurrentRejectStatus(info.getRejectStatus());
		rejectLog.setLogContent(info.getBuyerRemarks());
		rejectLog.setImgUrls(info.getBuyerImgUrl());
		rejectLog.setOperatorName(info.getCreateUser());
		rejectLog.setOperatorUserId(info.getCreateUser());
		rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
		rejectLog.setCreateTime(new Date());
		rejectLog.setCreateUser(info.getCreateUser());
		rejectLogDao.insert(rejectLog);
		return code;
	}
	
	public void updateRejctData(MemberInfo user, RejectInfo rejectInfo,	RejectItem rejectItem){
		if(null==rejectInfo){
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code );
		}
		if(null==rejectInfo.getRejectId()) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code );

		
		RejectInfo info = rejectInfoService.queryById(rejectInfo.getRejectId());
		if(null==info) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code );

		if(!info.getUserId().equals(user.getId())){
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.code );
		}
		rejectInfoDao.updateNotNullById(rejectInfo);
		
		// 更新RejectItem
		RejectItem item = new RejectItem();
		item.setRejectCode( info.getRejectCode() );
		List<RejectItem> list = rejectItemDao.queryByObject( item );
		if( list ==null || list.isEmpty() )
				throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTITEM_NULL.code );
		 RejectItem itemdata = list.get(0);
		 itemdata.setItemRefundQuantity( rejectItem.getItemRefundQuantity() );
		 itemdata.setItemRefundAmount( rejectItem.getItemRefundAmount() );
		 rejectItemDao.updateNotNullById(itemdata);		
		 //////////////////////
		 
		//记录日志
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(info.getRejectCode());
		rejectLog.setOrderCode(info.getOrderCode());
		rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.APPLY.code);// 申请
		//rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code);// 申请退货
		rejectLog.setCurrentRejectStatus(info.getRejectStatus());
		rejectLog.setLogContent(info.getBuyerRemarks());
		rejectLog.setImgUrls(info.getBuyerImgUrl());
		rejectLog.setOperatorName(info.getCreateUser());
		rejectLog.setOperatorUserId(info.getCreateUser());
		rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
		rejectLog.setCreateTime(new Date());
		rejectLog.setCreateUser(info.getCreateUser());
		rejectLogDao.insert(rejectLog);
	}

	@Override
	public PageInfo<RejectInfo> queryMobilePageListByRejectQuery(
			RejectQuery rejectQuery) {
		if (null != rejectQuery) {
			PageInfo<RejectInfo> rejectInfoPage = rejectInfoService.queryPageListByRejectQuery(rejectQuery);
			return rejectInfoPage;
		}
		return new PageInfo<RejectInfo>();
	}
	
	public PageInfo<RejectInfo> queryPageList(RejectQuery rejectQuery){
		if(null!=rejectQuery){
			PageInfo<RejectInfo> pageList = this.queryMobilePageListByRejectQuery(rejectQuery);
			List<RejectInfo> list = pageList.getRows();
			List<RejectInfo> rejectInfoDTOList = new ArrayList<RejectInfo>();
			Long lastOrderNo = null;
			RejectInfo lastInfoDTO = null;
			if (CollectionUtils.isNotEmpty(list)) {
				for (RejectInfo rejectInfoDTO : list) {
					Long curOrderNo = rejectInfoDTO.getOrderCode();
					RejectItem rejectItem = rejectInfoDTO.getRejectItemList().get(0);
					if(!curOrderNo.equals(lastOrderNo)){
						lastOrderNo=curOrderNo;
						lastInfoDTO = new RejectInfo();
						lastInfoDTO.setOrderCode(curOrderNo);
						rejectInfoDTOList.add(lastInfoDTO);
					}
					//List<RejectInfo> rejectInfoDTOList = new ArrayList<RejectInfo>();
					lastInfoDTO.getRejectItemList().add(rejectItem);
				}	
				PageInfo<RejectInfo> page = new PageInfo<RejectInfo>();
				page.setPage(rejectQuery.getStartPage());
				page.setSize(rejectQuery.getPageSize());
				page.setRecords(pageList.getRecords());
				page.setRows(rejectInfoDTOList);
				return page;
			}		
		}
		return new PageInfo<RejectInfo>();		
	}


	@Override
	public int saveExpressNo(RejectInfo rejectInfoDTO) { 
		int code = 1;
		
		if(null == rejectInfoDTO.getCompanyName())
			return CUSTOMER_SERVICE_ERROR_CODE.COMPANYNAME_NULL.code;
		
		if(null == rejectInfoDTO.getExpressNo())
			return CUSTOMER_SERVICE_ERROR_CODE.EXPRESSNO_NULL.code;
				
		if(null == rejectInfoDTO.getRejectId()) 
			return CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code;
		
		RejectInfo rejectInfo = rejectInfoService.queryById(rejectInfoDTO.getRejectId()); 
		 
		rejectInfo.setExpressNo(rejectInfoDTO.getExpressNo());
		rejectInfo.setCompanyName(rejectInfoDTO.getCompanyName());
		rejectInfo.setCompanyCode(rejectInfoDTO.getCompanyCode());
		rejectInfoDao.updateNotNullById(rejectInfo);
		//记录日志
		RejectLog rejectLog = new RejectLog();
		rejectLog.setRejectCode(rejectInfo.getRejectCode());
		rejectLog.setOrderCode(rejectInfo.getOrderCode());
		rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.SENT_BACK.code);// 寄回商品
		rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);// 退货中
		rejectLog.setCurrentRejectStatus(rejectInfo.getRejectStatus());
		rejectLog.setLogContent(rejectInfo.getBuyerRemarks());
		rejectLog.setImgUrls(rejectInfo.getBuyerImgUrl());
		rejectLog.setOperatorName(rejectInfo.getCreateUser());
		rejectLog.setOperatorUserId(rejectInfo.getCreateUser());
		rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
		rejectLog.setCreateTime(new Date());
		rejectLog.setCreateUser(rejectInfo.getCreateUser());
		rejectLogDao.insert(rejectLog);
		return code;
	}
	
	
	
	public void saveExpressNoForMemberId(RejectInfo rejectInfo,Long memberId) { 
		int code = 1;
		
		if(null == rejectInfo.getCompanyName())		
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.COMPANYNAME_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.COMPANYNAME_NULL.value );
		
		if(null == rejectInfo.getPackageNo())
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.EXPRESSNO_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.EXPRESSNO_NULL.value );
				
		if(null == rejectInfo.getRejectId()) 
			throw new OrderServiceException( CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.value );
		
		if(null == memberId){
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.value);
		}
		
		RejectInfo oldRejectInfo = rejectInfoService.queryById(rejectInfo.getRejectId());
		
		if(null == oldRejectInfo) 
			throw new OrderServiceException(
				CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.value);
		
		if(null == oldRejectInfo.getUserId())
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.code, CUSTOMER_SERVICE_ERROR_CODE.USER_NULL.value);
		
		if(oldRejectInfo.getUserId().equals(memberId)){
			oldRejectInfo.setExpressNo(rejectInfo.getPackageNo());
			oldRejectInfo.setCompanyName(rejectInfo.getCompanyName());
			oldRejectInfo.setCompanyCode(rejectInfo.getCompanyCode());
			// by zhs 0303
			oldRejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);
			rejectInfoDao.updateNotNullById(oldRejectInfo);
			//记录日志
			RejectLog rejectLog = new RejectLog();
			rejectLog.setRejectCode(oldRejectInfo.getRejectCode());
			rejectLog.setOrderCode(oldRejectInfo.getOrderCode());
			rejectLog.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.SENT_BACK.code);// 寄回商品
			rejectLog.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);// 退货中
			rejectLog.setCurrentRejectStatus(rejectInfo.getRejectStatus() == null ? oldRejectInfo.getRejectStatus() : rejectInfo.getRejectStatus() );
			rejectLog.setLogContent(rejectInfo.getBuyerRemarks() ==null ?  oldRejectInfo.getBuyerRemarks() : rejectInfo.getBuyerRemarks() );
			rejectLog.setImgUrls( rejectInfo.getBuyerImgUrl()==null ?  oldRejectInfo.getBuyerImgUrl() : rejectInfo.getBuyerImgUrl() );
			rejectLog.setOperatorName(rejectInfo.getCreateUser()==null?  oldRejectInfo.getCreateUser() : rejectInfo.getCreateUser());
			rejectLog.setOperatorUserId(rejectInfo.getCreateUser()==null ? oldRejectInfo.getCreateUser() : rejectInfo.getCreateUser() );
			rejectLog.setOperatorType(LogTypeConstant.LOG_TYPE.MEMBER.code);// 会员
			rejectLog.setCreateTime(new Date());
			rejectLog.setCreateUser(rejectInfo.getCreateUser()==null ? oldRejectInfo.getCreateUser() : rejectInfo.getCreateUser() );
			rejectLogDao.insert(rejectLog);
		}else {
			throw new OrderServiceException(
					CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.code, CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_USER_ERROR.value);
		}		
	}


	@Override
	public RejectInfo queryRejectDetailByNo(Long rejectNo) {
		if(null == rejectNo) {
			throw new OrderServiceException(OrderErrorCodes.CUSTOMER_SERVICE_ERROR_CODE.REJECTNO_NULL.code, OrderErrorCodes.CUSTOMER_SERVICE_ERROR_CODE.REJECTNO_NULL.value);
		}
		RejectInfo rejectInfoDTO = rejectInfoService.queryRejectInfoByNo(rejectNo);
		if(null != rejectInfoDTO){
			RejectLog rejectLog = new RejectLog();
			rejectLog.setRejectCode(rejectNo);
			List<RejectLog> logList = rejectLogDao.queryByObject(rejectLog);
			rejectInfoDTO.setRejectLogList(logList);
		}
		return rejectInfoDTO;
	}
	
	private void initPoints(RejectInfo rejectInfo,OrderItem orderItem,RejectItem rejectItem){
		rejectInfo.setPoints(0);
		Integer points = 0;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderItemId", orderItem.getId());
		List<OrderPoint> orderPointList = orderPointDao.queryByParam(params);
		Integer refundPoints = 0;
		if(CollectionUtils.isNotEmpty(orderPointList)){
			for(OrderPoint orderPoint:orderPointList){
				refundPoints+=orderPoint.getRefundedPoint();
				points+=orderPoint.getPoint();
			}
		}
		if(points-refundPoints>0){
			Integer point = BigDecimalUtil.multiply(
					BigDecimalUtil.divide(points, orderItem.getQuantity(),2),
					rejectItem.getItemRefundQuantity()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			if(point>(points-refundPoints)){
				point = points-refundPoints;
			}
			rejectInfo.setPoints(point);
		}
	}
}
