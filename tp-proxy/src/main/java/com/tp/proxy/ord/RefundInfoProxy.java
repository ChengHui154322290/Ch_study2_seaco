package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.ord.CancelItem;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.RefundLog;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.pay.PayServiceProxy;
import com.tp.query.ord.RefundQuery;
import com.tp.service.IBaseService;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.IRefundLogService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IRefundPayinfoService;
import com.tp.util.BeanUtil;
/**
 * 退款单代理层
 * @author szy
 *
 */
@Service
public class RefundInfoProxy extends BaseProxy<RefundInfo>{

	@Autowired
	private IRefundInfoService refundInfoService;
	@Autowired
	private IRefundLogService refundLogService;
	
	@Autowired
	private PayServiceProxy payServiceProxy;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;
	@Autowired
	private IRefundPayinfoService refundPayinfoService;
	@Autowired
	private CancelInfoProxy cancelProxy;
	@Autowired
	private RejectInfoProxy rejectInfoProxy;
	
	@Autowired
	private SubOrderProxy subOrderProxy;

	@Override
	public IBaseService<RefundInfo> getService() {
		return refundInfoService;
	}
	
	
	public PageInfo<RefundInfo> queryByQuery(RefundQuery refundQuery) {
		Map<String,Object> params = BeanUtil.beanMap(refundQuery);
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(null!=refundQuery.getRefundAmountEnd()){
			whereList.add(new WHERE_ENTRY("offset_amount", MYBATIS_SPECIAL_STRING.GT, refundQuery.getRefundAmountStart()));
		}
		params.remove("offsetAmountStart");
		if(null!=params.get("offsetAmountEnd")){
			whereList.add(new WHERE_ENTRY("offset_amount", MYBATIS_SPECIAL_STRING.LT, refundQuery.getRefundAmountEnd()));
		}
		params.remove("offsetAmountEnd");
		if(null!=refundQuery.getCreateTimeBegin()){
			whereList.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, refundQuery.getCreateTimeBegin()));
		}
		params.remove("createTimeBegin");
		if(null!=refundQuery.getCreateTimeEnd()){
			whereList.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, refundQuery.getCreateTimeEnd()));
		}
		params.remove("createTimeEnd");
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		PageInfo<RefundInfo> pageInfo = new PageInfo<RefundInfo>(refundQuery.getStartPage(),refundQuery.getPageSize());
		pageInfo = refundInfoService.queryPageByParam(params, pageInfo);
		if(pageInfo!=null){
			List<RefundInfo> refundInfoList = pageInfo.getRows();
			if(CollectionUtils.isNotEmpty(refundInfoList)){
				Map<Long,List<RefundInfo>> cancelRefundCode =new HashMap<Long,List<RefundInfo>>();
				Map<Long,List<RefundInfo>> rejectRefundCode =new HashMap<Long,List<RefundInfo>>();
				List<Long> cancelRefundCodeList = new ArrayList<Long>();
				List<Long> rejectRefundCodeList = new ArrayList<Long>();
				for(RefundInfo refundInfo:refundInfoList){
					if(RefundConstant.REFUND_TYPE.CANCEL.code.intValue()==refundInfo.getRefundType()){
						if(cancelRefundCode.get(refundInfo.getOrderCode()) == null){
							List<RefundInfo> newList = new ArrayList<RefundInfo>();
							newList.add(refundInfo);
							cancelRefundCode.put(refundInfo.getOrderCode(),newList);
						}
						else{
							cancelRefundCode.get(refundInfo.getOrderCode()).add(refundInfo);
						}
						cancelRefundCodeList.add(refundInfo.getRefundCode());
					}else if(RefundConstant.REFUND_TYPE.REJECT.code.intValue()==refundInfo.getRefundType()){
						if(rejectRefundCode.get(refundInfo.getOrderCode()) == null){
							List<RefundInfo> newList = new ArrayList<RefundInfo>();
							newList.add(refundInfo);
							rejectRefundCode.put(refundInfo.getOrderCode(),newList);
						}
						else{
							rejectRefundCode.get(refundInfo.getOrderCode()).add(refundInfo);
						}
						rejectRefundCodeList.add(refundInfo.getRefundCode());
					}
				}
				
				if(CollectionUtils.isNotEmpty(cancelRefundCode.keySet())){
					List<CancelItem> cancelItemList = cancelProxy.queryRejectItemListByRefundNoList(cancelRefundCodeList);
					if(CollectionUtils.isNotEmpty(cancelItemList)){
						for(CancelItem item:cancelItemList){
							if(!CollectionUtils.isEmpty(cancelRefundCode.get(item.getOrderCode()))){
								for(RefundInfo refundInfo : cancelRefundCode.get(item.getOrderCode())){
									List itemList = refundInfo.getItemList();
									itemList.add(item);
								}
							}
						}
					}
				}
				if(CollectionUtils.isNotEmpty(rejectRefundCode.keySet())){
					List<RejectItem> rejectItemList = rejectInfoProxy.queryRejectItemListByRefundNoList(rejectRefundCodeList);
					if(CollectionUtils.isNotEmpty(rejectItemList)){
						
						for(RejectItem item:rejectItemList){
							if(!CollectionUtils.isEmpty(rejectRefundCode.get(item.getOrderCode()))){
								for(RefundInfo refundInfo : rejectRefundCode.get(item.getOrderCode())){
									List itemList = refundInfo.getItemList();
									itemList.add(item);
								}
							}
						}
					}
				}
				
				
			}
		
		}
		return pageInfo;
	}

	public ResultInfo<?> audit(Long refundNo, String userName, Boolean success, Boolean bAuditComplete) {
		RefundInfo oldRefund = refundInfoService.selectByRefundNo(refundNo);
		
		ResultInfo<?> returnData = validate(oldRefund);
		if(returnData != null)
			return returnData;
		
		SubOrder order = subOrderProxy.findSubOrderByCode(oldRefund.getOrderCode());
		Long orderNo = order == null ? oldRefund.getOrderCode() : order.getOrderCode();
		PaymentInfo paymentInfo = payServiceProxy.queryPaymentInfoByBizCode(orderNo.toString());
		
		if(paymentInfo ==null){
		    paymentInfo = payServiceProxy.queryPaymentInfoByBizCode(oldRefund.getOrderCode().toString());
		}
		if(paymentInfo == null && order != null){
			paymentInfo = payServiceProxy.queryPaymentInfoByBizCode(order.getParentOrderCode().toString());
		}
		if(paymentInfo == null){
			throw new ServiceException("找不到"+orderNo+"对应的付款信息");
		}
		if(RefundConstant.REFUND_STATUS.APPLY.code.intValue() == oldRefund.getRefundStatus().intValue()){
			RefundInfo refundInfoDO = new RefundInfo();
			refundInfoDO.setRefundId(oldRefund.getRefundId());
			if(success)
				refundInfoDO.setRefundStatus(RefundConstant.REFUND_STATUS.AUDITING.code);
			else
				refundInfoDO.setRefundStatus(RefundConstant.REFUND_STATUS.CANCEL.code);
			refundInfoDO.setUpdateUser(userName);
			refundInfoDO.setUpdateTime(new Date());
			refundInfoService.updateNotNullById(refundInfoDO);
			
			RefundLog refundLogDO = new RefundLog();
			refundLogDO.setRefundCode(oldRefund.getRefundCode());
			refundLogDO.setOrderCode(oldRefund.getOrderCode());
			refundLogDO.setActionType(RefundConstant.REFUND_LOG_TYPE.AUDIT.code);
			refundLogDO.setOldRefundStatus(oldRefund.getRefundStatus());
			refundLogDO.setCurrentRefundStatus(refundInfoDO.getRefundStatus());
			if(success)
				refundLogDO.setLogContent("财务审核通过，退款金额等待原路返回");
			
			else
				refundLogDO.setLogContent("退款单被取消");
			refundLogDO.setOperatorName(userName);
			refundLogDO.setOperatorType(LogTypeConstant.LOG_TYPE.USER.code);
			refundLogDO.setCreateTime(new Date());
			refundLogService.insert(refundLogDO);
			if(!success){
				if(RefundConstant.REFUND_TYPE.REJECT.code.equals(oldRefund.getRefundType()))
					rejectInfoProxy.refundResult(refundNo, success);
			}else if(bAuditComplete){	// 直接退货成功，应用于不能退款的情况
				
				
				operateAfterRefund(refundNo, success);
				return new ResultInfo<>("成功");
			}
		}
		else if(RefundConstant.REFUND_STATUS.CANCEL.code.intValue() != oldRefund.getRefundStatus().intValue()){
			RefundInfo refundInfoDO = new RefundInfo();
			refundInfoDO.setRefundId(oldRefund.getRefundId());
			refundInfoDO.setUpdateUser(userName);
			refundInfoDO.setUpdateTime(new Date());
			if(success)
				refundInfoDO.setRefundStatus(RefundConstant.REFUND_STATUS.AUDITING.code);
			else
				refundInfoDO.setRefundStatus(RefundConstant.REFUND_STATUS.CANCEL.code);
			updateNotNullById(refundInfoDO);
			
			RefundLog refundLog = new RefundLog();
			refundLog.setRefundCode(oldRefund.getRefundCode());
			refundLog.setOrderCode(oldRefund.getOrderCode());
			refundLog.setActionType(RefundConstant.REFUND_LOG_TYPE.AUDIT.code);
			refundLog.setOldRefundStatus(oldRefund.getRefundStatus());
			refundLog.setCurrentRefundStatus(refundInfoDO.getRefundStatus());
			refundLog.setLogContent("财务审核通过，退款金额等待原路返回");
			refundLog.setOperatorName(userName);
            refundLog.setOperatorType(LogTypeConstant.LOG_TYPE.USER.code);
            refundLog.setCreateTime(new Date());
			refundLogService.insert(refundLog);
			
			if(bAuditComplete){	// 直接退货成功，应用于不能退款的情况
				operateAfterRefund(refundNo, success);
				return new ResultInfo<>("成功");
			}
		}
		if(!success)
			return new ResultInfo<>(new FailInfo("退款失败"));
		
		RefundPayinfo refundPayinfoDO = refundPayinfoService.selectByRefundNo(oldRefund.getRefundCode());
		if(refundPayinfoDO == null){
			refundPayinfoDO = new RefundPayinfo();
			refundPayinfoDO.setPaymentId(paymentInfo.getPaymentId());
			refundPayinfoDO.setBizCode(oldRefund.getRefundCode());
			refundPayinfoDO.setBizType(paymentInfo.getBizType());
			refundPayinfoDO.setRefundType(1);
			refundPayinfoDO.setAmount(oldRefund.getRefundAmount());
			refundPayinfoDO.setGatewayId(paymentInfo.getGatewayId());
			refundPayinfoDO.setStatus(1);
			refundPayinfoDO.setGatewayTradeNo(paymentInfo.getGatewayTradeNo());
			refundPayinfoDO.setCreateUser(userName);
			refundPayinfoDO.setUpdateUser(userName);
			refundPayinfoDO.setCreateTime(new Date());
			refundPayinfoDO.setUpdateTime(new Date());
			refundPayinfoService.insert(refundPayinfoDO);
		}
		Long gatewayId = oldRefund.getGatewayId();
		if(gatewayId == null){
			gatewayId = paymentInfo.getGatewayId();
		}
		PaymentGateway paymentGatewayDO = paymentGatewayService.queryById(gatewayId);
		if(paymentGatewayDO == null){
			throw new ServiceException("没有找到对应的网关");
		}
		String msg = payServiceProxy.refund(paymentGatewayDO.getGatewayCode(), refundNo);
		logger.info("refund{},result:{}", refundNo, msg);
		
		return new ResultInfo<>("成功");
	}

	public void operateAfterRefund(Long refundNo, Boolean isSuccess) {
		refundInfoService.operateAfterRefund(refundNo, isSuccess);
	}
	
	public ResultInfo<?> validate(RefundInfo refundInfo){
		ResultInfo<Boolean> returnData = null;
		if(RefundConstant.REFUND_STATUS.AUDITED.code.intValue() == refundInfo.getRefundStatus().intValue()){
			returnData =new ResultInfo<>(new FailInfo("订单"+refundInfo.getOrderCode()+"已"+RefundConstant.REFUND_STATUS.getCnName(refundInfo.getRefundStatus())));
		}
		return returnData;
	}


	public List<RefundLog> findRefundLogByRefundCode(Long refundCode) {
		RefundLog query = new RefundLog();
		query.setRefundCode(refundCode);
		Map<String, Object> params = BeanUtil.beanMap(query);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " log_id desc ");
		return refundLogService.queryByParamNotEmpty(params);
	}
}
