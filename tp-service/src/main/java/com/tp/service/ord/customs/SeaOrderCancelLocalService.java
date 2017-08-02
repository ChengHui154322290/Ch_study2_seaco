/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant.CancelCustomsOrderStatus;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CancelCustomsInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.service.ord.ICancelCustomsInfoService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.customs.ISeaOrderCancelLocalService;
import com.tp.service.ord.customs.JKF.IJKFCancelOrderService;

/**
 * @author Administrator
 * 只有清关通过的单子才可以使用此接口申报删单（暂时作废，不再使用）
 */
@Service
public class SeaOrderCancelLocalService implements ISeaOrderCancelLocalService{

	private static final Logger logger = LoggerFactory.getLogger(SeaOrderCancelLocalService.class);
	
	@Autowired
	private IJKFCancelOrderService jKFCancelOrderService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Autowired
	private ICancelCustomsInfoService cancelCustomsInfoService;
	
	/**
	 * 取消海淘订单
	 */
	@Override
	public ResultInfo<Boolean> cancelSeaOrder(SubOrder subOrder, String cancelReason) {	
		//取消单
		CancelCustomsInfo cancelCustomsInfo = cancelCustomsInfoService.selectByOrderCode(subOrder.getOrderCode());
		if (null != cancelCustomsInfo) {
			if (CancelCustomsOrderStatus.AUDIT_SUCCESS.code.equals(cancelCustomsInfo.getCancelStatus())) {
				return new ResultInfo<>(new FailInfo("订单已在海关删单成功,不需重复申报"));
			}
		}
		FailInfo failInfo = cancelOrderDeclare(subOrder, cancelReason);
		if (failInfo != null) {
			return new ResultInfo<>(failInfo);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	/** 取消申报 */
	private FailInfo cancelOrderDeclare(SubOrder subOrder, String cancelReason){
		if (ClearanceChannelsEnum.HANGZHOU.id.equals(subOrder.getSeaChannel())) {
			ResultInfo<Boolean> resultInfo = jKFCancelOrderService.cancelSeaOrder(subOrder, cancelReason);
			if (Boolean.TRUE == resultInfo.isSuccess()) {
				insertOrderStatusLog(subOrder, "订单取消 - 推送海关成功");
			}
			return resultInfo.getMsg();
		}else{
			return new FailInfo("订单" + subOrder.getOrderCode() + "的通关渠道未对接");
		}
	}
	
	//插入日志
	private void insertOrderStatusLog(SubOrder subOrder, String message){
		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName("系统");
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.SYSTEM.code);
		orderStatusLog.setName("海淘订单取消订单");
		orderStatusLog.setContent(message);
		orderStatusLogService.insert(orderStatusLog);
	}
}
