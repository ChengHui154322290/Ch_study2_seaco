package com.tp.service.ord.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CancelInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.SubOrder;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.ICancelInfoService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.local.IOrderCancelLocalService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.StringUtil;

/**
 * 子订单取消服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class OrderCancelLocalService implements IOrderCancelLocalService {

	private static final Logger log = LoggerFactory.getLogger(OrderCancelLocalService.class);

	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private ICancelInfoService cancelInfoService;
	@Autowired
	private IRefundInfoService refundInfoService;
	@Autowired
	private IOutputOrderService outputOrderService;
	@Autowired
	private IInventoryOperService inventoryOperService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	
	/** 
	 * 付款后取消订单，订单支付成功消息后到达处理
	 * @param subOrderDO
	 * @param userId
	 * @param userName
	 */
	@Override
	@Transactional
	public void cancelOrderByPaymentedForJob(SubOrder subOrder, Long userId,String userName) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderId", subOrder.getId());
		List<OrderItem> orderLineList = orderItemService.queryByParam(params);
		MemberInfo user = memberInfoService.queryById(userId);
		
		try{
			ResultInfo<String> resultMessage = inventoryOperService.unoccupyInventory(subOrder.getOrderCode());
			if(!resultMessage.success){
				log.error("用户{}取消订单{}失败！{} 退库存失败,错误信息：{}" ,new Object[]{userId,subOrder.getOrderCode(),resultMessage});
			}
		}catch(Exception e){
			log.error("用户{}取消订单{}失败！{}",new Object[]{userId,subOrder.getOrderCode(),e});
		}
		Long refundNo = addRefundInfo(subOrder,subOrder.getOrderCode());
		CancelInfo cancelInfo = cancelInfoService.addCancelInfo(subOrder,orderLineList, refundNo);

	}
	
	/**
	 * <pre>
	 * 添加退款单信息
	 * </pre>
	 *
	 * @param cancelInfo
	 * @param orderCode
	 */
	private Long addRefundInfo(SubOrder subOrder,Long orderCode){
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setOrderCode(subOrder.getOrderCode());
		refundInfo.setRefundBizType(RefundConstant.BIZ_TYPE.ORDER.code);
		refundInfo.setRefundAmount(BigDecimalUtil.add(subOrder.getFreight(), subOrder.getTotal()).doubleValue());
		refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
		refundInfo.setRefundType(RefundConstant.REFUND_TYPE.CANCEL.code);
		Long orderNo = orderCode;
		if(OrderUtils.isSeaOrder(subOrder.getType())){
			orderNo = subOrder.getOrderCode();
		}
		refundInfo.setGatewayId(paymentInfoService.queryPaymentInfoByBizCode(orderNo).getGatewayId());
		MemberInfo memberInfo = memberInfoService.queryById(subOrder.getMemberId());
		refundInfo.setCreateUser(memberInfo==null?subOrder.getMemberId().toString():memberInfo.getNickName());
		RefundInfo result = refundInfoService.insert(refundInfo);
		if(result != null)
			return result.getRefundCode();
		return null;
	}
	
	/**
	 * <pre>
	 * 退库存
	 * </pre>
	 *
	 * @param subOrderDO
	 */
	private ResultInfo<Boolean> refundStock(SubOrder subOrder){
		// 验证通过退库存
		if(subOrder.getType().equals(1)){
			log.debug("调用库存回滚接口cancelOutputOrder入参：orderCode：{}", subOrder.getOrderCode());
			ResultInfo<String> resultMessage = outputOrderService.cancelOutputOrder(subOrder.getOrderCode());
			if (!resultMessage.success) {
				log.info(" 库存回滚接口调用失败,错误信息：" + resultMessage.getMsg().getMessage());
				return new ResultInfo<Boolean>(new FailInfo("库存回滚调用接口错误",OrderErrorCodes.ROLLBACK_STOCK_ERROR));
			}
			return new ResultInfo<>(Boolean.TRUE);
		}
		return new ResultInfo<Boolean>(new FailInfo("类型不正确"));
	}
	
	/**
	 * <pre>
	 * 给用户发送信息
	 * </pre>
	 *
	 * @param user
	 */
	private void sendSuccessMessage(MemberInfo memberInfo){
		try {
			String shortName = promoterInfoService.queryShortNameByChannelCode(memberInfo.getChannelCode());
			String content = "订单已取消！";
			if(StringUtil.isNoneBlank(shortName)){
				content = "【"+shortName+"】"+content;
			}
			sendSmsService.sendSms(memberInfo.getMobile(), content,null);
		} catch (Exception e) {
			log.error("用户{}取消订单发送信息失败 {}",new Object[]{memberInfo.getId(),e});
		}
	}
}
