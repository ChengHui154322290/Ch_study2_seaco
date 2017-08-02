package com.tp.service.ord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.mmp.BeanUtil;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.mem.SmsUtil;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.common.vo.ord.SubOrderConstant.PutStatus;
import com.tp.common.vo.ord.SubOrderConstant.PutWarehouseStatus;
import com.tp.common.vo.stg.BMLStorageConstant;
import com.tp.dao.ord.MemRealinfoDao;
import com.tp.dao.ord.OrderChannelTrackDao;
import com.tp.dao.ord.OrderConsigneeDao;
import com.tp.dao.ord.OrderInfoDao;
import com.tp.dao.ord.OrderItemDao;
import com.tp.dao.ord.OrderPointDao;
import com.tp.dao.ord.OrderPromotionDao;
import com.tp.dao.ord.OrderReceiptDao;
import com.tp.dao.ord.OrderStatusLogDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.dao.ord.TopicLimitItemDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderAfterPayDTO;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInsertInfoDTO;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.stg.OutputOrderDetailDto;
import com.tp.dto.stg.OutputOrderDto;
import com.tp.dto.wms.StockoutDto;
import com.tp.dto.wms.StockoutItem;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.stg.Warehouse;
import com.tp.mq.RabbitMqProducer;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.service.ord.IOrderService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.local.IOrderCancelLocalService;
import com.tp.service.ord.remote.IOrderCancelRemoteService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.wms.IStockoutService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.JsonFormatUtils;
import com.tp.util.StringUtil;

@Service
public class OrderService implements IOrderService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderInfoDao  orderInfoDao;
	@Autowired
	private SubOrderDao  subOrderDao;
	@Autowired
	private OrderItemDao  orderItemDao;
	@Autowired
	private OrderConsigneeDao  orderConsigneeDao;
	@Autowired
	private OrderReceiptDao  orderReceiptDao;
	@Autowired
	private OrderStatusLogDao  orderStatusLogDao;
	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;
	@Autowired
	private OrderPromotionDao  orderPromotionDao;	
	@Autowired
	private OrderPointDao orderPointDao;
	@Autowired
	private MemRealinfoDao  memRealinfoDao;
	@Autowired
	private TopicLimitItemDao  topicLimitItemDao;
	@Autowired
	private OrderChannelTrackDao orderChannelTrackDao;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IOutputOrderService outputOrderService;
	@Autowired
	private IOrderCancelLocalService orderCancelLocalService;
	@Autowired
	private IOrderCancelRemoteService orderCancelRemoteService;
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	@Autowired
	private IRefundInfoService refundInfoService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	@Autowired
	private IStockoutService stockoutService;
	@Autowired
	private IExpressInfoService expressInfoService;
    
    @Value("#{meta['pay.success.message']}")
    private String paySuccessMessage;
    @Autowired
    private IPromoterInfoService promoterInfoService;
	@Override
	@Transactional
	public Long addOrder(OrderInsertInfoDTO orderInsertInfoDTO) {// 减库存成功做订单插入操作

		OrderInfo orderInfo = new OrderInfo();

		// 插入父订单信息
		orderInfo = orderInsertInfoDTO.getOrderInfo();
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		if (orderInfo.getTotal() < 0) {
			orderInfo.setTotal(0.00);
		}
		orderInfo.setCreateUser(String.valueOf(orderInfo.getMemberId()));
		orderInfo.setUpdateUser(String.valueOf(orderInfo.getMemberId()));
		orderInfo.setBatchNum(""+System.currentTimeMillis());
		orderInfoDao.insert(orderInfo);
		final Long parentOrderId = orderInfo.getId();
		Long parentOrderCode = orderInfo.getParentOrderCode();
		// 插入父订单状态log
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(orderInfo.getParentOrderCode());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);
		orderStatusLog.setContent("提交订单");// 设置日志内容
		orderStatusLog.setCreateUserId(orderInfo.getMemberId());
		orderStatusLog.setCreateUserName(orderInfo.getAccountName());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLogDao.insert(orderStatusLog);

		// 插入子订单信息
		Map<Long, List<OrderItem>> subOrderIdMapOrderLines = new LinkedHashMap<Long, List<OrderItem>>();// 子订单ID对应子订单行list
		Iterator<SubOrder> subOrderKit = orderInsertInfoDTO.getSubOrderMapOrderItem().keySet().iterator();
		while (subOrderKit.hasNext()) {
			SubOrder subOrder = (SubOrder) subOrderKit.next();

			List<OrderItem> tempOrderLineList = new ArrayList<OrderItem>();
			tempOrderLineList = orderInsertInfoDTO.getSubOrderMapOrderItem().get(subOrder);// 取出当前子订单对应订单行list
			subOrder.setConsigneeName(orderInsertInfoDTO.getOrderConsignee().getName());// 插入收货人名字
			subOrder.setConsigneeMobile(orderInsertInfoDTO.getOrderConsignee().getMobile());// 收货人手机
			subOrder.setParentOrderId(parentOrderId);
			subOrder.setCreateTime(new Date());
			subOrder.setUpdateTime(new Date());
			subOrder.setGroupId(orderInsertInfoDTO.getGroupId());
			subOrderDao.insert(subOrder);
			Long subOrderId = subOrder.getId();
			Long subOrderCode = subOrder.getOrderCode();
			for(OrderItem orderItem:tempOrderLineList){
				orderItem.setOrderCode(subOrderCode);
				orderItem.setOrderId(subOrderId);
				orderItem.setParentOrderId(parentOrderId);
				orderItem.setParentOrderCode(parentOrderCode);
			}
			// 设置子订单ID对应订单行对象
			subOrderIdMapOrderLines.put(subOrderId, tempOrderLineList);

			// 插入子订单状态log
			OrderStatusLog subOrderStatusLog = new OrderStatusLog();
			subOrderStatusLog.setParentOrderCode(orderInfo.getParentOrderCode());
			subOrderStatusLog.setOrderCode(subOrder.getOrderCode());
			subOrderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);
			subOrderStatusLog.setContent("提交订单");// 设置日志内容
			subOrderStatusLog.setCreateUserId(orderInfo.getMemberId());
			orderStatusLog.setCreateUserName(orderInfo.getAccountName());
			subOrderStatusLog.setCreateTime(new Date());
			orderStatusLogDao.insert(subOrderStatusLog);
		}

		// 插入订单行信息
		// 记录子订单ID对应订单行ID对应优惠券list MAP
		Map<Long, Map<Long, List<OrderPromotion>>> subOrderOrderLineMapPromotion = new LinkedHashMap<Long, Map<Long, List<OrderPromotion>>>();
		// key值为subOrderId
		Iterator<Long> subOrderIdKit = subOrderIdMapOrderLines.keySet().iterator();
		while (subOrderIdKit.hasNext()) {
			Long subOrderKey = (Long) subOrderIdKit.next();
			List<OrderItem> orderItems = subOrderIdMapOrderLines.get(subOrderKey);
			// 记录订单行对应优惠行list
			Map<Long, List<OrderPromotion>> orderItemIdMapPromotion = new LinkedHashMap<Long, List<OrderPromotion>>();
			for (OrderItem orderItem : orderItems) {
				// 记录对应的优惠券list
				List<OrderPromotion> tempOrderPromotion = new ArrayList<OrderPromotion>();
				if (null != orderInsertInfoDTO.getOrderItemMapPromotion() && orderInsertInfoDTO.getOrderItemMapPromotion().size() > 0) {
					if (orderInsertInfoDTO.getOrderItemMapPromotion().containsKey(orderItem)) {// 该行如果有优惠信息
						tempOrderPromotion = orderInsertInfoDTO.getOrderItemMapPromotion().get(orderItem);// 记录优惠List
						for (OrderPromotion orderPromotion : tempOrderPromotion) {
							orderPromotion.setOrderCode(orderItem.getOrderCode());
							orderPromotion.setOrderId(orderItem.getOrderId());
							orderPromotion.setParentOrderId(parentOrderId);
							orderPromotion.setParentOrderCode(parentOrderCode);
						}
					}
				}
				
				// 设置订单行信息并插入数据库
				orderItem.setParentOrderId(parentOrderId);
				orderItem.setParentOrderCode(parentOrderCode);
				orderItem.setOrderId(subOrderKey);
				orderItem.setMemberId(orderInfo.getMemberId());// userID
				orderItem.setIp(orderInfo.getIp());// IP
				orderItem.setCreateTime(new Date());
				orderItem.setUpdateTime(new Date());
				//orderItem.setOrderCode(orderInfo);
				orderItemDao.insert(orderItem);
				Long orderItemId = orderItem.getId();
				if (tempOrderPromotion.size() > 0) {// 该行如果有优惠信息
					// 将订单行id和优惠券list对应
					orderItemIdMapPromotion.put(orderItemId, tempOrderPromotion);
				}
			}

			for (OrderItem orderItem : orderItems) {
				orderItem.getOrderPointList().forEach(new Consumer<OrderPoint>(){
					public void accept(OrderPoint t) {
						t.setOrderCode(orderItem.getOrderCode());
						t.setOrderId(orderItem.getOrderId());
						t.setParentOrderId(parentOrderId);
						t.setParentOrderCode(parentOrderCode);
						t.setOrderItemId(orderItem.getId());
						orderPointDao.insert(t);
					}
				});
			}
			if (orderItemIdMapPromotion.size() > 0) {// 假如子订单有优惠
				// 记录子订单ID对应订单行ID对应优惠券list
				subOrderOrderLineMapPromotion.put(subOrderKey, orderItemIdMapPromotion);
			}
		}

		// 插入优惠信息表
		if (null != orderInsertInfoDTO.getOrderItemMapPromotion() && orderInsertInfoDTO.getOrderItemMapPromotion().size() > 0) {
			// key值为subOrderId
			subOrderIdKit = subOrderOrderLineMapPromotion.keySet().iterator();
			while (subOrderIdKit.hasNext()) {
				Long subOrderKey = (Long) subOrderIdKit.next();
				Map<Long, List<OrderPromotion>> tempOrderLineIdMapPromotion = subOrderOrderLineMapPromotion.get(subOrderKey);
				// key值为orderItemId
				Iterator<Long> orderItemIdKit = tempOrderLineIdMapPromotion.keySet().iterator();
				while (orderItemIdKit.hasNext()) {
					Long orderItemKey = (Long) orderItemIdKit.next();
					List<OrderPromotion> tempOrderPromotions = tempOrderLineIdMapPromotion.get(orderItemKey);
					for (OrderPromotion orderPromotion : tempOrderPromotions) {
						orderPromotion.setParentOrderId(parentOrderId);// 设置父订单ID
						orderPromotion.setOrderId(subOrderKey);// 设置子订单ID
						orderPromotion.setOrderItemId(orderItemKey);// 设置订单行ID
						orderPromotion.setParentOrderCode(parentOrderCode);
						orderPromotion.setCreateTime(new Date());
						orderPromotion.setUpdateTime(new Date());
						BeanUtil.processNullField(orderPromotion);
						orderPromotionDao.insert(orderPromotion);
					}
				}
			}
		}

		// 插入收货地址信息
		orderInsertInfoDTO.getOrderConsignee().setParentOrderId(parentOrderId);
		orderInsertInfoDTO.getOrderConsignee().setParentOrderCode(parentOrderCode);
		orderInsertInfoDTO.getOrderConsignee().setCreateTime(new Date());
		orderConsigneeDao.insert(orderInsertInfoDTO.getOrderConsignee());

		// 插入发票信息
		if (orderInsertInfoDTO.getOrderReceipt() != null) {// 有发票信息
			orderInsertInfoDTO.getOrderReceipt().setParentOrderId(parentOrderId);
			orderInsertInfoDTO.getOrderReceipt().setParentOrderCode(parentOrderCode);
			orderInsertInfoDTO.getOrderReceipt().setCreateTime(new Date());
			orderReceiptDao.insert(orderInsertInfoDTO.getOrderReceipt());
		}
		// 插入海淘实名认证信息
		if (null != orderInsertInfoDTO.getMemRealinfo()) {
			orderInsertInfoDTO.getMemRealinfo().setCreateTime(new Date());
			orderInsertInfoDTO.getMemRealinfo().setUpdateTime(new Date());
			orderInsertInfoDTO.getMemRealinfo().setParentOrderCode(parentOrderCode);
			orderInsertInfoDTO.getMemRealinfo().setParentOrderId(parentOrderId);
			memRealinfoDao.insert(orderInsertInfoDTO.getMemRealinfo());
		}

		// 插入限购商品购买信息
		if (CollectionUtils.isNotEmpty(orderInsertInfoDTO.getTopicLimitItems())) {
			for (TopicLimitItem topicLimitItem : orderInsertInfoDTO.getTopicLimitItems()) {
				topicLimitItemDao.saveLimit(topicLimitItem);
			}
		}

		return parentOrderId;
	}

	@Override
	@Transactional
	public void operateAfterSuccessPay(PaymentInfo paymentInfo) {
		if (!validateParam(paymentInfo))
			return; // 入参校验

		OrderAfterPayDTO order = toOrderAfterPayDTO(paymentInfo);
		if (null == order)
			return;

		/* 若已支付（重复支付），则退款 */
		if (OrderConstant.ORDER_STATUS.PAYMENT.code < order.getStatus() && paymentInfo.getGatewayId().intValue() != order.getGatewayId().intValue()) {
			log.info("Order Status:{} - Order GatewayId：{} - Payment GatewayId:{}", order.getStatus(), order.getGatewayId(), paymentInfo.getGatewayId());
			//addRefundInfo(paymentInfo);
			return;
		}
		List<SubOrder> subOrderList = getSubOrderByParentOrderCode(paymentInfo.getBizCode());
		SubOrder subOrder = subOrderDao.selectOneByCode(paymentInfo.getBizCode());
		
		/* 若已取消，则取消并退款 */
		if (OrderConstant.ORDER_STATUS.CANCEL.code.equals(order.getStatus())) {
			if (PaymentConstant.BIZ_TYPE.ORDER.code.equals(paymentInfo.getBizType())) { // 父订单
				if (CollectionUtils.isNotEmpty(subOrderList)) {
					for (SubOrder sub : subOrderList) {
						orderCancelLocalService.cancelOrderByPaymentedForJob(sub, order.getMemberId(), order.getNickname());
					}
				}
			} else { // 子订单（海淘）
				orderCancelLocalService.cancelOrderByPaymentedForJob(subOrder, order.getMemberId(), order.getNickname());
			}
			return;
		}
		log.info("支付回调修改订单状态参数{} {}",paymentInfo.getBizType(),paymentInfo.getBizCode());
		/* 更新订单状态并发货 */
		if (PaymentConstant.BIZ_TYPE.ORDER.code.equals(paymentInfo.getBizType())
			||PaymentConstant.BIZ_TYPE.MERGEORDER.code.equals(paymentInfo.getBizType())) { // 父订单
			updateOrderToPayed(paymentInfo, order, subOrderList);
			callWareHouseShipping(order.getOrderId(), subOrderList);

			//passPromoter(subOrderList.get(0));
		} else if (PaymentConstant.BIZ_TYPE.SUBORDER.code.equals(paymentInfo.getBizType())) { // 子订单（海淘）
			updateSubOrderToPayed(paymentInfo, order);
			putWareHouseShippingBySubOrder(subOrder);

			//passPromoter(subOrder);
		}
		sendSms(order.getMemberId());
	}

	// 更新子订单为‘待发货’状态
	private void updateSubOrderToPayed(PaymentInfo paymentInfo, OrderAfterPayDTO order) {
		SubOrder subOrderOld = subOrderDao.selectOneByCode(paymentInfo.getBizCode());
		SubOrder subOrder = new SubOrder();
		subOrder.setOrderCode(paymentInfo.getBizCode());
		subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		if(OrderConstant.FAST_ORDER_TYPE==subOrderOld.getType().intValue()){
			subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.TRANSFER.code);
		}else if(OrderConstant.OrderType.BUY_COUPONS.code==subOrderOld.getType().intValue()){
			subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
		}
		subOrder.setUpdateTime(new Date());
		subOrder.setPayCode(paymentInfo.getGatewayTradeNo());
		subOrder.setPayTime(paymentInfo.getCallbackTime());
		subOrder.setPayWay(paymentInfo.getGatewayId());
		subOrder.setPayType(paymentInfo.getPaymentType());
		subOrder.setPutStatus(PutStatus.NEW.code);
		subOrderDao.updateSubOrderStatus(subOrder);
		OrderInfo orderInfoForUpdate = new OrderInfo();
		orderInfoForUpdate.setParentOrderCode(subOrderOld.getParentOrderCode());
		orderInfoForUpdate.setOrderStatus(subOrder.getOrderStatus());
		orderInfoForUpdate.setUpdateTime(new Date());
		orderInfoForUpdate.setPayTime(paymentInfo.getCallbackTime());
		orderInfoDao.updateSalesOrderStatusAfterSuccessPay(orderInfoForUpdate);
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setOrderCode(paymentInfo.getBizCode());
		orderStatusLog.setParentOrderCode(order.getOrderCode());
		orderStatusLog.setPreStatus(order.getStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setContent("支付成功后更新订单状态");
		orderStatusLog.setCreateUserId(order.getMemberId());
		orderStatusLog.setCreateUserName(order.getNickname());
		orderStatusLogDao.insert(orderStatusLog);
	}

	// 更新父订单为‘待发货’状态
	private void updateOrderToPayed(PaymentInfo paymentInfo, OrderAfterPayDTO order, List<SubOrder> subOrderList) {
		// 更新父订单信息
		OrderInfo orderInfoForUpdate = new OrderInfo();
		orderInfoForUpdate.setParentOrderCode(paymentInfo.getBizCode());
		orderInfoForUpdate.setOrderStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		orderInfoForUpdate.setUpdateTime(new Date());
		orderInfoForUpdate.setPayCode(paymentInfo.getPaymentTradeNo());
		orderInfoForUpdate.setPayTime(paymentInfo.getCallbackTime());
		orderInfoForUpdate.setPayWay(paymentInfo.getGatewayId());
		orderInfoForUpdate.setPayType(paymentInfo.getPaymentType());
		orderInfoDao.updateSalesOrderStatusAfterSuccessPay(orderInfoForUpdate);

		// 更新子订单信息
		for(SubOrder uSubOrder:subOrderList){
			SubOrder subOrder = new SubOrder();
			subOrder.setOrderCode(uSubOrder.getOrderCode());
			subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
			if(OrderConstant.FAST_ORDER_TYPE==uSubOrder.getType().intValue()){
				subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.TRANSFER.code);
			}
			subOrder.setUpdateTime(new Date());
			subOrder.setPayCode(paymentInfo.getGatewayTradeNo());
			subOrder.setPayTime(paymentInfo.getCallbackTime());
			subOrder.setPayWay(paymentInfo.getGatewayId());
			subOrder.setPayType(paymentInfo.getPaymentType());
			subOrderDao.updateSubOrderStatus(subOrder);
		}

		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(paymentInfo.getBizCode());
		orderStatusLog.setPreStatus(order.getStatus());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setContent("支付成功后更新父订单状态");
		orderStatusLog.setCreateUserId(order.getMemberId());
		orderStatusLog.setCreateUserName(order.getNickname());
		orderStatusLogDao.insert(orderStatusLog);

		if (CollectionUtils.isNotEmpty(subOrderList)) {
			for (SubOrder subOrder1 : subOrderList) {
				OrderStatusLog orderStatuslg = new OrderStatusLog();
				orderStatuslg.setParentOrderCode(paymentInfo.getBizCode());
				orderStatuslg.setOrderCode(subOrder1.getOrderCode());
				orderStatuslg.setPreStatus(subOrder1.getOrderStatus());
				orderStatuslg.setCurrStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
				orderStatuslg.setCreateTime(new Date());
				orderStatuslg.setContent("支付成功后更新子订单状态");
				orderStatuslg.setCreateUserId(order.getMemberId());
				orderStatuslg.setCreateUserName(order.getNickname());
				orderStatusLogDao.insert(orderStatuslg);
			}
		}
	}

	private boolean validateParam(PaymentInfo paymentInfo) {
		if (null == paymentInfo || null == paymentInfo.getAmount() || null == paymentInfo.getBizCode() || null == paymentInfo.getBizType()
				|| null == paymentInfo.getPaymentTradeNo() || null == paymentInfo.getCallbackTime() || null == paymentInfo.getGatewayId() || null == paymentInfo.getPaymentType()) {
			log.error("入参错误[{}]", JsonFormatUtils.format(paymentInfo));
			return false;
		}
		return true;
	}

	// 转换OrderAfterPayDTO
	private OrderAfterPayDTO toOrderAfterPayDTO(PaymentInfo paymentInfo) {
		if (PaymentConstant.BIZ_TYPE.ORDER.code.equals(paymentInfo.getBizType())
		 ||PaymentConstant.BIZ_TYPE.MERGEORDER.code.equals(paymentInfo.getBizType())) { // 父订单
			OrderInfo order = orderInfoDao.selectOneByCode(paymentInfo.getBizCode());
			if (null == order) {
				log.error("父订单[{}]不存在", paymentInfo.getBizCode());
				return null;
			}

			return toOrderAfterPayDTO(order, paymentInfo.getBizType());
		} else if (PaymentConstant.BIZ_TYPE.SUBORDER.code.equals(paymentInfo.getBizType())) { // 子订单
			SubOrder sub = subOrderDao.selectOneByCode(paymentInfo.getBizCode());
			if (null == sub) {
				log.error("子订单[{}]不存在", paymentInfo.getBizCode());
				return null;
			}

			return toOrderAfterPayDTO(sub, paymentInfo.getBizType());
		} else {
			log.error("错误的业务类型[{}]", paymentInfo.getBizType());
			return null;
		}
	}

	// 子单转OrderAfterPayDTO
	private OrderAfterPayDTO toOrderAfterPayDTO(SubOrder order, int bizType) {
		OrderAfterPayDTO dto = new OrderAfterPayDTO();
		dto.setOrderId(order.getParentOrderId());
		dto.setOrderCode(order.getParentOrderCode());
		dto.setMemberId(order.getMemberId());
		dto.setStatus(order.getOrderStatus());
		dto.setNickname(getNickname(order.getMemberId()));
		dto.setGatewayId(order.getPayWay());
		return dto;
	}

	// 父单转OrderAfterPayDTO
	private OrderAfterPayDTO toOrderAfterPayDTO(OrderInfo order, int bizType) {
		OrderAfterPayDTO dto = new OrderAfterPayDTO();
		dto.setOrderId(order.getId());
		dto.setOrderCode(order.getParentOrderCode());
		dto.setMemberId(order.getMemberId());
		dto.setStatus(order.getOrderStatus());
		dto.setNickname(getNickname(order.getMemberId()));
		dto.setGatewayId(order.getPayWay());
		return dto;
	}

	// 获取用户昵称
	private String getNickname(Long memberId) {
		MemberInfo memberInfo = memberInfoService.queryById(memberId);
		if (null == memberInfo) {
			log.error("id为[{}]的用户不存在");
			return null;
		}
		return memberInfo.getNickName();
	}

	private List<SubOrder> getSubOrderByParentOrderCode(Long parentOrderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", parentOrderCode);
		return subOrderDao.queryByParam(params);
	}

	private void callWareHouseShipping(long parentOrderId, List<SubOrder> subOrderList) {
		// 1.根据父订单编号查找收货人信息
		OrderConsignee orderConsignee = orderConsigneeDao.selectOneByParentOrderId(parentOrderId);

		// 3.组装调用接口参数
		if (orderConsignee != null) {
			if (CollectionUtils.isNotEmpty(subOrderList)) {
				for (SubOrder subOrder : subOrderList) {
					putWareHouseShippingBySubOrder(subOrder);
				}
			}
		}
	}

	@Override
	public boolean orderCancelCheck(Long parentOrderCode, Long parentOrderId) {
		OrderInfo currentSalesOrder =orderInfoDao.selectOneByCode(parentOrderCode);
		if (null!=currentSalesOrder) {
			Integer status = currentSalesOrder.getOrderStatus();
			Long memberId = currentSalesOrder.getMemberId();
			if (status != null && status.intValue() < OrderConstant.ORDER_STATUS.DELIVERY.code.intValue()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public OrderInfo operateAfterCancel(Long parentOrderCode, Long userId, String userName) {

		// 根据父订单code查询父订单信息
		OrderInfo orderInfo = orderInfoDao.selectOneByCode(parentOrderCode);
		if (null!=orderInfo) {
			Integer preStatus = orderInfo.getOrderStatus();
			// 更新父订单信息
			OrderInfo orderInfoForUpdate = new OrderInfo();
			orderInfoForUpdate.setParentOrderCode(parentOrderCode);
			orderInfoForUpdate.setOrderStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
			orderInfoForUpdate.setUpdateTime(new Date());
			orderInfoDao.updateSalesOrderStatusAfterCancel(orderInfoForUpdate);
			// 更新子订单信息
			SubOrder subOrder = new SubOrder();
			subOrder.setParentOrderCode(parentOrderCode);
			subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
			subOrder.setUpdateTime(new Date());
			subOrderDao.updateSubOrderStatus(subOrder);

			// 添加订单状态日志信息
			List<OrderStatusLog> orderStatusLogList = new ArrayList<OrderStatusLog>();
			OrderStatusLog orderStatusLog = new OrderStatusLog();
			orderStatusLog.setParentOrderCode(parentOrderCode);
			orderStatusLog.setPreStatus(preStatus);
			orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
			orderStatusLog.setContent("父订单取消");
			orderStatusLog.setCreateTime(new Date());
			orderStatusLog.setCreateUserId(userId);
			orderStatusLog.setCreateUserName(userName);
			orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.MEMBER.code);
			orderStatusLogList.add(orderStatusLog);
			List<SubOrder> subOrderList = getSubOrderByParentOrderCode(parentOrderCode);
			for (SubOrder sub : subOrderList) {
				OrderStatusLog orderStatusLg = new OrderStatusLog();
				orderStatusLg.setParentOrderCode(parentOrderCode);
				orderStatusLg.setOrderCode(sub.getOrderCode());
				orderStatusLg.setPreStatus(preStatus);
				orderStatusLg.setCurrStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
				orderStatusLg.setCreateTime(new Date());
				orderStatusLg.setCreateUserType(LogTypeConstant.LOG_TYPE.MEMBER.code);
				orderStatusLg.setContent("子订单取消");
				orderStatusLg.setCreateUserId(userId);
				orderStatusLg.setCreateUserName(userName);
				orderStatusLogList.add(orderStatusLg);
			}
			orderStatusLogDao.batchInsert(orderStatusLogList);
		}

		return orderInfo;
	}

	@Override
	public Long generateCode(OrderCodeType type){
		return orderCodeGeneratorService.generate(type);
	}

	private void addRefundInfo(PaymentInfo paymentInfo) {
		log.info("Add a refund info for repay...");
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setOrderCode(paymentInfo.getBizCode());
		refundInfo.setRefundBizType(paymentInfo.getBizType());
		refundInfo.setRefundAmount(paymentInfo.getAmount());
		refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
		refundInfo.setRefundType(RefundConstant.REFUND_TYPE.REPEAT_PAYMENT.code);
		refundInfo.setGatewayId(paymentInfo.getGatewayId());
		refundInfo.setCreateUser("[系统-重复付款1]");
		refundInfo.setUpdateUser("[系统-重复付款1]");
		refundInfoService.insert(refundInfo);
	}

	/**
	 * 海淘自营子订单推送仓库
	 *  
	 */
	@Override
	public boolean putWareHouseShippingBySeaSubOrder(SubOrder subOrder) {
		if (!subOrder.getPutStorage() || !subOrder.getPutCleanOrder()) {	//不需要推送仓库或者不需要报关的订单不调用此接口
			return false;
		}
		final String key = "seaorder:suborder:putwarehouseshipping:" + subOrder.getOrderCode();
		OrderConsignee orderConsignee = orderConsigneeDao.selectOneByParentOrderId(subOrder.getParentOrderId());
		OrderReceipt receipt = orderReceiptDao.selectOneByParentOrderCode(subOrder.getParentOrderId());
		PersonalgoodsDeclareInfo declareInfo = personalgoodsDeclareInfoService.queryUniquePersonalGoodsDeclByOrderCode(subOrder.getOrderCode().toString());
		ResultInfo<String> resultMessage = putWareHouseShippingBySeaSubOrder(subOrder, orderConsignee, receipt, declareInfo);
		Boolean putted = Boolean.FALSE;
		if (resultMessage.success) {
			putted = Boolean.TRUE;
		}
		SubOrder putSubOrder = new SubOrder();
		putSubOrder.setPutStatus(putted ? 1 : 0);
		if (!putted && jedisCacheUtil.incr(key) > 3) {
			putSubOrder.setPutStatus(PutWarehouseStatus.PUT_FAIL.code);
		}
		if (0 != putSubOrder.getPutStatus()) {
			jedisCacheUtil.deleteCacheStringKey(key);
		}
		putSubOrder.setUpdateTime(new Date());
		putSubOrder.setId(subOrder.getId());
		subOrderDao.updateNotNullById(putSubOrder);
		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(LogTypeConstant.LOG_TYPE.SYSTEM.code.longValue());
		orderStatusLog.setCreateUserName(LogTypeConstant.LOG_TYPE.SYSTEM.cnName);
		if (resultMessage != null) {
			orderStatusLog.setContent("海淘订单清关通过,推送仓库返回信息：" + resultMessage.toString());
		} 
		orderStatusLogDao.insert(orderStatusLog);
		return putted;
	}
	
	/**
	 * 海淘自营子订单推送仓库
	 */
	private ResultInfo<String> putWareHouseShippingBySeaSubOrder(final SubOrder subOrder, 
			final OrderConsignee orderConsignee, final OrderReceipt receipt, final PersonalgoodsDeclareInfo declareInfo){
		if (declareInfo == null) {
			return new ResultInfo<>(new FailInfo("个人申报信息为空"));
		}
		// 根据子订单信息获取商品详情
		List<OrderItem> orderItemList = orderItemDao.selectBySubOrderId(subOrder.getId());
		if (CollectionUtils.isEmpty(orderItemList)) {
			return new ResultInfo<>(new FailInfo("商品详情为空"));
		}
		Warehouse warehouse = warehouseService.queryById(subOrder.getWarehouseId());
		if(null == warehouse){
			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
		}
		ExpressInfo expressInfo = expressInfoService.selectByCode(warehouse.getLogistics());
		StockoutDto stockoutDto = new StockoutDto();
		stockoutDto.setOrderCode(subOrder.getOrderCode().toString());
		stockoutDto.setOrderCreateTime(subOrder.getCreateTime());
		stockoutDto.setOrderPayTime(subOrder.getPayTime());
		stockoutDto.setMemberId(subOrder.getMemberId());
		stockoutDto.setTotalAmount(subOrder.getOriginalTotal());
		stockoutDto.setPayAmount(subOrder.getPayTotal());
		stockoutDto.setDiscountAmount(subOrder.getDiscount());
		stockoutDto.setPostAmount(subOrder.getFreight());
		stockoutDto.setIsPostagePay(1);
		stockoutDto.setIsDeliveryPay(1);
		stockoutDto.setIsUrgency(0);
		stockoutDto.setWarehouseCode(warehouse.getCode());
		stockoutDto.setWarehouseId(warehouse.getId());
		stockoutDto.setWarehouseName(warehouse.getName());
		stockoutDto.setLogisticsCompanyCode(warehouse.getLogistics());
		stockoutDto.setLogisticsCompanyName(expressInfo.getName());
		stockoutDto.setExpressNo(declareInfo.getExpressNo());	
		/*------------ 收件人信息 ------------*/
		stockoutDto.setPostCode("");
		stockoutDto.setProvince(orderConsignee.getProvinceName());
		stockoutDto.setCity(orderConsignee.getCityName());
		stockoutDto.setArea(orderConsignee.getCountyName());
		stockoutDto.setAddress(orderConsignee.getTownName() + orderConsignee.getAddress());
		stockoutDto.setConsignee(orderConsignee.getName());
		stockoutDto.setMobile(orderConsignee.getMobile());
		stockoutDto.setTel(orderConsignee.getTelephone());
		/*------------发票信息-------------*/
		stockoutDto.setIsInvoice(1); //海淘订单不开发票
		stockoutDto.setInvoiceInfoList(null);
		/*------------订单商品信息-------------*/
		List<StockoutItem> stockItemList = new ArrayList<>();
		for (int i = 0; i < orderItemList.size(); i++) {
			OrderItem orderItem = orderItemList.get(i);
			StockoutItem item = new StockoutItem();
			item.setOrderItemId(Long.valueOf(i));
			item.setItemSku(orderItem.getSkuCode());
			item.setItemName(orderItem.getSpuName());
			item.setBarCode(orderItem.getBarCode());
			item.setQuantity(orderItem.getRealQuantity()); //多件装的商品需要做处理
			item.setActualPrice(orderItem.getPrice());
			item.setSalesprice(orderItem.getSalesPrice());
			item.setDiscountAmount(orderItem.getDiscount());
			stockItemList.add(item);
		}
		stockoutDto.setOrderItemList(stockItemList);
		stockoutDto.setWarehouse(warehouse);
		try {
			ResultInfo<Boolean> resultInfo = stockoutService.deliverStockoutOrder(stockoutDto);
			if (!resultInfo.success) {
				log.error("清关通过后推送订单至仓库失败,stockoutDto：{},错误信息：{}",JsonFormatUtils.format(stockoutDto), JsonFormatUtils.format(resultInfo.getMsg()));
				return new ResultInfo<>(resultInfo.getMsg());
			}
			return new ResultInfo<>("推送成功");
		} catch (Exception e) {
			log.error("清关通过后推送订单至仓库失败！subOrderCode:" + subOrder.getOrderCode(), e);
			return new ResultInfo<>(new FailInfo(e));
		}
	}
	
	@Override
	public Boolean putWareHouseShippingBySubOrder(SubOrder subOrder) {
		if (!subOrder.getPutStorage() || subOrder.getPutCleanOrder()) {	//不需要推送仓库
			return Boolean.FALSE; //需要报关,不直接推送
		}
		// 1.根据父订单编号查找收货人信息
		final String key = "order:suborder:putwarehouseshipping:" + subOrder.getOrderCode();
		OrderConsignee orderConsignee = orderConsigneeDao.selectOneByParentOrderCode(subOrder.getParentOrderCode());
		OrderReceipt receipt = orderReceiptDao.selectOneByParentOrderCode(subOrder.getParentOrderId());
		ResultInfo<String> resultMessage = putWareHouseShippingBySubOrder(subOrder, orderConsignee, receipt);
		Boolean putted = Boolean.FALSE;
		if (resultMessage.success) {
			putted = Boolean.TRUE;
		}
		SubOrder putSubOrder = new SubOrder();
		putSubOrder.setPutStatus(putted ? 1 : 0);
		if (!putted && jedisCacheUtil.incr(key) > 3) {
			putSubOrder.setPutStatus(PutStatus.NEW.code);
		}
		if (0 != putSubOrder.getPutStatus()) {
			jedisCacheUtil.deleteCacheStringKey(key);
		}
		putSubOrder.setUpdateTime(new Date());
		putSubOrder.setId(subOrder.getId());
		putSubOrder.setTotal(null);
		putSubOrder.setItemTotal(null);
		putSubOrder.setOriginalTotal(null);
		putSubOrder.setDiscount(null);
		putSubOrder.setFreight(null);
		subOrderDao.updateNotNullById(putSubOrder);
		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(LogTypeConstant.LOG_TYPE.SYSTEM.code.longValue());
		orderStatusLog.setCreateUserName(LogTypeConstant.LOG_TYPE.SYSTEM.cnName);
		if (resultMessage != null) {
			orderStatusLog.setContent("支付成功30分钟后推送子订单信息到仓库,返回消息" + resultMessage.toString());
		} 
		orderStatusLogDao.insert(orderStatusLog);
		return putted;
	}

	/**
	 * 根据子订单推送商品到仓库
	 * 
	 * @param subOrder
	 * @param orderConsigneeDO
	 */
	private ResultInfo<String> putWareHouseShippingBySubOrder(final SubOrder subOrder, final OrderConsignee orderConsignee, final OrderReceipt receipt) {

		List<OutputOrderDetailDto> orderDetailDtoList = new ArrayList<OutputOrderDetailDto>();

		OutputOrderDto outputOrderDto = new OutputOrderDto();
		outputOrderDto.setOrderCode(String.valueOf(subOrder.getOrderCode()));
		outputOrderDto.setShipping(BMLStorageConstant.ExpressType.STO.getCode());
		outputOrderDto.setFreight(subOrder.getFreight());
		outputOrderDto.setName(orderConsignee.getName());
		outputOrderDto.setPostCode(orderConsignee.getPostcode());
		outputOrderDto.setMobile(orderConsignee.getMobile());
		outputOrderDto.setProv(orderConsignee.getProvinceName());
		outputOrderDto.setCity(orderConsignee.getCityName());
		outputOrderDto.setDistrict(orderConsignee.getCountyName());
		outputOrderDto.setAddress(orderConsignee.getTownName()+"　"+orderConsignee.getAddress());
		outputOrderDto.setServiceCharge(subOrder.getDiscount());

		if (null != receipt) { // 需要开票
			outputOrderDto.setIsCashsale(StorageConstant.INVOICE_REQ); // 是否开票
			outputOrderDto.setWebsite(OrderReceipt.getTitle(receipt)); // 发票抬头
		} else { // 不需要开票
			outputOrderDto.setIsCashsale(StorageConstant.INVOICE_NO); // 是否开票
		}

		outputOrderDto.setItemsValue(BigDecimalUtil.formatToPrice(BigDecimalUtil.add(subOrder.getTotal(), subOrder.getFreight())).doubleValue());

		// 根据子订单信息获取商品详情
		List<OrderItem> orderItemList = orderItemDao.selectBySubOrderId(subOrder.getId());

		Map<String, String> storageTitleMap = getStorageTitleMap(orderItemList); // key:sku编号，value:storageTitle

		if (CollectionUtils.isNotEmpty(orderItemList)) {
			for (OrderItem orderItem : orderItemList) {
				OutputOrderDetailDto outputOrderDetailDto = new OutputOrderDetailDto();
				outputOrderDetailDto.setSku(orderItem.getSkuCode());
				outputOrderDetailDto.setApp(App.PROMOTION);
				outputOrderDetailDto.setBizId(orderItem.getTopicId().toString());
				outputOrderDetailDto.setItemCount(orderItem.getQuantity());
				outputOrderDetailDto.setBarcode(orderItem.getBarCode());
				outputOrderDetailDto.setItemName(storageTitleMap.get(orderItem.getSkuCode()));
				outputOrderDetailDto.setItemValue(BigDecimalUtil.divide(orderItem.getSubTotal(), orderItem.getQuantity()).doubleValue());
				orderDetailDtoList.add(outputOrderDetailDto);
			}
		}

		outputOrderDto.setOrderDetailDtoList(orderDetailDtoList);
		try {
			ResultInfo<String> resultInfo = outputOrderService.deliverOutputOrder(outputOrderDto, toStorageType(subOrder.getType()));
			if (!resultInfo.success) {
				log.error("支付成功后,调用仓库订单发货出库接口失败,outputOrderDto：{},错误信息：{}",JsonFormatUtils.format(outputOrderDto), JsonFormatUtils.format(resultInfo.msg));
				return new ResultInfo<>(resultInfo.getMsg());
			}
			return new ResultInfo<>("推送成功");
		} catch (Exception e) {
			log.error("支付成功后,调用仓库订单发货出库接口失败！subOrderCode:{} ，错误：{}", new Object[] { subOrder.getOrderCode(), e });
			return new ResultInfo<>(new FailInfo(e));
		}
	}
	
	// 订单类型转仓库类型
	private StorageType toStorageType(int orderType) {
		OrderType orderTypeEnum = OrderType.getOrderTypeByCode(orderType);
		switch (orderTypeEnum) {
		case COMMON:
			return StorageType.COMMON;
		case PLATFORM:
			return StorageType.PLATFORM;
		case DOMESTIC:
			return StorageType.DOMESTIC;
		case BONDEDAREA:
			return StorageType.BONDEDAREA;
		case OVERSEASMAIL:
			return StorageType.OVERSEASMAIL;
		case COMMON_SEA:
			return StorageType.COMMON_SEA;
		default:
			throw new IllegalArgumentException("无效的订单类型");
		}
	}

	// 获取map（key － sku编号，value － StorageTitle）
	private Map<String, String> getStorageTitleMap(List<OrderItem> orderItemList) {
		List<String> skuCodeList = extractSkuCodeList(orderItemList);
		if (CollectionUtils.isNotEmpty(skuCodeList)) {
			List<ItemResultDto> itemList = itemService.getSkuList(skuCodeList);
			return toStorageTitleMap(itemList);
		}
		return Collections.emptyMap();
	}

	// 转map（key － sku编号，value － StorageTitle）
	private Map<String, String> toStorageTitleMap(List<ItemResultDto> itemList) {
		if (CollectionUtils.isNotEmpty(itemList)) {
			Map<String, String> map = new HashMap<String, String>();
			for (ItemResultDto item : itemList) {
				if (null != item && null != item.getSku()) {
					map.put(item.getSku(), item.getStorageTitle());
				}
			}
			return map;
		}
		return Collections.emptyMap();
	}

	// 提取sku编号列表
	private List<String> extractSkuCodeList(List<OrderItem> orderItemList) {
		if (CollectionUtils.isNotEmpty(orderItemList)) {
			Set<String> skuCodeSet = new HashSet<String>(orderItemList.size());
			for (OrderItem line : orderItemList) {
				if (null != line && null != line.getSkuCode()) {
					skuCodeSet.add(line.getSkuCode());
				}
			}
			return new ArrayList<String>(skuCodeSet);
		}
		return Collections.emptyList();
	}
	
	// 发短信
    private void sendSms(final Long memberId) {
        try {
            MemberInfo memberInfo = memberInfoService.queryById(memberId);
			String shortName = promoterInfoService.queryShortNameByChannelCode(memberInfo.getChannelCode());
			String content = paySuccessMessage;
			if(StringUtil.isNoneBlank(paySuccessMessage) && StringUtil.isNoneBlank(shortName)){
				content = "【"+shortName+"】"+paySuccessMessage;
			}
            sendSmsService.sendSms(memberInfo.getMobile(), content,null);
        } catch (Exception e) {
            log.error("发短信失败", e);
        }
    }

	@Override
	@Transactional
	public OrderDto insertOrder(OrderDto orderDto) {
		orderInfoDao.insert(orderDto.getOrderInfo());
		final Long parentOrderId = orderDto.getOrderInfo().getId();// 记录父订单ID
		for(SubOrder subOrder:orderDto.getSubOrderList()){
			subOrder.setParentOrderId(parentOrderId);
			subOrder.setReceiveTel(orderDto.getOrderInfo().getReceiveTel());
		}
		subOrderDao.batchInsert(orderDto.getSubOrderList());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderId", parentOrderId);
		List<SubOrder> subOrderList = subOrderDao.queryByParam(params);
		for(OrderItem orderItem:orderDto.getOrderItemList()){
			for(SubOrder subOrder:subOrderList){
				if(orderItem.getOrderCode().equals(subOrder.getOrderCode())){
					orderItem.setParentOrderId(parentOrderId);
					orderItem.setOrderId(subOrder.getId());
				}
			}
		}
		orderItemDao.batchInsert(orderDto.getOrderItemList());
		final List<OrderItem> orderItemList = orderItemDao.queryByParam(params);
		orderDto.setOrderPromotionList(new ArrayList<OrderPromotion>());
		orderDto.getOrderItemList().forEach(new Consumer<OrderItem>(){
			public void accept(OrderItem orderItem) {
				orderItemList.forEach(new Consumer<OrderItem>(){
					public void accept(OrderItem t) {
						if(t.getSkuCode().equals(orderItem.getSkuCode()) && t.getTopicId().equals(orderItem.getTopicId())){
							orderItem.setId(t.getId());
						}
					}
				});
				if(CollectionUtils.isNotEmpty(orderItem.getOrderPromotionList())){
					for(OrderPromotion orderPromotion:orderItem.getOrderPromotionList()){
						orderPromotion.setParentOrderId(parentOrderId);
						orderPromotion.setParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
						orderPromotion.setOrderCode(orderItem.getOrderCode());
						orderPromotion.setOrderId(orderItem.getOrderId());
						orderPromotion.setOrderItemId(orderItem.getId());
					}
					orderDto.getOrderPromotionList().addAll(orderItem.getOrderPromotionList());
				}
				if(CollectionUtils.isNotEmpty(orderItem.getOrderPointList())){
					orderItem.getOrderPointList().forEach(new Consumer<OrderPoint>(){
						public void accept(OrderPoint t) {
							t.setOrderCode(orderItem.getOrderCode());
							t.setOrderId(orderItem.getOrderId());
							t.setParentOrderId(parentOrderId);
							t.setParentOrderCode(orderItem.getParentOrderCode());
							t.setOrderItemId(orderItem.getId());
							t.setCreateUser(t.getCreateUser());
							orderPointDao.insert(t);
						}
					});
				}
			}
		});
		orderItemList.clear();
		orderDto.getOrderConsignee().setParentOrderId(parentOrderId);
		if(orderDto.getOrderConsignee().getConsigneeId()!=0){
			orderConsigneeDao.insert(orderDto.getOrderConsignee());
		}
	
		if(CollectionUtils.isNotEmpty(orderDto.getOrderPromotionList())){
			orderPromotionDao.batchInsert(orderDto.getOrderPromotionList());
		}
		if (orderDto.getOrderReceipt() != null) {// 有发票信息
			orderDto.getOrderReceipt().setParentOrderId(parentOrderId);
			orderReceiptDao.insert(orderDto.getOrderReceipt());
		}
		MemRealinfo memRealinfo = orderDto.getMemRealinfo();
		if(memRealinfo!=null){
			memRealinfo.setParentOrderId(parentOrderId);
			memRealinfoDao.insert(memRealinfo);
		}
		if (CollectionUtils.isNotEmpty(orderDto.getTopicLimitItemList())) {
			for(TopicLimitItem topicLimitItem:orderDto.getTopicLimitItemList()){
				topicLimitItemDao.saveLimit(topicLimitItem);
			}
		}
		orderStatusLogDao.batchInsert(orderDto.getOrderStatusLogList());
		if(orderDto.getOrderChannelTrack()!=null){
			OrderChannelTrack orderChannelTrack = orderDto.getOrderChannelTrack(); 
			orderChannelTrack.setParentOrderId(parentOrderId);
			orderChannelTrack.setParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
			orderChannelTrackDao.insert(orderChannelTrack);
		}
		return orderDto;
	}
	
	/**
	 * 开通分销
	 */
	public void passPromoter(SubOrder subOrder){
		if(subOrder.getPromoterId()!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("memberId", subOrder.getMemberId());
			PromoterInfo promoterInfo = promoterInfoService.queryUniqueByParams(params);
			if(null!=promoterInfo && DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())
			&& DssConstant.PROMOTER_STATUS.UN_PASS.code.equals(promoterInfo.getPromoterStatus())){
				PromoterInfo updatePromoter = new PromoterInfo();
				updatePromoter.setPromoterId(promoterInfo.getPromoterId());
				updatePromoter.setPromoterStatus(DssConstant.PROMOTER_STATUS.EN_PASS.code);
				updatePromoter.setUpdateUser("【分销员】"+promoterInfo.getPromoterId());
				promoterInfoService.updateNotNullById(updatePromoter);
			}
		}
	}
}
