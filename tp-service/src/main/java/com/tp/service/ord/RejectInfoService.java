package com.tp.service.ord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dao.ord.RejectInfoDao;
import com.tp.dao.ord.RejectItemDao;
import com.tp.dao.ord.RejectLogDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.mmp.PointDetail;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.query.ord.RejectQuery;
import com.tp.service.BaseService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.dss.IChannelInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderPointService;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Service
public class RejectInfoService extends BaseService<RejectInfo> implements IRejectInfoService {

	@Autowired
	private RejectInfoDao rejectInfoDao;
	@Autowired
	private RejectItemDao rejectItemDao;
	@Autowired
	private RejectLogDao rejectLogDao;
	@Autowired
	private IRefundInfoService refundInfoService;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private IOrderPointService orderPointService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IPointDetailService pointDetailService;
	
	@Autowired
	private IForbiddenWordsService forbiddenWordsService;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IChannelInfoService channelInfoService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Override
	public BaseDao<RejectInfo> getDao() {
		return rejectInfoDao;
	}

	@Override
	public void refundResult(Long refundNo, Boolean isSuccess) {
		RejectInfo rejectInfo = queryRejectByRefundNo(refundNo);
		if(isSuccess){
			
			RejectLog log = new RejectLog();
			log.setRejectCode(rejectInfo.getRejectCode());
			log.setOrderCode(rejectInfo.getOrderCode());
			log.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.REFUND.code);
			log.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);
			log.setCurrentRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
			log.setAuditStatus(rejectInfo.getAuditStatus());
			log.setLogContent(RejectConstant.REJECT_STATUS.REJECTED.cnName);
			log.setOperatorType(4);
			log.setOperatorName("system");
			rejectLogDao.insert(log);
			
			RejectInfo toBeUpdated = new RejectInfo();
			toBeUpdated.setRejectId(rejectInfo.getRejectId());
			toBeUpdated.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
			rejectInfoDao.updateNotNullById(toBeUpdated);
			
			RejectItem query = new RejectItem();
			query.setRejectCode(rejectInfo.getRejectCode());
			List<RejectItem> rejectItemDOList = rejectItemDao.queryByObject(query);
			if(!CollectionUtils.isEmpty(rejectItemDOList)){
				OrderItem toBeUpdatedOrderItem = new OrderItem();
				toBeUpdatedOrderItem.setId(rejectItemDOList.get(0).getOrderItemId());
				toBeUpdatedOrderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
				orderItemService.updateNotNullById(toBeUpdatedOrderItem);
			}
			else
				logger.warn("退货单号"+rejectInfo.getRejectCode()+"没找到对应的item");
			// sendSmsService.sendSms(rejectInfo.getLinkMobile(), "您的订单#"+ rejectInfo.getOrderCode() +"#已退款成功，请登陆账号查询！【西客商城】");
		}
	}
	
	@Override
	public RejectInfo queryRejectByRefundNo(Long refundNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("refundCode", refundNo);
		return queryRejectByBussinessNo(params);
	}
	
	public RejectInfo queryRejectByBussinessNo(Map<String,Object> param){
		RejectInfo rejectInfo = super.queryUniqueByParams(param);
		if(null!=rejectInfo){
			param.clear();
			param.put("rejectCode", rejectInfo.getRejectCode());
			List<RejectItem> rejectItemList = rejectItemDao.queryByParam(param);
			List<RejectLog> rejectLogList = rejectLogDao.queryByParam(param);
			rejectInfo.setRejectItemList(rejectItemList);
			rejectInfo.setRejectLogList(rejectLogList);
		}
		return rejectInfo;
	}
	@Override
	public void updateForForceAudit(RejectInfo rejectInfo) {
		Long refundNo = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectInfo.getRejectCode());
		List<RejectItem> rejectItemDOList = rejectItemDao.queryByParam(params);
		SubOrder subOrder = subOrderService.findSubOrderByCode(rejectInfo.getOrderCode());
		if(null!=subOrder){
			if (!CollectionUtils.isEmpty(rejectItemDOList)) {
				OrderItem toBeUpdatedOrderItem = new OrderItem();
				toBeUpdatedOrderItem.setId(rejectItemDOList.get(0)
						.getOrderItemId());
				toBeUpdatedOrderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);
				orderItemService.updateNotNullById(toBeUpdatedOrderItem);
			}
			refundPoin(rejectInfo,rejectItemDOList.get(0));
			RefundInfo refundInfo = new RefundInfo();
			refundInfo.setCreateTime(new Date());
			refundInfo.setCreateUser(rejectInfo.getUpdateUser());
			refundInfo.setOrderCode(rejectInfo.getOrderCode());
			refundInfo.setRefundAmount(rejectInfo.getRefundAmount());
			refundInfo.setRefundType(RefundConstant.REFUND_TYPE.REJECT.code);
			refundInfo.setUpdateTime(new Date());
			refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
			Long orderCode = subOrder.getOrderCode();
			if (OrderUtils.isSeaOrder(subOrder.getType())) {
				orderCode = subOrder.getOrderCode();
			}
			PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderCode);
			if (null == paymentInfo) {
				return;
			}
			Long gatewayId = paymentInfo.getGatewayId();
			refundInfo.setGatewayId(gatewayId);
			refundInfo = refundInfoService.insert(refundInfo);
			if (refundInfo != null)
				refundNo = refundInfo.getRefundCode();
			rejectInfo.setRefundCode(refundNo);
			
		}
		int result = rejectInfoDao.updateNotNullById(rejectInfo);
		if (result > 0) {
			RejectLog log = new RejectLog();
			log.setRejectCode(rejectInfo.getRejectCode());
			log.setOrderCode(rejectInfo.getOrderCode());
			log.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.USER_AUDIT.code); //商家审核
			log.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code); // 退货中
			log.setCurrentRejectStatus(rejectInfo.getRejectStatus());
			log.setAuditStatus(rejectInfo.getAuditStatus()); //审核状态
			log.setLogContent(rejectInfo.getRemarks());
			log.setOperatorType(Constant.LOG_AUTHOR_TYPE.USER_CALL.code);
			log.setOperatorName(rejectInfo.getUpdateUser());
			rejectLogDao.insert(log);
		}
	}

	@Override
	public void updateForAudit(RejectInfo rejectInfo) {
		RejectInfo rejectInfo2 = queryById(rejectInfo.getRejectId());
		
		RejectItem rejectItem = rejectItemDao.queryById(rejectInfo.getRejectItemId());
		
		if( !rejectItem.getRejectCode().equals( rejectInfo.getRejectCode()  ) ){
			return;
		}
		
		RejectLog log = new RejectLog();
		if(rejectInfo2.getRefundAmount()<rejectInfo.getRefundAmount()){
			Assert.isTrue(Boolean.FALSE,"修改退货金额不能大于所退商品本来金额");
		}else if(rejectInfo2.getRefundAmount().doubleValue()!=rejectInfo.getRefundAmount().doubleValue()){
			log.setLogContent(rejectInfo.getRemarks()+" ,修改退货金额："+rejectInfo2.getRefundAmount()+"->"+rejectInfo.getRefundAmount());
			rejectInfo2.setRefundAmount(rejectInfo.getRefundAmount());
		}else{
			log.setLogContent(rejectInfo.getRemarks());
		}						
		
		Integer auditStatus = rejectInfo.getAuditStatus();
		Long refundNo = null;
		
		if(!rejectItem.getSupplierCode().equals("0") &&  rejectInfo.getSuccess()){
			if(!RejectConstant.REJECT_AUDIT_STATUS.SELLER_FIAL.code.equals(rejectInfo2.getAuditStatus())){
				auditStatus = RejectConstant.REJECT_AUDIT_STATUS.SELLER_AUDITING.code;					
			}else{
				auditStatus = RejectConstant.REJECT_AUDIT_STATUS.END_XG_AUDITED.code; // 客服终审通过
				SubOrder subOrder = subOrderService.findSubOrderByCode(rejectInfo.getOrderCode());
				if(null!=subOrder){
					refundPoin(rejectInfo,rejectItem);
					if(rejectInfo.getRefundAmount().doubleValue()!=0){
						RefundInfo refundInfo = new RefundInfo();
						refundInfo.setCreateTime(new Date());
						refundInfo.setCreateUser(rejectInfo.getCreateUser());
						refundInfo.setOrderCode(rejectInfo2.getOrderCode());
						refundInfo.setRefundAmount(rejectInfo.getRefundAmount());
						refundInfo.setRefundType(RefundConstant.REFUND_TYPE.REJECT.code);
						refundInfo.setUpdateTime(new Date());
						refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.AUDITING.code);
						Long orderNo = subOrder.getOrderCode();
						if(OrderUtils.isSeaOrder(subOrder.getType())){
							orderNo = subOrder.getOrderCode();
						}
						PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderNo);
						if(null==paymentInfo){
							Assert.isTrue(Boolean.FALSE,"付款信息为空");
						}
						Long gatewayId = paymentInfo.getGatewayId();
						refundInfo.setGatewayId(gatewayId);
						refundInfo = refundInfoService.insert(refundInfo);
						if(refundInfo != null)
							refundNo = refundInfo.getRefundCode();						
					}
					
				}
			}			
		}else if(rejectItem.getSupplierCode().equals("0") && rejectInfo.getSuccess()){
			if(RejectConstant.REJECT_AUDIT_STATUS.XG_AUDITED.code.equals(rejectInfo2.getAuditStatus())){
				auditStatus = RejectConstant.REJECT_AUDIT_STATUS.END_XG_AUDITED.code;
				SubOrder subOrder = subOrderService.findSubOrderByCode(rejectInfo.getOrderCode());
				if(null!=subOrder){
					refundPoin(rejectInfo,rejectItem);
					if(rejectInfo.getRefundAmount().doubleValue()!=0){
						RefundInfo refundInfo = new RefundInfo();
						refundInfo.setCreateTime(new Date());
						refundInfo.setCreateUser(rejectInfo.getCreateUser());
						refundInfo.setOrderCode(rejectInfo2.getOrderCode());
						refundInfo.setRefundAmount(rejectInfo2.getRefundAmount());
						refundInfo.setRefundType(RefundConstant.REFUND_TYPE.REJECT.code);
						refundInfo.setUpdateTime(new Date());
						refundInfo.setUpdateUser(rejectInfo.getCreateUser());
						refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.AUDITING.code);
						Long orderNo = subOrder.getOrderCode();
						if(OrderUtils.isSeaOrder(subOrder.getType())){
							orderNo = subOrder.getOrderCode();
						}
						PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderNo);
						if(null==paymentInfo){
							logger.error("{}->{}付款信息为空",subOrder.getOrderCode(),orderNo);
							Assert.isTrue(Boolean.FALSE,"付款信息为空");
						}
						Long gatewayId = paymentInfo.getGatewayId();
						refundInfo.setGatewayId(gatewayId);
						refundInfo = refundInfoService.insert(refundInfo);
						if(refundInfo != null)
							refundNo = refundInfo.getRefundCode();
					}
				}
			}else{
				auditStatus = RejectConstant.REJECT_AUDIT_STATUS.XG_AUDITED.code;//添加了 二次审核，第一次则不生成退款单
			}
		}else{
			if(  RejectConstant.REJECT_AUDIT_STATUS.XG_AUDITED.code.equals(rejectInfo2.getAuditStatus()) ){
				auditStatus = RejectConstant.REJECT_AUDIT_STATUS.END_XG_FIAL.code;
			}
		}
	
		rejectAudit(rejectInfo, auditStatus);
		rejectInfo.setAuditStatus(auditStatus);
		rejectInfo.setUpdateUser(rejectInfo.getCreateUser());
		rejectInfo.setRefundCode(refundNo);
		int result = rejectInfoDao.updateNotNullById(rejectInfo);
		if (result > 0) {
			log.setRejectCode(rejectInfo.getRejectCode());
			log.setOrderCode(rejectInfo.getOrderCode());
			log.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.USER_AUDIT.code); //客服审核
			log.setOldRejectStatus(RejectConstant.REJECT_STATUS.APPLYING.code); // 申请退货
			log.setCurrentRejectStatus(rejectInfo.getRejectStatus());
			log.setAuditStatus(rejectInfo.getAuditStatus()); //审核状态			
			log.setOperatorType(rejectInfo.getOperatorType());
			log.setOperatorName(rejectInfo.getCreateUser());
			rejectLogDao.insert(log);
		}
	}
	
	private void rejectAudit(RejectInfo rejectInfo, Integer auditStatus) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectInfo.getRejectCode());
		List<RejectItem> rejectItemList = rejectItemDao.queryByParam(params);
		
		if (auditStatus == RejectConstant.REJECT_AUDIT_STATUS.XG_AUDITED.code
				.intValue()){
			rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.CONFIRMING.code);
			if(CollectionUtils.isNotEmpty(rejectItemList)){
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rejectItemList.get(0).getOrderItemId());
				orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.CONFIRMING.code);
				orderItemService.updateNotNullById(orderItem);
			}
			//sendMessage(rejectInfo);
		} else if(auditStatus == RejectConstant.REJECT_AUDIT_STATUS.SELLER_AUDITING.code.intValue()){
			rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);			
			if(CollectionUtils.isNotEmpty(rejectItemList)){
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rejectItemList.get(0).getOrderItemId());
				orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTING.code);
				orderItemService.updateNotNullById(orderItem);
			}
			//sendMessage(rejectInfo);
		} else if(auditStatus == RejectConstant.REJECT_AUDIT_STATUS.END_XG_AUDITED.code.intValue()){			
			if(CollectionUtils.isNotEmpty(rejectItemList)){
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rejectItemList.get(0).getOrderItemId());
				if(rejectInfo.getRefundAmount().doubleValue()==0){
					rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
					orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
				}else{
					rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);							
					orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);
				}
				orderItemService.updateNotNullById(orderItem);
				
				//退还积分
			}
		}
		else {		
			rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTFAIL.code);// 审核不通过,退货失败
			if(CollectionUtils.isNotEmpty(rejectItemList)){
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rejectItemList.get(0).getOrderItemId());
				orderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTFAIL.code);
				orderItemService.updateNotNullById(orderItem);
			}
		}
	}

	@Override
	public List<RejectInfo> queryNotSuccessPostKuaidi100List(RejectInfo rejectInfo) {
		return rejectInfoDao.queryNotSuccessPostKuaidi100List(rejectInfo);
	}

	@Override
	public Integer batchUpdatePostKuaidi100(List<RejectInfo> rejectInfoDOList) {
		return rejectInfoDao.batchUpdatePostKuaidi100(rejectInfoDOList);
	}

	@Override
	public Integer updatePostKuaidi100(RejectInfo rejectInfo) {
		return rejectInfoDao.updatePostKuaidi100(rejectInfo);
	}

	@Override
	public List<RejectInfo> selectListByRejectNoAndPackageNo(Long rejectNo, String packageNo) {
		return rejectInfoDao.selectListByRejectNoAndPackageNo(rejectNo, packageNo);
	}

	@Override
	public PageInfo<RejectInfo> queryPageListByRejectQuery(RejectQuery rejectQuery) {
		if (rejectQuery != null) {
			Map<String,Object> params = BeanUtil.beanMap(rejectQuery);
			Integer totalCount = rejectInfoDao.queryPageListByRejectQueryCount_DistinctByOrdercode(params);					
			List<RejectInfo> resultList = rejectInfoDao.queryPageListByRejectQuery_DistinctByOrdercode(rejectQuery);
			if (CollectionUtils.isNotEmpty(resultList)) {
				List<Long> rejectNos = new ArrayList<Long>();
				
				
				for(RejectInfo rej : resultList ){
					rejectNos.add(rej.getRejectCode());					
				}
				
				List<RejectItem> rejectItemDOList = rejectItemDao.queryListByRejectNos(rejectNos);
								
				if (CollectionUtils.isNotEmpty(rejectItemDOList)) {
					for (RejectInfo rejectInfo : resultList) {
						for (RejectItem rejectItem : rejectItemDOList) {
							if (rejectInfo.getRejectCode().equals(rejectItem.getRejectCode())) {		
								
								if(rejectItem.getItemImgUrl() != null && !rejectItem.getItemImgUrl().contains(Constant.IMAGE_URL_TYPE.item.url) ){
									rejectItem.setItemImgUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, rejectItem.getItemImgUrl()));										
								}
								rejectInfo.getRejectItemList().add(rejectItem);
								
								///////////////////////////////////// by zhs  统计售后次数
								Long orderNo = rejectItem.getOrderCode();
								Long orderItemId = rejectItem.getOrderItemId();
								Map<String,Object> paramquery = new HashMap<String,Object>();
								paramquery.put("orderCode", orderNo);
								paramquery.put("orderItemId", orderItemId);				
								Integer count = rejectItemDao.queryByParamCount(paramquery);
								rejectInfo.setCustServCount( count );
								///////////////////
																
								////////////// by zhs 0304
								if ( rejectInfo.getOrderCode().equals( rejectItem.getOrderCode() ) ) {
									OrderItem orderItem = new OrderItem();
									orderItem.setOrderCode( rejectInfo.getOrderCode());
									List<OrderItem> list = orderItemService.queryByObject(orderItem);
									if( list==null || list.get(0) == null )
										return new PageInfo<RejectInfo>();									
									rejectInfo.setSubTotal( list.get(0).getSubTotal() );									
								}
								//////////////								
							}
						}
					}
				}
				PageInfo<RejectInfo> page = new PageInfo<RejectInfo>();
				page.setPage(rejectQuery.getStartPage());
				page.setSize(rejectQuery.getPageSize());
				page.setRecords(totalCount.intValue());
				page.setRows(resultList);
				return page;
			}
		}
		return new PageInfo<RejectInfo>();
	}
	
	@Override
	public void updateForSellerAudit(RejectInfo rejectInfo, Integer operatorType,String createUser) {
		if(null == rejectInfo){
			throw new OrderServiceException(OrderErrorCodes.CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code);
		}
		String refundNo = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectInfo.getRejectCode());
		List<RejectItem> rejectItemDOList = rejectItemDao.queryByParam(params);
		
		if (rejectInfo.getAuditStatus() == RejectConstant.REJECT_AUDIT_STATUS.SELLER_AUDITED.code
				.intValue()) {
	
			SubOrder subOrderDTO = salesOrderRemoteService.findSubOrderByCode(rejectInfo.getOrderCode());
			if(null!=subOrderDTO){  
				if(rejectInfo.getRefundAmount().doubleValue()==0){
					rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
					if(!CollectionUtils.isEmpty(rejectItemDOList)){       // 维护orderLine的退款状态 , 如果状态不变,就不维护了
						OrderItem toBeUpdatedOrderItem = new OrderItem();
						toBeUpdatedOrderItem.setId(rejectItemDOList.get(0).getOrderItemId());
						toBeUpdatedOrderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
						orderItemService.updateNotNullById(toBeUpdatedOrderItem);
					}
				}else{	//生成退款单
					rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);
					if(!CollectionUtils.isEmpty(rejectItemDOList)){
						OrderItem toBeUpdatedOrderItem = new OrderItem();
						toBeUpdatedOrderItem.setId(rejectItemDOList.get(0).getOrderItemId());
						toBeUpdatedOrderItem.setRefundStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);
						orderItemService.updateNotNullById(toBeUpdatedOrderItem);
					}
					refundPoin(rejectInfo,rejectItemDOList.get(0));
					RefundInfo refundInfo = new RefundInfo(); 
					refundInfo.setCreateTime(new Date());
					refundInfo.setCreateUser(createUser);
					refundInfo.setOrderCode(rejectInfo.getOrderCode());
					refundInfo.setRefundAmount(rejectInfo.getRefundAmount());
					refundInfo.setRefundType(RefundConstant.REFUND_TYPE.REJECT.code);
					refundInfo.setUpdateTime(new Date());
//					refundInfo.setModifyUser(createUser);
					refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
					Long orderNo = subOrderDTO.getOrderCode();
					if(OrderUtils.isSeaOrder(subOrderDTO.getType())){
						orderNo = subOrderDTO.getOrderCode();
					}
					PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderNo);
					if(null==paymentInfo){
						throw new OrderServiceException("付款信息为空");
					}
					Long gatewayId = paymentInfo.getGatewayId();
	 				refundInfo.setGatewayId(gatewayId);
					refundInfo = refundInfoService.insert(refundInfo);	
				}
			}
			
		} else {		
			rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);
			rejectInfo.setAuditStatus(RejectConstant.REJECT_AUDIT_STATUS.SELLER_FIAL.code);  // 商家审核不通过
		}
		
		rejectInfo.setUpdateUser(createUser);
		int result = rejectInfoDao.updateForAudit(rejectInfo);
		if (result > 0) {
			RejectLog log = new RejectLog();
			log.setRejectCode(rejectInfo.getRejectCode());
			log.setOrderCode(rejectInfo.getOrderCode());
			log.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.SELLER_AUDIT.code); //商家审核
			log.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code); // 退货中
			log.setCurrentRejectStatus(rejectInfo.getRejectStatus());
			log.setAuditStatus(rejectInfo.getAuditStatus()); //审核状态
			log.setLogContent(rejectInfo.getSellerRemarks());
			log.setOperatorType(operatorType);
			log.setOperatorName(createUser);
			rejectLogDao.insert(log);
		}
	}
	@Override
	public RejectInfo queryRejectInfoByNo(Long rejectNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectNo);
		RejectInfo rejectInfo = super.queryUniqueByParams(params);
		if(rejectInfo!=null){
			rejectInfo.setRejectItemList(rejectItemDao.queryByParam(params));
			rejectInfo.setRejectLogList(rejectLogDao.queryByParam(params));
		}
		return rejectInfo;
	}
	
	@Override
	public RejectInfo queryRejectItemByRejectId(Long rejectId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectId", rejectId);
		RejectInfo rejectInfo = super.queryUniqueByParams(params);
		if(rejectInfo!=null){
			params.clear();
			params.put("rejectCode", rejectInfo.getRejectCode());
			rejectInfo.setRejectItemList(rejectItemDao.queryByParam(params));
			rejectInfo.setRejectLogList(rejectLogDao.queryByParam(params));
		}
		return rejectInfo;
	}
	
	@Override
	public List<RejectInfo> queryRejectInfoListByRefundNoList(List<Long> refundNos) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " reject_code in ("+StringUtil.join(refundNos, Constant.SPLIT_SIGN.COMMA)+")");
		return rejectInfoDao.queryByParam(params);
	}
	private void sendMessage(RejectInfo rejectInfo) {
		if(null == rejectInfo.getLinkMobile()){
			throw new OrderServiceException("用户联系号码为空");
		}
		try {
			sendSmsService.sendSms(rejectInfo.getLinkMobile(), "您的订单#"+rejectInfo.getOrderCode()+"#已申请退货，请在24小时内将退货寄回，具体退货地址请登陆账号查看！【西客商城】",null);
		} catch (Exception e) {
			logger.error("发送短信消息失败", e);
		}
	}
	

	/**
	 * <pre>
	 *    获取违禁词列表
	 * </pre>
	 */
	public List<ForbiddenWords> getForbiddenWords(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		return forbiddenWordsService.queryByParam(params);
	}
	
	/**
	 * <pre>
	 *  验证违禁词
	 * </pre>
	 * @param sourceField
	 * @param fieldDesc
	 * @param fobiddenWords
	 * @return
	 */
	public  String checkForbiddenWordsField(String sourceField ,String fieldDesc,List<ForbiddenWords> fobiddenWords){
		StringBuffer sb = new StringBuffer(fieldDesc);
		boolean flag = false;
		sb.append("中有");
		if(CollectionUtils.isNotEmpty(fobiddenWords)){
			if(StringUtil.isNotBlank(sourceField)){
				for(ForbiddenWords forbiddenWords :fobiddenWords ){
					String words = forbiddenWords.getWords();
					if(StringUtil.isNotBlank(words)){
						int total = checkStrCount(sourceField,words);
						if(total>0){
							flag = true;
							sb.append(": 违禁词[").append(forbiddenWords)
							  .append("],总共出现").append(total).append("次。");
						}
					}
				}
			}
		}
		if(flag){
			return sb.toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	  校验退货商品的信息
	 * </pre>
	 *
	 * @param info
	 * @return
	 */
	public ResultInfo<Boolean> validRejectInfo(RejectInfo info) {
		List<ForbiddenWords>  list = getForbiddenWords() ;
		
		//违禁词校验
		String checkMsg = "" ; 
		if(StringUtils.isNotEmpty(info.getBuyerRemarks())){
			checkMsg = checkForbiddenWordsField(info.getBuyerRemarks(),"问题描述",list);
		}
		if(StringUtils.isNotEmpty(info.getLinkMan())){
			checkMsg += checkForbiddenWordsField(info.getLinkMan(),"联系人",list);
		}
		if(StringUtils.isNotEmpty(info.getRemarks())){
			checkMsg += checkForbiddenWordsField(info.getRemarks(),"客服处理信息",list);
		}
		if(StringUtils.isNotEmpty(info.getSellerRemarks())){
			checkMsg += checkForbiddenWordsField(info.getSellerRemarks(),"商家处理信息",list);
		}
		if(StringUtils.isNotEmpty(checkMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkMsg));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 退还积分
	 * @param rejectInfo
	 * @param rejectItem
	 * @param subOrder
	 */
	private void refundPoin(RejectInfo rejectInfo,RejectItem rejectItem){
		OrderItem orderItem = orderItemService.queryById(rejectItem.getOrderItemId());
		if(rejectInfo.getPoints()>0){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderItemId", orderItem.getId());
			List<OrderPoint> orderPointList = orderPointService.queryByParam(params);
			Integer refundPoints = 0;
			if(CollectionUtils.isNotEmpty(orderPointList)){
				for(OrderPoint orderPoint:orderPointList){//不区别积分包
					refundPoints +=orderPoint.getRefundedPoint();
					orderPoint.setRefundedPoint(orderPoint.getRefundedPoint()+rejectInfo.getPoints());
				}
				if(refundPoints+rejectInfo.getPoints()>orderItem.getPoints()){
					logger.info("返还的积分不能大于使用的积分 ，返还{},使用{}",refundPoints+rejectInfo.getPoints(),orderItem.getPoints());
					rejectInfo.setPoints(orderItem.getPoints()-refundPoints);
				}
				
				if(rejectInfo.getPoints()>0){
					params.clear();
					params.put("bizType", PointConstant.BIZ_TYPE.ORDER.code);
					params.put("bizId", orderPointList.get(0).getParentOrderCode());
					params.put("memberId", rejectInfo.getUserId());
					PointDetail pointDetail = pointDetailService.queryUniqueByParams(params);
					if(null!=pointDetail){
						pointDetail.setPoint(rejectInfo.getPoints());
						pointDetail.setCreateUser(rejectInfo.getUpdateUser());
						pointDetail.setTitle(PointConstant.BIZ_TYPE.REFUNED.title);
						pointDetail.setRelateBizType(PointConstant.BIZ_TYPE.ORDER.code);
						pointDetail.setBizType(PointConstant.BIZ_TYPE.REFUNED.code);
						pointDetailService.updatePointByRefund(pointDetail);
						
						for(OrderPoint orderPoint:orderPointList){
							orderPointService.updateById(orderPoint);
						}
					}
				}
			}
		}
		
		//第三方商城积分接入--start
		Map<String, Object> channeParams = new HashMap<>();
		SubOrder subOrderInfo=	subOrderService.selectOneByCode(rejectInfo.getOrderCode());
		channeParams.put("channelCode",subOrderInfo.getChannelCode());
		ChannelInfo channelInfo = channelInfoService.queryUniqueByParams(channeParams);
		if(channelInfo!=null && "1".equals(channelInfo.getIsUsedPoint())){//是否使用自己商城的积分
			HhbShopOrderInfoDTO  hhbOrderInfo=new HhbShopOrderInfoDTO();
			SubOrder subOrder = subOrderService.findSubOrderByCode(rejectInfo.getOrderCode());
			//佣金累计
            Double  subReturnMoney=orderItem.getSalesPrice()*orderItem.getCommisionRate();
            subOrder.setReturnMoney(subReturnMoney);
			String userMobile=memberInfoService.getLoginInfoByMemId(subOrder.getMemberId()).getMobile();
			String openId=memberInfoService.getByMobile(userMobile).getTpin();
			hhbOrderInfo.setOpenId(openId);
			hhbOrderInfo.setCode(String.valueOf(subOrder.getOrderCode()));//订单编号
			hhbOrderInfo.setCash(rejectInfo.getRefundAmount());//应退金额
			hhbOrderInfo.setIntegral(0D);//使用积分数
			hhbOrderInfo.setReturnMoney(subOrder.getReturnMoney());//返佣金额
			hhbOrderInfo.setTotalMoney(rejectInfo.getRefundAmount()+Double.valueOf(rejectInfo.getPoints())/Double.valueOf(100));//应退总金额
			hhbOrderInfo.setType("0");//退单
			hhbOrderInfo.setBalance(Double.valueOf(rejectInfo.getPoints()));//退还积分数
			memberInfoService.sendOrderToThirdShop(subOrder.getChannelCode(), hhbOrderInfo);//发送订单到第三方商城
		}
		//第三方商城积分接入--end
	}
	
	/**
	 * 
	 * <pre>
	 *  判断一个字符串出现另一个字符串的次数
	 * </pre>
	 *
	 * @param str
	 * @param checkStr
	 * @return
	 */
	private static  int checkStrCount(String str, String checkStr){
		int total = 0;
		for (String tmp = str; 	tmp!= null
				&&tmp.length()>=checkStr.length();){
		  if(tmp.indexOf(checkStr) == 0){
		    total ++;
		  }
		  tmp = tmp.substring(1);
		}
		return total;
	}
	
}
