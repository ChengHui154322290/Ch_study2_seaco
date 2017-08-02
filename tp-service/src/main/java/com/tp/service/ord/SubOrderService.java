package com.tp.service.ord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.SalesOrderConstant.OrderIsDeleted;
import com.tp.common.vo.ord.SubOrderConstant.PutStatus;
import com.tp.dao.ord.Kuaidi100ExpressDao;
import com.tp.dao.ord.OrderDeliveryDao;
import com.tp.dao.ord.OrderInfoDao;
import com.tp.dao.ord.OrderStatusLogDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.dto.ord.OrderItemCMBC;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.ord.Kuaidi100Express;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.ExpressLogInfoDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.BaseService;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.util.StringUtil;

@Service
public class SubOrderService extends BaseService<SubOrder> implements ISubOrderService {

	@Autowired
	private SubOrderDao subOrderDao;
	@Autowired
	private OrderInfoDao orderInfoDao;
	@Autowired
	private OrderDeliveryDao orderDelvieryDao;
	@Autowired
	private Kuaidi100ExpressDao kuaidi100ExpressDao;
	@Autowired
	private OrderStatusLogDao orderStatusLogDao;
	
	@Autowired
	private IRejectInfoService rejectInfoService;
	
	@Autowired
	private IPromoterInfoService promoterInfoService;
	
	@Autowired
	private ICommisionDetailService commisionDetailService;

	@Override
	public BaseDao<SubOrder> getDao() {
		return subOrderDao;
	}
	
	@Override
	public List<SubOrderExpressInfoDTO> queryExpressLogInfo(String code, String packageNo) {
		Assert.notNull(code, "子订单编号不可为空");
		List<Kuaidi100Express> expressLogInfoList = null;
		Map<String, OrderDelivery> orderDeliveryMap = new HashMap<String, OrderDelivery>();
		Map<String, RejectInfo> rejectInfoMap = new HashMap<String, RejectInfo>();
		List<SubOrderExpressInfoDTO> list = new ArrayList<SubOrderExpressInfoDTO>();
		Map<String,Object> params = new HashMap<String,Object>();
		if (code.startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) {// 子订单
			params.put("subOrderCode", code);
			params.put("packageNo", packageNo);
			List<OrderDelivery> orderDeliveryList = orderDelvieryDao.queryByParam(params);
			if (CollectionUtils.isNotEmpty(orderDeliveryList)) {
				List<Kuaidi100Express> kuaidi100ExpressList = new ArrayList<Kuaidi100Express>();
				Kuaidi100Express kuaidi100Express = null;
				for (OrderDelivery orderDelivery : orderDeliveryList) {
					orderDeliveryMap.put(orderDelivery.getPackageNo(), orderDelivery);// 构建订单物流信息map
					kuaidi100Express = new Kuaidi100Express();
					kuaidi100Express.setOrderCode(orderDelivery.getOrderCode());
					kuaidi100Express.setPackageNo(orderDelivery.getPackageNo());
					kuaidi100ExpressList.add(kuaidi100Express);// 组织物流日志记录查询参数列表O
				}
				expressLogInfoList = kuaidi100ExpressDao.queryByParam(params);
			}
		} else if (code.startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {// 退货
			params.put("orderNo", code);
			params.put("expressNo", packageNo);
			List<RejectInfo> rejectInfoList = rejectInfoService.queryByParam(params);
			if (CollectionUtils.isNotEmpty(rejectInfoList)) {
				List<Kuaidi100Express> kuaidi100ExpressList = new ArrayList<Kuaidi100Express>();
				Kuaidi100Express kuaidi100Express = null;
				for (RejectInfo rejectInfo : rejectInfoList) {
					rejectInfoMap.put(rejectInfo.getExpressNo(), rejectInfo);// 构建订单物流信息map
					kuaidi100Express = new Kuaidi100Express();
					kuaidi100Express.setRejectCode(rejectInfo.getRejectCode());// 退货单号
					kuaidi100Express.setPackageNo(rejectInfo.getExpressNo());
					kuaidi100ExpressList.add(kuaidi100Express);// 组织物流日志记录查询参数列表O
				}
				params.remove("expressNo");
				params.put("packageNo", packageNo);
				expressLogInfoList = kuaidi100ExpressDao.queryByParam(params);
			}
		}
		if (CollectionUtils.isNotEmpty(expressLogInfoList)) {
			// 组织具体的日志信息
			Map<String, ArrayList<ExpressLogInfoDTO>> expressLogInfoMap = new HashMap<String, ArrayList<ExpressLogInfoDTO>>();
			for (Kuaidi100Express tmpKuaidi100Express : expressLogInfoList) {
				ArrayList<ExpressLogInfoDTO> list2 = expressLogInfoMap.get(tmpKuaidi100Express.getPackageNo());
				if (CollectionUtils.isEmpty(list2)) {
					list2 = new ArrayList<ExpressLogInfoDTO>();
				}
				ExpressLogInfoDTO eliDTO = new ExpressLogInfoDTO();
				String dataTime = StringUtils.isNotBlank(tmpKuaidi100Express.getDataFtime()) ? tmpKuaidi100Express.getDataFtime() : tmpKuaidi100Express
						.getDataTime();
				eliDTO.setDataTime(dataTime);
				eliDTO.setContext(tmpKuaidi100Express.getDataContext());
				list2.add(eliDTO);
				expressLogInfoMap.put(tmpKuaidi100Express.getPackageNo(), list2);
			}

			if (MapUtils.isNotEmpty(orderDeliveryMap)) {
				SubOrderExpressInfoDTO subOrderExpressInfoDTO = null;
				for (Map.Entry<String, OrderDelivery> entry : orderDeliveryMap.entrySet()) {
					subOrderExpressInfoDTO = new SubOrderExpressInfoDTO();
					OrderDelivery orderDelivery = entry.getValue();
					subOrderExpressInfoDTO.setExpressLogInfoDTOList(expressLogInfoMap.get(entry.getKey()));
					subOrderExpressInfoDTO.setCompanyId(orderDelivery.getCompanyId());
					subOrderExpressInfoDTO.setCompanyName(orderDelivery.getCompanyName());
					subOrderExpressInfoDTO.setPackageNo(entry.getKey());
					subOrderExpressInfoDTO.setSubOrderCode(orderDelivery.getOrderCode());
					list.add(subOrderExpressInfoDTO);
				}
			} else if (MapUtils.isNotEmpty(rejectInfoMap)) {
				SubOrderExpressInfoDTO subOrderExpressInfoDTO = null;
				for (Map.Entry<String, RejectInfo> entry : rejectInfoMap.entrySet()) {
					subOrderExpressInfoDTO = new SubOrderExpressInfoDTO();
					RejectInfo rejectInfo = entry.getValue();
					subOrderExpressInfoDTO.setExpressLogInfoDTOList(expressLogInfoMap.get(entry.getKey()));
					subOrderExpressInfoDTO.setCompanyId(rejectInfo.getCompanyCode());
					subOrderExpressInfoDTO.setCompanyName(rejectInfo.getCompanyName());
					subOrderExpressInfoDTO.setPackageNo(entry.getKey());
					subOrderExpressInfoDTO.setSubOrderCode(rejectInfo.getOrderCode());
					subOrderExpressInfoDTO.setRejectNo(rejectInfo.getRejectCode());
					list.add(subOrderExpressInfoDTO);
				}
			}

		}
		return list;
	}

	@Override
	public SubOrder findSubOrderByCode(Long code) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", code);
		return super.queryUniqueByParams(params);
	}
	@Override
	public PageInfo<SubOrder> selectPageByQO(SubOrderQO query) {
		int totalCount = subOrderDao.selectCountByQO(query).intValue();
		List<SubOrder> doList = subOrderDao.selectPageByQO(query);

		PageInfo<SubOrder> page = new PageInfo<SubOrder>();
		page.setRows(doList);
		page.setPage(query.getStartPage());
		page.setSize(query.getPageSize());
		page.setRecords(totalCount);
		return page;
	}
	
	@Override
	public PageInfo<SubOrder> selectPageByQO_coupons_shop_scan(SubOrderQO query){
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		CommisionDetail detail = new CommisionDetail();
		detail.setCreateBeginTime(query.getCreateBeginTime());
		detail.setCreateEndTime(query.getCreateEndTime());
//		detail.setIndirect(INDIRECT_TYPE.NO.code);
//		detail.setBizType(BUSSINESS_TYPE.ORDER.code);
//		detail.setOperateType(ACCOUNT_TYPE.INCOMING.code);		
		if( query.getShopPromoterId() != null){
			params.put("shopPromoterId", query.getShopPromoterId());			
			detail.setPromoterId( query.getShopPromoterId() );
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
		}else if( query.getPromoterId() != null){
			params.put("promoterId", query.getPromoterId());						
			detail.setPromoterId( query.getPromoterId() );
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.COUPON.code);
		}else if(query.getScanPromoterId() != null){
			//获取所有子分销员ID
			PromoterInfo currentPromoterInfo =new PromoterInfo();
			currentPromoterInfo.setPromoterId(query.getScanPromoterId());//分销员ID
			currentPromoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);//分销类型
			currentPromoterInfo=promoterInfoService.queryUniqueByObject(currentPromoterInfo);
			List<Long> allScanPromoterIds=new ArrayList<Long>();
			allScanPromoterIds.add(query.getScanPromoterId());
			if(currentPromoterInfo.getTopPromoterId()==0){//是顶级扫码分销员 查询子分销员的所有订单
				PromoterInfo promoterInfo =new PromoterInfo();
				promoterInfo.setParentPromoterId(query.getScanPromoterId());//分销员ID
				promoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);//分销类型
				List<PromoterInfo> promoterInfoList=promoterInfoService.getAllChildPromoterInfo(promoterInfo);
				for(PromoterInfo childromoterInfo :promoterInfoList){
					allScanPromoterIds.add(childromoterInfo.getPromoterId());
				}
			}
			
//			params.put("scanPromoterId", query.getScanPromoterId());									
//			detail.setPromoterId( query.getScanPromoterId() );
			detail.setAllchildPromoterids(allScanPromoterIds);
			params.put("scanPromoterId", allScanPromoterIds);
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		}
		 List<Long> ordercodeList = commisionDetailService.queryAllCildPromotersDistinctOrderCode(detail);
		 if(ordercodeList == null  ){
			 ordercodeList = new ArrayList<Long>();
			 ordercodeList.add(0L);
		 }else if( ordercodeList.isEmpty() ){
			 ordercodeList.add(0L);			 
		 }
		 params.put("orderCodeList", ordercodeList);		 
		if(query.getOrderStatus() != null){
			params.put("orderStatus", query.getOrderStatus());												
		}
		params.put("start",  query.getStart());												
		params.put("pageSize", query.getPageSize());												
		params.put("createBeginTime", query.getCreateBeginTime());	
		params.put("createEndTime", query.getCreateEndTime());	
		
		int totalCount = subOrderDao.selectCountByQO_coupons_shop_scan(params).intValue();
		List<SubOrder> doList = subOrderDao.selectPageByQO_coupons_shop_scan(params);
        
		params.put("pageSize", totalCount);
		params.put("start", 0);
		List<SubOrder> allOrderList = subOrderDao.selectPageByQO_coupons_shop_scan(params);
		Double totalMoney=0D;
		for(SubOrder subOrder:allOrderList){
			if(subOrder.getOrderStatus()>2){
				totalMoney=totalMoney+subOrder.getTotal()+subOrder.getPoints()/100;
			}
			
		}
		
		PageInfo<SubOrder> page = new PageInfo<SubOrder>();
		page.setRows(doList);
		page.setTotalMoney(totalMoney);//订单总金额
		page.setPage(query.getStartPage());
		page.setSize(query.getPageSize());
		page.setRecords(totalCount);
		return page;
	}

	@Override
	public SubOrder selectOneByCode(Long code) {
		return findSubOrderByCode(code);
	}

	@Override
	public Integer updateSubOrderStatus(SubOrder subOrder) {
		return subOrderDao.updateSubOrderStatus(subOrder);
	}

	@Override
	public Integer getTotalCustomersForPormoter(SubOrderQO query){
		return subOrderDao.getTotalCustomersForPormoter(query);
	}

	@Override
	public Double getShopTotalSales(Long promoterId){
		return subOrderDao.getShopTotalSales(promoterId);
	}

	
	@Override
	public Double getShopTotalSales_coupons_shop_scan(Map<String, Object> params){
		return subOrderDao.getShopTotalSales_coupons_shop_scan(params);
	}

	@Override
	public List<SubOrder> selectListByOrderIdList(List<Long> orderIdList) {
		if (CollectionUtils.isNotEmpty(orderIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " parent_order_id in ("+StringUtil.join(orderIdList, Constant.SPLIT_SIGN.COMMA)+")");
			return subOrderDao.queryByParam(params);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Integer deleteByCode(Long orderCode, Long memberId) {
		SubOrder sub = new SubOrder();
		sub.setOrderCode(orderCode);
		sub.setMemberId(memberId);
		sub.setDeleted(OrderIsDeleted.YES.code);
		return subOrderDao.deleteByCode(sub);
	}

	@Override
	public List<SubOrder> selectListByCodeList(List<Long> subCodeList) {
		if (CollectionUtils.isNotEmpty(subCodeList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_code in ("+StringUtil.join(subCodeList, Constant.SPLIT_SIGN.COMMA)+")");
			return subOrderDao.queryByParam(params);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<SubOrder> selectListByIdList(List<Long> idList) {
		if (CollectionUtils.isNotEmpty(idList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(idList, Constant.SPLIT_SIGN.COMMA)+")");
			return subOrderDao.queryByParam(params);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Integer batchUpdateSubOrderStatusByCode(Map<String, Object> map) {
		return subOrderDao.batchUpdateSubOrderStatusByCode(map);
	}
	
	@Override
	public List<SubOrder> querySubOrderByWaitPutSeaWashes(Map<String, Object> map) {
		return subOrderDao.querySubOrderByWaitPutSeaWashes(map);
	}

	@Override
	public List<SubOrder> selectListByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		return super.queryByParam(params);
	}

	@Override
	public List<SubOrder> querySubOrderBySeaOrderUnPayIsExpired(int minute) {
		return subOrderDao.querySubOrderBySeaOrderUnPayIsExpired(minute);
	}
	
	@Override
	public List<SubOrder> queryUndeclaredSubOrders(Map<String, Object> map){
		return subOrderDao.queryUndeclaredSubOrders(map);
	}
	
	@Override
	public List<SubOrder> queryUnPutWaybillSubOrders(Map<String, Object> map){
		return subOrderDao.queryUnPutWaybillSubOrders(map);
	}
	
	@Override
	public List<SubOrder> querySubOrderToFisherAfterPayThirtyMinutes(Map<String, Object>  inputArgument) {
		return subOrderDao.querySubOrderToFisherAfterPayThirtyMinutes(inputArgument);
	}

	

	@Override
	public Integer selectPaymentCount(Long memberId,List<Integer> orderTypeList,String channelCode) {
		return subOrderDao.selectPaymentCount(memberId,orderTypeList,channelCode);
	}

	@Override
	public Integer selectCountDynamic(SubOrder sub) {
		return subOrderDao.queryByObjectCount(sub).intValue();
	}

    @Override
    public List<SubOrder> selectByCodeAndMemberID(Long subOrderCode, Long memberId) {
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", subOrderCode);
		params.put("memberId", memberId);
        List<SubOrder> doList = subOrderDao.queryByParam(params);
        return doList;
    }	
	@Override
	public boolean orderCancelCheck(Long subOrderCode, Long memberId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode",subOrderCode);
		params.put("memberId",memberId);
		List<SubOrder> subOrderList = subOrderDao.queryByParam(params);
		if(CollectionUtils.isEmpty(subOrderList)){
			logger.info("根据订单编号={} 用户ID={} 没有找到订单信息",new Object[]{subOrderCode,memberId});
			return Boolean.FALSE;
		}
		if(ORDER_STATUS.DELIVERY.code.intValue()!=subOrderList.get(0).getOrderStatus()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public void operateAfterCancel(Long subOrderCode,Long memberId,String userName) {
		// 更新子订单信息
		SubOrder subOrder = new SubOrder();
		subOrder.setOrderCode(subOrderCode);
		subOrder.setOrderStatus(ORDER_STATUS.CANCEL.code);
		subOrder.setUpdateTime(new Date());
		subOrderDao.updateSubOrderStatus(subOrder);
		
		// 更新父订单信息
		Long parentOrderCode = subOrderDao.queryOrderCodeBySubOrderCodeAndCancelStatus(subOrderCode);
		if(null!=parentOrderCode){
			OrderInfo salesOrderForUpdate = new OrderInfo();
			salesOrderForUpdate.setParentOrderCode(parentOrderCode);
			salesOrderForUpdate.setOrderStatus(ORDER_STATUS.CANCEL.code);
			salesOrderForUpdate.setUpdateTime(new Date());
			orderInfoDao.updateSalesOrderStatusAfterCancel(salesOrderForUpdate);
		}
		
		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(parentOrderCode);
		orderStatusLog.setOrderCode(subOrderCode);
		orderStatusLog.setPreStatus(ORDER_STATUS.DELIVERY.code);
		orderStatusLog.setCurrStatus(ORDER_STATUS.CANCEL.code);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(memberId);
		orderStatusLog.setCreateUserName(userName);
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.MEMBER.code);
		orderStatusLogDao.insert(orderStatusLog);
	}

	@Override
	public void updateOrderPutStatus(Long subOrderCode, Boolean isSuccess,String message) {
		Integer putStatus = isSuccess?PutStatus.VERIFY_SUCCUESS.code:PutStatus.VERIFY_FAIL.code;
		SubOrder subOrder = subOrderDao.selectOneByCode(subOrderCode);
		if(null==subOrder){
			logger.error("code={} not find data!",subOrderCode);
			return;
		}
		subOrder.setPutStatus(putStatus);
		subOrder.setUpdateTime(new Date());
		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrderCode);
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName("系统回调消息");
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.SYSTEM.code);
		orderStatusLog.setContent("海淘审核返回信息："+message);
		subOrderDao.updateNotNullById(subOrder);
		orderStatusLogDao.insert(orderStatusLog);
	}

	@Override
	public Map<String, Number> queryOrderCountByPromoterId(Map<String, Object> params) {
		return subOrderDao.queryOrderCountByPromoterId(params);
	}
	
	@Override
	public List<OrderItemCMBC> selectOrderItemsForPushCMBC(Map<String, Object> params){
		return subOrderDao.selectOrderItemsForPushCMBC(params);
	}
	
	@Override
	public List<SubOrder> selectSubOrderForPushCMBC(Map<String, Object> params){
		List<SubOrder> sublist = subOrderDao.selectSubOrderForPushCMBC(params);		
		return sublist;
	}
	
}
