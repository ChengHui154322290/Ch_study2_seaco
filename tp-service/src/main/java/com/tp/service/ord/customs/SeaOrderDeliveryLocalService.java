/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsInfoDeliveryService;
import com.tp.service.ord.customs.ISeaOrderDeliveryLocalService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

/**
 * @author Administrator
 * 订单清关推送处理(JOB调用)
 */
@Service
public class SeaOrderDeliveryLocalService implements ISeaOrderDeliveryLocalService {

	private static final Logger logger = LoggerFactory.getLogger(SeaOrderDeliveryLocalService.class);
	
	/** 海淘订单推送海关 **/
	private static Boolean IS_DEALING = false;
	
	/** 支付多久后订单或者个人物品申报单推送海关 **/
	public static final int UNDECLARED_AFTER_PAYED_MINUTE_DEFAULT = 60;
	
	/** 推送单单页数量 **/
	public static final int PAGE_SIZE = 50;
	
	public static final String DELIVERY_LOCK_KEY_PRE = "clearance_delivery_lock_";
	
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService; 
	
	@Autowired
	private ISubOrderService subOrderService;

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private List<ISeaCustomsInfoDeliveryService> seaDeliveryServices;
	
	private  static PutCustomsType[] deliveryCustomsSequence = new PutCustomsType[]{
			PutCustomsType.PAY_DECLARE,
			PutCustomsType.ORDER_DECLARE,
			PutCustomsType.PERSONALGOODS_DECLARE,
			PutCustomsType.WAYBILL_DECLARE
		};
	//推送海淘订单相关数据至海关
	@Override
	public void declareSeaOrderToCustoms(){
		if (!IS_DEALING) {
			// 推送订单至海关
			try {
				boolean b1 = false;
				int page = 1;
				do {
					b1 = pushSeaOrderInfoToCustoms(page);
					page++;
				} while (b1);
			} catch (Exception e) {
				logger.error("报关数据 - 处理推送订单相关数据至海关数据异常", e);
			}
			IS_DEALING = false;
		}	
	}
	
	// Manual Push
	@Override
	public ResultInfo<Boolean> declareSeaOrderToCustoms(SubOrder subOrder){
		if (null == subOrder) {
			return new ResultInfo<>(new FailInfo("订单数据为空"));
		}
		ResultInfo<Boolean> checkResult = checkManualPushCustomsInfo(subOrder);
		if(!checkResult.isSuccess()) return checkResult;
		return deliverySingleSubOrder(subOrder);
	}
	/*---------------------------------------------------------------------------------------------*/
	/*---------------------------------------------------------------------------------------------*/	
	//推送海关(JOB push)
	public boolean pushSeaOrderInfoToCustoms(int page){
    	Map<String, Object> map = new HashMap<>();
    	map.put("minute", UNDECLARED_AFTER_PAYED_MINUTE_DEFAULT);
    	map.put("start", (page - 1)*PAGE_SIZE);
    	map.put("pagesize", PAGE_SIZE);
		List<SubOrder> subOrders = salesOrderRemoteService.queryUndeclaredSubOrders(map);
		if (CollectionUtils.isEmpty(subOrders)) {
			logger.error("申报海关数据 - 订单数据为空");
			return false;
		}
		for (SubOrder subOrder : subOrders) {
			ResultInfo<Boolean> checkResult = checkAutoPushCustomsInfo(subOrder);
			if(!checkResult.isSuccess()) continue;
			deliverySingleSubOrder(subOrder);
		}
		return true;
	}
	
	
	public ResultInfo<Boolean> deliverySingleSubOrder(SubOrder subOrder){	
		String lockKey = DELIVERY_LOCK_KEY_PRE + subOrder.getOrderCode();
		if (!jedisCacheUtil.lock(lockKey)) { //加锁
			logger.error("[deliverySingleSubOrder]请求加锁失败：[lock_key={}]", lockKey);
			return new ResultInfo<>(new FailInfo("正在处理订单报关请求..."));
		}
		try{
			return deliverSingleSubOrder(subOrder);
		}catch(Exception e){
			logger.error("推送报关异常", e);
		}finally{
			jedisCacheUtil.unLock(lockKey); //解锁
			logger.info("[deliverySingleSubOrder]解锁成功");	
		}
		return new ResultInfo<>(new FailInfo("推送失败"));
	}
	
	/**
	 * 推送支付单，订单，清关单，运单至通关平台（按次序及是否推送的标示）
	 * 1. 推送支付单,对于合并支付的订单推送成功后获取对应子单的支付流水
	 * 2. 推送订单
	 * 3. 推送清关单
	 * 4. 推送运单
	 * 
	 * 任何一步推送失败都不再继续推送
	 * 对于清单需要申报的订单，优先生成清关单
	 * 直邮报关，先报订单和支付单，等待填写航班号和提运单之后再推送海关
	 */
	public ResultInfo<Boolean> deliverSingleSubOrder(SubOrder subOrder) throws Exception{
		for(PutCustomsType type : deliveryCustomsSequence){
			ISeaCustomsInfoDeliveryService service = getCustomsDeliveryServiceByPutCustomsType(type);
			if (service == null || false == service.checkDelivery(subOrder)) continue;
			
			ResultInfo<Boolean> prepareResult = service.prepareDelivery(subOrder);
			if (!prepareResult.isSuccess()) return prepareResult;
			
			ResultInfo<Boolean> pushResult = service.delivery(subOrder);
			if (Boolean.FALSE == pushResult.isSuccess()){
				logger.error("[DECLARE_CUSTOMS][{}]申报海关 - 推送失败", subOrder.getOrderCode());
				updatePushSingleSubOrderStatus(subOrder, false); //更新状态
				return pushResult;
			}
		}
		updatePushSingleSubOrderStatus(subOrder, true);
		return new ResultInfo<>(Boolean.TRUE);
	}
		
	private ISeaCustomsInfoDeliveryService getCustomsDeliveryServiceByPutCustomsType(PutCustomsType type){
		for(ISeaCustomsInfoDeliveryService service : seaDeliveryServices){
			if (service.checkPutCustomsType(type)) return service;
		}
		return null;
	}
	private ResultInfo<Boolean> checkManualPushCustomsInfo(SubOrder subOrder){
		return checkPushCustomsInfo(subOrder);
	}
	private ResultInfo<Boolean> checkAutoPushCustomsInfo(SubOrder subOrder){
		//非待推送状态的订单不予推送
		if (!ClearanceStatus.isNew(subOrder.getClearanceStatus())) {
			logger.error("[{}]订单报关状态不是等待推送状态", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单报关状态不是等待推送状态, 请重置后重新推送"));
		}		
		return checkPushCustomsInfo(subOrder);
	}
	private ResultInfo<Boolean> checkPushCustomsInfo(SubOrder subOrder){
		/*-----------------------------------基本信息校验-------------------------------------*/
		if (OrderConstant.ORDER_STATUS.DELIVERY.code != subOrder.getOrderStatus()) {
			logger.error("[{}]订单不是待发货状态，不需要推送海关", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单不是待发货状态，不需要推送海关"));
		}
		Date payLowTime = new Date(subOrder.getPayTime().getTime() + UNDECLARED_AFTER_PAYED_MINUTE_DEFAULT*60*1000);
		if (payLowTime.after(new Date())) {
			logger.error("[{}]订单支付" + UNDECLARED_AFTER_PAYED_MINUTE_DEFAULT + "分钟后才能推送海关", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单支付一定时间后才能推送海关"));
		}
		if (ClearanceStatus.PUT_SUCCESS.code.equals(subOrder.getClearanceStatus())) {
			logger.error("[{}]已推送成功，不需要重复推送", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单已推送成功,不需要重复推送"));
		}
		if (ClearanceStatus.AUDIT_SUCCESS.code.equals(subOrder.getClearanceStatus())) {
			logger.error("[{}]已审核通过，不允许重复推送", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单已审核通过,不允许重复推送"));
		}
		return new ResultInfo<>(Boolean.TRUE);		
	}
	//更新单个订单数据海关推送状态
	private void updatePushSingleSubOrderStatus(SubOrder subOrder, boolean success){
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		if (success) {
			so.setClearanceStatus(ClearanceStatus.PUT_SUCCESS.code);
		}else{
			so.setClearanceStatus(ClearanceStatus.PUT_FAILED.code);
		}
		so.setPutCustomsTimes(subOrder.getPutCustomsTimes() + 1);
		subOrderService.updateNotNullById(so);
	}
}
