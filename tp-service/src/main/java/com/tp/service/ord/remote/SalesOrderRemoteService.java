package com.tp.service.ord.remote;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.formatToPrice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tp.common.util.OrderUtils;
import com.tp.common.util.mem.Sms;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.OrderStatusLogConstant;
import com.tp.common.vo.ord.SalesOrderConstant.OrderIsDeleted;
import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.dto.ord.remote.OrderCountDTO;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.dto.ord.remote.SubOrder4CouponDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.ord.Kuaidi100Express;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.ReceiptDetail;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.ExpressLogInfoDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.IKuaidi100ExpressService;
import com.tp.service.ord.IMemRealinfoService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.IOrderDeliveryService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderPromotionService;
import com.tp.service.ord.IOrderReceiptService;
import com.tp.service.ord.IOrderService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IReceiptDetailService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.IRejectItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.OrderCodeUserVerification;
import com.tp.service.ord.OrderQueryServiceHelper;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 订单远程服务实现
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class SalesOrderRemoteService implements ISalesOrderRemoteService {
	private static final Logger logger = LoggerFactory.getLogger(SalesOrderRemoteService.class);
	@Value("#{meta['order.delivery.success.message']}")
	private String SMSCONTENT_DELIVERY_TEMPLATE = "亲爱的会员，您所购买的商品已发出！由于现海关申报流程调整，近期澳洲直邮的商品预计需15天到货，造成不便，敬请谅解";
	/** 批量处理列表次数 */
	private static final int BATCH_DEAL_TIMES = 50;
	/** 开放平台批量处理列表次数 */
	private static final int PLATFORM_BATCH_DEAL_TIMES = 100;
	// 支付途径
	private static final Map<Long, String> PAY_WAY_MAP = new ConcurrentHashMap<Long, String>();
	
	private static final Map<Integer,List<PaymentGateway>> PAY_WAY_CHANNEL_MAP = new ConcurrentHashMap<Integer,List<PaymentGateway>>();

	@Autowired
	private IOrderInfoService orderInfoService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private IOrderReceiptService orderReceiptService;
	@Autowired
	private IOrderConsigneeService orderConsigneeService;
	@Autowired
	private IOrderDeliveryService orderDeliveryService;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	@Autowired
	private IOrderPromotionService orderPromotionService;
	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IKuaidi100ExpressService kuaidi100ExpressService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IRejectInfoService rejectInfoService;
	@Autowired
	private IRejectItemService rejectItemService;
	@Autowired
	private IMemRealinfoService memRealinfoService;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;
	@Autowired
	private IReceiptDetailService receiptDetailService;
    @Autowired
    private OrderCodeUserVerification orderCodeUserVerification ;
    @Autowired
	private IPromoterInfoService promoterInfoService;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;
	@Autowired
	private IInventoryOperService inventoryOperService;

	
	@Override
	public PageInfo<OrderList4UserDTO> findOrderList4UserPage(SubOrderQO query) {
		Assert.notNull(query);
		Assert.notNull(query.getMemberId()); // 用户ID必填
		return findOrderList4UserPageByQO(query);
	}

	@Override
	public PageInfo<OrderList4UserDTO> findRejectOrderList4UserPage(SubOrderQO query) {
		processReject(query);
		return findOrderList4UserPageByQO(query);
	}
	
	// 处理退货QO
	private void processReject(SubOrderQO qo) {
		/* 订单完成后，30天内可以退货 */
		Calendar doneBeginTime = Calendar.getInstance();
		doneBeginTime.add(Calendar.DAY_OF_MONTH, -30);
		qo.setDoneBeginTime(doneBeginTime.getTime());
	}
	
	@Override
    public ResultInfo<Integer> getTotalCustomersForPromoterByQO(SubOrderQO qo){
		qo.setDeleted(OrderIsDeleted.NO.code);
    	Integer count = subOrderService.getTotalCustomersForPormoter(qo);
    	return new ResultInfo<Integer>(count);
    }
	
	// 根据QO查询订单分页列表
	public PageInfo<OrderList4UserDTO> findOrderList4UserPageByQO(SubOrderQO qo) {
		/* 子订单 */
		qo.setDeleted(OrderIsDeleted.NO.code);
		PageInfo<SubOrder> subPage = subOrderService.selectPageByQO(qo);
		List<SubOrder> subList = subPage.getRows();
		setPayWayStrOfSubOrder(subList);	// 设置支付途径
		Map<Long, List<SubOrder>> subMap = OrderQueryServiceHelper.toSubMap_orderId(subList);

		/* 父订单 */
		List<Long> orderIdList = OrderQueryServiceHelper.extractOrderIdList(subList);
		List<OrderInfo> orderList = orderInfoService.selectListByIdList(orderIdList);
		setPayWayStrOfSalesOrder(orderList);	// 设置支付途径

		/* 未付款或已取消，显示子订单 */
		List<Long> notPayOrderIdList = new ArrayList<Long>(); // 未支付
		List<Long> payOrderIdList = new ArrayList<Long>(); // 已支付
		if(CollectionUtils.isNotEmpty(subList)){
			for (SubOrder order : subList) {
				/**if (!isPayed(order) && !OrderUtils.isSeaOrder(subMap.get(order.getParentOrderId()).get(0).getType())) { // 未付款且不是海淘单
					notPayOrderIdList.add(order.getId());
				} else {*/ // 已付款
					payOrderIdList.add(order.getId());
				//}
			}
		}
		/* 订单行 */
		List<OrderItem> lineListY = orderItemService.selectListByOrderIdList(payOrderIdList);
		Map<Long, List<OrderItem>> lineMapY = OrderQueryServiceHelper.toLineMap_subId(lineListY);
		List<OrderItem> lineListN = orderItemService.selectListByOrderIdList(notPayOrderIdList);
		Map<Long, List<OrderItem>> lineMapN = OrderQueryServiceHelper.toLineMapParentorderId(lineListN);

		/* 收货人 */
		List<OrderConsignee> consigneeList = orderConsigneeService.selectListByOrderIdList(orderIdList);
		Map<Long, OrderConsignee> consigneeMap = OrderQueryServiceHelper.toConsigneeMap_orderId(consigneeList);

		/* 组装 */
		List<OrderList4UserDTO> dtoList = new ArrayList<OrderList4UserDTO>();
		for (OrderInfo order : orderList) {
			/**if (!isPayed(order) && !OrderUtils.isSeaOrder(subMap.get(order.getId()).get(0).getType())) { // 未付款且不是海淘单
				OrderList4UserDTO dto = new OrderList4UserDTO();
				dto.setOrderInfo(order);
				dto.setOrderItemList(lineMapN.get(order.getId()));
				dto.setOrderConsignee(consigneeMap.get(order.getId()));
				dto.setIsParent(true);
				dtoList.add(dto);
			} else {*/ // 已付款
				List<SubOrder> innerSubList = subMap.get(order.getId());
				if (CollectionUtils.isNotEmpty(innerSubList)) {
					for (SubOrder sub : innerSubList) {
						if (OrderIsDeleted.NO.code.equals(sub.getDeleted())) { // 非逻辑删除
							OrderList4UserDTO dto = new OrderList4UserDTO();
							dto.setOrderInfo(order);
							dto.setSubOrder(sub);
							dto.setOrderItemList(lineMapY.get(sub.getId()));
							dto.setOrderConsignee(consigneeMap.get(order.getId()));
							dto.setIsParent(false);
							dtoList.add(dto);
						}
					}
				}
			//}
		}

		PageInfo<OrderList4UserDTO> page = new PageInfo<OrderList4UserDTO>();
		page.setRows(dtoList);
		page.setSize(subPage.getSize());
		page.setPage(subPage.getPage());
		page.setRecords(subPage.getRecords());
		return page;
	}
	
	@Override
	public PageInfo<OrderList4UserDTO> findOrderList4UserPageByQO_coupons_shop_scan(SubOrderQO qo) {
		/* 子订单 */
		qo.setDeleted(OrderIsDeleted.NO.code);
//		PageInfo<SubOrder> subPage = subOrderService.selectPageByQO(qo);
		PageInfo<SubOrder> subPage = subOrderService.selectPageByQO_coupons_shop_scan(qo);
		List<SubOrder> subList = subPage.getRows();
		setPayWayStrOfSubOrder(subList);	// 设置支付途径
		Map<Long, List<SubOrder>> subMap = OrderQueryServiceHelper.toSubMap_orderId(subList);

		/* 父订单 */
		List<Long> orderIdList = OrderQueryServiceHelper.extractOrderIdList(subList);
		List<OrderInfo> orderList = orderInfoService.selectListByIdList(orderIdList);
		setPayWayStrOfSalesOrder(orderList);	// 设置支付途径

		/* 未付款或已取消，显示子订单 */
		List<Long> notPayOrderIdList = new ArrayList<Long>(); // 未支付
		List<Long> payOrderIdList = new ArrayList<Long>(); // 已支付
		if(CollectionUtils.isNotEmpty(subList)){
			for (SubOrder order : subList) {
				/**if (!isPayed(order) && !OrderUtils.isSeaOrder(subMap.get(order.getParentOrderId()).get(0).getType())) { // 未付款且不是海淘单
					notPayOrderIdList.add(order.getId());
				} else {*/ // 已付款
					payOrderIdList.add(order.getId());
				//}
			}
		}
		/* 订单行 */
		List<OrderItem> lineListY = orderItemService.selectListByOrderIdList(payOrderIdList);
		Map<Long, List<OrderItem>> lineMapY = OrderQueryServiceHelper.toLineMap_subId(lineListY);
		List<OrderItem> lineListN = orderItemService.selectListByOrderIdList(notPayOrderIdList);
		Map<Long, List<OrderItem>> lineMapN = OrderQueryServiceHelper.toLineMapParentorderId(lineListN);

		/* 收货人 */
		List<OrderConsignee> consigneeList = orderConsigneeService.selectListByOrderIdList(orderIdList);
		Map<Long, OrderConsignee> consigneeMap = OrderQueryServiceHelper.toConsigneeMap_orderId(consigneeList);

		/* 组装 */
		List<OrderList4UserDTO> dtoList = new ArrayList<OrderList4UserDTO>();
		for (OrderInfo order : orderList) {
			/**if (!isPayed(order) && !OrderUtils.isSeaOrder(subMap.get(order.getId()).get(0).getType())) { // 未付款且不是海淘单
				OrderList4UserDTO dto = new OrderList4UserDTO();
				dto.setOrderInfo(order);
				dto.setOrderItemList(lineMapN.get(order.getId()));
				dto.setOrderConsignee(consigneeMap.get(order.getId()));
				dto.setIsParent(true);
				dtoList.add(dto);
			} else {*/ // 已付款
				List<SubOrder> innerSubList = subMap.get(order.getId());
				if (CollectionUtils.isNotEmpty(innerSubList)) {
					for (SubOrder sub : innerSubList) {
						if (OrderIsDeleted.NO.code.equals(sub.getDeleted())) { // 非逻辑删除
							OrderList4UserDTO dto = new OrderList4UserDTO();
							dto.setOrderInfo(order);
							dto.setSubOrder(sub);
							dto.setOrderItemList(lineMapY.get(sub.getId()));
							dto.setOrderConsignee(consigneeMap.get(order.getId()));
							dto.setIsParent(false);
							dtoList.add(dto);
						}
					}
				}
			//}
		}

		PageInfo<OrderList4UserDTO> page = new PageInfo<OrderList4UserDTO>();
		page.setRows(dtoList);
		page.setSize(subPage.getSize());
		page.setPage(subPage.getPage());
		page.setRecords(subPage.getRecords());
		page.setTotalMoney(subPage.getTotalMoney());//订单总金额
		return page;
	}
	
	// 是否已支付
	private boolean isPayed(OrderInfo order) {
		return StringUtils.isNotBlank(order.getPayCode());
	}
	private boolean isPayed(SubOrder order) {
		return StringUtils.isNotBlank(order.getPayCode());
	}
	@Override
	public OrderDetails4UserDTO findOrderDetails4User(Long memberId, Long code) {
		Assert.notNull(memberId);
		OrderDetails4UserDTO dto = null;
		if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) { // 父订单号
			dto = findOrderDetails4UserByOrderCode(memberId, code);
		} else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) { // 子订单号
			dto = findOrderDetails4UserBySubCode(memberId, code);
		} else {
			throw new OrderServiceException("无效的订单编号[" + code + "]");
		}
		return dto;
	}

	@Override
	public PageInfo<SubOrder4BackendDTO> findSubOrder4BackendPage(SubOrderQO query) {
		/* 子订单 */
		PageInfo<SubOrder> subPage = null;
		subPage = subOrderService.selectPageByQO(query);	
		
		if(subPage==null || CollectionUtils.isEmpty(subPage.getRows())){
			return new PageInfo<SubOrder4BackendDTO>();
		}
		List<SubOrder> subList = subPage.getRows();
		setPayWayStrOfSubOrder(subList);	// 设置支付途径

		/* 父订单 */
		List<Long> orderIdList = OrderQueryServiceHelper.extractOrderIdList(subList);
		List<OrderInfo> orderList = orderInfoService.selectListByIdList(orderIdList);
		setPayWayStrOfOrder(orderList);	// 设置支付途径

		/* 订单行 */
		List<OrderItem> lineList = orderItemService.selectListByParentOrderIdList(orderIdList);

		/* 物流信息 */
		List<Long> subCodeList = OrderQueryServiceHelper.extractSubCodeList(subList);
		List<OrderDelivery> deliveryList = orderDeliveryService.selectListBySubCodeList(subCodeList);

		/* 收货人 */
		List<OrderConsignee> consigneeList = orderConsigneeService.selectListByOrderIdList(orderIdList);

		/* 发票 */
		List<OrderReceipt> receiptList = orderReceiptService.selectListByOrderIdList(orderIdList);
		
		/* 会员实名 */
		List<Long> orderCodeList = OrderQueryServiceHelper.extractParentOrderCodeList(subList);
		List<MemRealinfo> realinfoList = memRealinfoService.selectListByOrderCodeList(orderCodeList);

		/* 组装 SubOrder4BackendDTO */
		List<SubOrder4BackendDTO> list = assembleSubOrder4BackendList(orderList, subList, lineList, deliveryList, consigneeList, realinfoList, receiptList);

		PageInfo<SubOrder4BackendDTO> page = new PageInfo<SubOrder4BackendDTO>();
		page.setRows(list);
		page.setSize(subPage.getSize());
		page.setPage(subPage.getPage());
		page.setRecords(subPage.getRecords());
		return page;
	}

	@Override
	public SubOrder4BackendDTO findSubOrder4BackendByCode(Long code) {
		SubOrder4BackendDTO dto = null;
		if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) { // 父订单号
			dto = findSubOrder4BackendByOrderCode(code);
		} else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) { // 子订单号
			dto = findSubOrder4BackendBySubCode(code);
		} else {
			throw new IllegalArgumentException("无效的订单编号[" + code + "]");
		}
		return dto;
	}
	
	/**
	 * 根据父订单号查询
	 * 
	 * @param code
	 * @return
	 */
	private SubOrder4BackendDTO findSubOrder4BackendByOrderCode(Long code) {
		OrderInfo order = orderInfoService.selectOneByCode(code);
		setPayWayStr(order);	// 设置支付途径
		if (null == order) { // 订单号无效
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}

		List<OrderItem> orderItemList = orderItemService.selectListByOrderId(order.getId());
		OrderReceipt orderReceipt = orderReceiptService.selectOneByOrderId(order.getId());
		OrderConsignee orderConsignee = orderConsigneeService.selectOneByOrderId(order.getId());

		SubOrder4BackendDTO dto = new SubOrder4BackendDTO();
		dto.setOrder(order);
		dto.setOrderItemList(orderItemList);
		dto.setOrderConsignee(orderConsignee);
		dto.setOrderReceipt(orderReceipt);
		return dto;
	}


	/**
	 * 根据子订单号查询
	 * 
	 * @param code
	 * @return
	 */
	private SubOrder4BackendDTO findSubOrder4BackendBySubCode(Long code) {
		SubOrder sub = subOrderService.selectOneByCode(code);
		setPayWayStr(sub);	// 设置支付途径

		if (null == sub) { // 订单号无效
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}
		
		if(OrderConstant.ORDER_STATUS.PAYMENT.code<sub.getOrderStatus()
		 && OrderConstant.OrderType.FAST.code.equals(sub.getType())){
			Long overTime = new Date().getTime()-sub.getPayTime().getTime();
			sub.setOverTime(0-(overTime/(1000*60)-2*60));
		}
		OrderInfo order = orderInfoService.selectOneByCode(sub.getParentOrderCode());
		setPayWayStr(order);	// 设置支付途径
		List<OrderItem> lineList = orderItemService.selectListBySubId(sub.getId());
		OrderReceipt receipt = orderReceiptService.selectOneByOrderId(sub.getParentOrderId());
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(sub.getParentOrderId());
		OrderDelivery delivery = orderDeliveryService.selectOneBySubOrderCode(sub.getOrderCode());
		MemRealinfo realinfo = memRealinfoService.selectOneByOrderCode(order.getParentOrderCode());
		ReceiptDetail receiptDetail = receiptDetailService.selectListBySubOrderCode(sub.getParentOrderCode());
		
		SubOrder4BackendDTO dto = new SubOrder4BackendDTO();
		dto.setOrder(order);
		dto.setSubOrder(sub);
		dto.setOrderItemList(lineList);
		dto.setOrderConsignee(consignee);
		dto.setOrderReceipt(receipt);
		dto.setOrderDelivery(delivery);
		dto.setMemRealinfo(realinfo);
		dto.setReceiptDetail(receiptDetail);

		return dto;
	}

	/**
	 * 根据子订单号和用户查询
	 * 
	 * @param memberId
	 * @param code
	 * @return
	 */
	private OrderDetails4UserDTO findOrderDetails4UserBySubCode(Long memberId, Long code) {
		SubOrder sub = subOrderService.selectOneByCode(code);
		setPayWayStr(sub);	// 设置支付途径
		if (null == sub || !sub.getMemberId().equals(memberId) || OrderIsDeleted.YES.code.equals(sub.getDeleted())) { // 订单号无效
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}

		OrderInfo orderInfo = orderInfoService.selectOneByCode(sub.getOrderCode());
		setPayWayStr(orderInfo);	// 设置支付途径
		List<OrderItem> orderItemList = orderItemService.selectListBySubId(sub.getId());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", code);
		for(OrderItem orderItem:orderItemList){
			if(orderItem.getRefundStatus()!=null){
				params.put("orderItemId", orderItem.getId());
				Integer count = rejectItemService.queryByParamCount(params);
				orderItem.setRejectCount(count);
			}
		}
		
		OrderReceipt receipt = orderReceiptService.selectOneByOrderId(sub.getParentOrderId());
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(sub.getParentOrderId());
		OrderDelivery delivery = orderDeliveryService.selectOneBySubOrderCode(sub.getOrderCode());
		OrderDetails4UserDTO dto = new OrderDetails4UserDTO();
		dto.setIsParent(false);
		dto.setOrderInfo(orderInfo);
		dto.setSubOrder(sub);
		dto.setOrderItemList(orderItemList);
		dto.setOrderConsignee(consignee);
		dto.setOrderReceipt(receipt);
		dto.setOrderDelivery(delivery);

		return dto;
	}

	/**
	 * 根据父订单号和用户ID查询
	 * 
	 * @param code
	 * @return
	 */
	private OrderDetails4UserDTO findOrderDetails4UserByOrderCode(Long memberId, Long code) {
		OrderInfo order = orderInfoService.selectOneByCode(code);
		setPayWayStr(order);	// 设置支付途径
		if (null == order || !order.getMemberId().equals(memberId) || OrderIsDeleted.YES.code.equals(order.getDeleted())) { // 订单号无效
			throw new OrderServiceException("订单编号不存在");
		}

		List<OrderItem> lineList = orderItemService.selectListByOrderId(order.getId());
		OrderReceipt receipt = orderReceiptService.selectOneByOrderId(order.getId());
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(order.getId());

		OrderDetails4UserDTO dto = new OrderDetails4UserDTO();
		dto.setIsParent(true);
		dto.setOrderInfo(order);
		dto.setOrderItemList(lineList);
		dto.setOrderConsignee(consignee);
		dto.setOrderReceipt(receipt);

		return dto;
	}

	/**
	 * 批量组装 SubOrder4BackendDTO
	 * 
	 * @param orderList
	 *            父订单
	 * @param subList
	 *            子订单
	 * @param lineList
	 *            订单行
	 * @return
	 */
	private List<SubOrder4BackendDTO> assembleSubOrder4BackendList(List<OrderInfo> orderList, List<SubOrder> subList, List<OrderItem> lineList,
			List<OrderDelivery> deliveryList, List<OrderConsignee> consigneeList, List<MemRealinfo> realinfoList, List<OrderReceipt> receiptList) {
		if (CollectionUtils.isNotEmpty(subList)) {
			List<SubOrder4BackendDTO> list = new ArrayList<SubOrder4BackendDTO>();
			Map<Long, OrderInfo> orderMap = OrderQueryServiceHelper.toOrderMap_id(orderList); // 父订单
			Map<Long, List<OrderItem>> lineMap = OrderQueryServiceHelper.toLineMap_subId(lineList); // 订单行
			Map<Long, OrderDelivery> deliveryMap = OrderQueryServiceHelper.toDeliveryMap_subCode(deliveryList); // 物流信息
			Map<Long, OrderConsignee> consigneeMap = OrderQueryServiceHelper.toConsigneeMap_orderId(consigneeList); // 收货人
			Map<Long, MemRealinfo> realinfoMap = OrderQueryServiceHelper.toRealinfoMap_orderCode(realinfoList); // 会员实名
			Map<Long, OrderReceipt> receiptMap = OrderQueryServiceHelper.toReceiptMap_orderId(receiptList); // 发票
			for (SubOrder sub : subList) {
				SubOrder4BackendDTO dto = new SubOrder4BackendDTO();
				dto.setOrder(orderMap.get(sub.getParentOrderId()));
				dto.setSubOrder(sub);
				dto.setOrderItemList(lineMap.get(sub.getId()));
				dto.setOrderDelivery(deliveryMap.get(sub.getOrderCode()));
				dto.setOrderConsignee(consigneeMap.get(sub.getParentOrderId()));
				dto.setMemRealinfo(realinfoMap.get(sub.getParentOrderCode()));
				dto.setOrderReceipt(receiptMap.get(sub.getParentOrderId()));
				list.add(dto);
			}
			return list;
		}
		return new ArrayList<SubOrder4BackendDTO>(0);
	}

	@Override
	@Transactional
	public synchronized void operateOrderForDeliver(OrderDelivery orderDelivery) {
		Assert.notNull(orderDelivery, "订单发货实体类不可为空");
		SubOrder subOrder = subOrderService.selectOneByCode(orderDelivery.getOrderCode());
		
		String packageNos = orderDelivery.getPackageNo();
		if (subOrder == null) {
			logger.info("根据订单编号查询的订单不存在，订单编号为{} ", orderDelivery.getOrderCode());
			//throw new OrderServiceException(OrderErrorCodes.SUB_ORDER_NOT_EXIST, "该订单不存在");
			return;
		}else if(OrderConstant.FAST_ORDER_TYPE.equals(subOrder.getType())){
			logger.info("订单类型是速购，订单号 {} ", orderDelivery.getOrderCode());
			return;
		}else if (StringUtils.isBlank(packageNos)) {
			//throw new OrderServiceException(OrderErrorCodes.ORDER_DELIVER_PACKAGE_CODE_IS_NULL, "运单号为空");
			logger.error("运单号为空{}",orderDelivery.getOrderCode());
			return;
		} else if (StringUtils.isBlank(orderDelivery.getCompanyId()) || StringUtils.isBlank(orderDelivery.getCompanyName())) {
			logger.error("物流公司信息为空{}",orderDelivery.getOrderCode());
			//throw new OrderServiceException(OrderErrorCodes.ORDER_DELIVER_LOGISTICS_COMPANY_INFO_IS_NULL, "物流公司信息为空");
			return;
		} else if (!OrderConstant.ORDER_STATUS.DELIVERY.code.equals(subOrder.getOrderStatus())) {// 订单状态不为待发货
			logger.info("更新订单状态为发货时，订单状态为：{}，订单编号为：{} ", OrderConstant.ORDER_STATUS.getCnName(subOrder.getOrderStatus()), orderDelivery.getOrderCode());
			return;
		} else {
			// 保存发货信息
			List<OrderDelivery> orderDeliveryDOList = buildOrderDelivery(orderDelivery, subOrder);
			orderDeliveryService.batchInsert(orderDeliveryDOList);

			// 更新子订单状态
			SubOrder updateSubOrder = buildUpdateSubOrder(subOrder, OrderConstant.ORDER_STATUS.RECEIPT.code);
			updateSubOrder.setDeliveredTime(orderDelivery.getDeliveryTime()==null?new Date():orderDelivery.getDeliveryTime());
			subOrderService.updateSubOrderStatus(updateSubOrder);

			// 添加订单状态日志信息
			OrderStatusLog orderStatusLog = buildOrderStatusLog(subOrder, OrderConstant.ORDER_STATUS.RECEIPT.code);
			orderStatusLog.setCreateUserId(subOrder.getMemberId());
			orderStatusLogService.insert(orderStatusLog);

			// 发送消息
			try {
				String companyName = StringUtils.isEmpty(orderDelivery.getCompanyName()) ? orderDelivery.getCompanyId() : orderDelivery.getCompanyName();
				String smsContent = MessageFormat.format(SMSCONTENT_DELIVERY_TEMPLATE, subOrder.getOrderCode().toString(), companyName, orderDelivery.getPackageNo());
				String channelName = promoterInfoService.queryShortNameByChannelCode(subOrder.getChannelCode());
				if(StringUtil.isNoneBlank(channelName)){
					smsContent = "【"+ channelName +"】"+smsContent;
				}
				OrderConsignee orderConsignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
				sendSmsService.sendSms(orderConsignee.getMobile(), smsContent,null);
			} catch (Exception e) {
				logger.error("发送发货信息异常", e);
			}
			List<SubOrder> subOrderList = new ArrayList<SubOrder>();
			subOrderList.add(subOrder);
			sendMqMessageByDelivery(subOrderList);
		}
	}

	@Override
	@Transactional
	public int deleteByCode(Long memberId, Long code) {
		Assert.notNull(memberId);

		if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) { // 父订单号
			return orderInfoService.deleteByCode(code, memberId);
		} else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) { // 子订单号
			return subOrderService.deleteByCode(code, memberId);
		} else {
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}
	}

	@Override
	public SubOrder findSubOrderByCode(Long code) {
		SubOrder sub = subOrderService.selectOneByCode(code);
		setPayWayStr(sub);	// 设置支付途径
		return sub;
	}

	@Override
	public void operateOrderForReceiveGoods(OrderReceiveGoodsDTO orderReceiveGoodsDTO) {
		Assert.notNull(orderReceiveGoodsDTO, "订单收货实体类不可为空");
		SubOrder subOrder = subOrderService.selectOneByCode(orderReceiveGoodsDTO.getSubOrderCode());
		if (subOrder == null) {
			logger.info("根据订单编号查询的订单不存在，订单编号为: " + orderReceiveGoodsDTO.getSubOrderCode());
			throw new OrderServiceException(OrderErrorCodes.SUB_ORDER_NOT_EXIST, "该订单不存在");
		} else if (!OrderConstant.ORDER_STATUS.RECEIPT.code.equals(subOrder.getOrderStatus())) {// 订单状态不为待收货
			logger.info("更新订单状态为发货时，订单状态为：{}，订单编号为：{} ", OrderConstant.ORDER_STATUS.getCnName(subOrder.getOrderStatus()), orderReceiveGoodsDTO.getSubOrderCode());
			throw new OrderServiceException(OrderErrorCodes.CHECK_ORDER_STATUS_ERROR, "订单状态错误");
		} else {
			// 更新子订单状态
			SubOrder updateSubOrder = buildUpdateSubOrder(subOrder, OrderConstant.ORDER_STATUS.FINISH.code);
			updateSubOrder.setDoneTime(new Date());
			subOrderService.updateSubOrderStatus(updateSubOrder);

			// 添加订单状态日志信息

			OrderStatusLog orderStatusLog = buildOrderStatusLog(subOrder, OrderConstant.ORDER_STATUS.FINISH.code);
			Date receiveTime = orderReceiveGoodsDTO.getReceiptDate() == null ? new Date() : orderReceiveGoodsDTO.getReceiptDate();
			orderStatusLog.setContent("收货：" + DateUtil.format(receiveTime, DateUtil.NEW_FORMAT));

			orderStatusLogService.insert(orderStatusLog);
			try {
				subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.FINISH.code);
				subOrder.setDoneTime(receiveTime);
				rabbitMqProducer.sendP2PMessage(MqMessageConstant.RECEIVE_GOODS_SUCCESS, subOrder);
			} catch (MqClientException e) {
				logger.error("收货成功后发送消息失败:{}",e);
			}
		}
	}

	   @Override
    public void operateOrderForReceiveGoodsByUser(Long memberID, OrderReceiveGoodsDTO orderReceiveGoodsDTO) {
        Assert.notNull(orderReceiveGoodsDTO, "订单收货实体类不可为空");
        Boolean b = orderCodeUserVerification.verifyCodeUser(memberID, orderReceiveGoodsDTO.getSubOrderCode());
        if (b) {
            this.operateOrderForReceiveGoods(orderReceiveGoodsDTO);
        }
    }
	
	@Override
	public ResultOrderDeliverDTO operateOrderListForDeliver(List<OrderDelivery> orderDeliverList) {
		Assert.notEmpty(orderDeliverList, "处理发货参数列表不可为空");
		ResultOrderDeliverDTO resultOrderDelivery = new ResultOrderDeliverDTO();
		// 参数基本校验
		List<OrderDelivery> usableList = new ArrayList<OrderDelivery>();// 处理数据
		List<OrderDelivery> errorList = new ArrayList<OrderDelivery>();// 处理失败数据
		List<OrderOperatorErrorDTO> orderOperatorErrorList = baseCheckParamForOrderDeliver(orderDeliverList, usableList, errorList);// 错误描述

		int totalSize = usableList.size();
		if (totalSize == 0) {
			resultOrderDelivery.setErrorDataList(errorList);
			resultOrderDelivery.setOrderOperatorErrorList(orderOperatorErrorList);
			return resultOrderDelivery;
		}
		int batchs = totalSize / BATCH_DEAL_TIMES;
		List<OrderDelivery> tmpOrderDeliverList = null;
		List<OrderDelivery> tmpErrorList = null;
		for (int i = 0; i <= batchs; i++) {
			int tmpEndIndex = (i + 1) * BATCH_DEAL_TIMES > totalSize ? (totalSize) : ((i + 1) * BATCH_DEAL_TIMES);
			tmpOrderDeliverList = usableList.subList(i * BATCH_DEAL_TIMES, tmpEndIndex);
			List<OrderOperatorErrorDTO> tmpOrderOperatorErrorDTOs;
			tmpErrorList = new ArrayList<OrderDelivery>();
			try {
				tmpOrderOperatorErrorDTOs = dealListForDeliver(tmpOrderDeliverList, tmpErrorList);
			} catch (Exception e) {
				logger.error("处理订单发货数据入库异常", e);
				tmpErrorList.addAll(tmpOrderDeliverList);
				tmpOrderOperatorErrorDTOs = new ArrayList<OrderOperatorErrorDTO>();
				for (OrderDelivery errorDealOrderDeliver : tmpErrorList) {
					tmpOrderOperatorErrorDTOs.add(new OrderOperatorErrorDTO(errorDealOrderDeliver.getOrderCode(), OrderErrorCodes.SYSTEM_ERROR, "系统处理错误"));
				}
			}
			errorList.addAll(tmpErrorList);
			orderOperatorErrorList.addAll(tmpOrderOperatorErrorDTOs);
		}
		resultOrderDelivery.setOrderOperatorErrorList(orderOperatorErrorList);
		resultOrderDelivery.setErrorDataList(errorList);
		return resultOrderDelivery;
	}

	@Override
	public List<OrderStatusLog> findStatusLogListByCode(Long code) {
		OrderStatusLog log = new OrderStatusLog();
		if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) { // 父订单号
			log.setParentOrderCode(code);
			OrderInfo order = orderInfoService.selectOneByCode(code);

			if (null == order) {
				throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
			}
			return orderStatusLogService.queryByObject(log);

		} else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) { // 子订单号
			log.setOrderCode(code);
			SubOrder sub = subOrderService.selectOneByCode(code);
			if (null == sub) {
				throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
			}
			return orderStatusLogService.queryByObject(log);
		} else {
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public SubOrder4CouponDTO findSubOrderCouponByCouponId(Long couponId) {
		Assert.notNull(couponId);

		List<OrderPromotion> couponList = orderPromotionService.selectListByCouponCode(couponId.toString());

		if (CollectionUtils.isEmpty(couponList))
			return null;

		List<Long> subIdList = OrderQueryServiceHelper.extractSubIdListFromPromotion(couponList);
		List<SubOrder> subList = subOrderService.selectListByIdList(subIdList);
		setPayWayStrOfSubOrder(subList);

		if (CollectionUtils.isEmpty(subList))
			return null;

		Map<Long, Double> subId_discount_map = new HashMap<Long, Double>();
		for (OrderPromotion coupon : couponList) {
			Double discount = subId_discount_map.get(coupon.getOrderId());
			if (null == discount)
				discount = 0D;
			Double totalDiscount = add(discount, coupon.getDiscount()).doubleValue();
			subId_discount_map.put(coupon.getOrderId(), totalDiscount);
		}

		Map<SubOrder, Double> subDiscoutnMap = new HashMap<SubOrder, Double>();
		Map<Long, SubOrder> subMap = OrderQueryServiceHelper.toSubMap_id(subList);
		for (Entry<Long, Double> entry : subId_discount_map.entrySet()) {
			subDiscoutnMap.put(subMap.get(entry.getKey()), subId_discount_map.get(entry.getKey()));
		}

		SubOrder4CouponDTO dto = new SubOrder4CouponDTO();
		OrderPromotion coupon = couponList.get(0);
		dto.setCouponAmount(coupon.getCouponFaceAmount());
		dto.setCouponCode(coupon.getCouponCode());
		dto.setSubMap(subDiscoutnMap);
		return dto;
	}

	/**
	 * 
	 * <pre>
	 * 参数基础校验  
	 * 批量处理 发货后的订单操作
	 * </pre>
	 * 
	 * @param originalList
	 *            原始参数列表
	 * @param usableList
	 *            可以使用的参数列表
	 * @param errorList
	 *            错误的参数列表
	 * @return 校验失败处理错误信息
	 */
	private List<OrderOperatorErrorDTO> baseCheckParamForOrderDeliver(List<OrderDelivery> originalList, List<OrderDelivery> usableList,
			List<OrderDelivery> errorList) {
		List<OrderOperatorErrorDTO> orderOperatorErrorList = new ArrayList<OrderOperatorErrorDTO>();
		Long subCode = null;
		Map<Long, OrderDelivery> tmpMap = new HashMap<Long, OrderDelivery>();
		List<Long> repeatOrderCodeList = new ArrayList<Long>();
		for (OrderDelivery orderDeliverDTO : originalList) {
			if (orderDeliverDTO != null && null!=orderDeliverDTO.getOrderCode()) {
				subCode = orderDeliverDTO.getOrderCode();
				if (tmpMap.containsKey(subCode)) {// 订单编号重复出现
					repeatOrderCodeList.add(subCode);
					errorList.add(orderDeliverDTO);
					errorList.add(tmpMap.remove(subCode));
					orderOperatorErrorList.add(new OrderOperatorErrorDTO(subCode, OrderErrorCodes.ORDER_DELIVER_SUB_ORDER_CODE_EXIST, "订单编号重复存在"));
				} else if (repeatOrderCodeList.contains(subCode)) {
					errorList.add(orderDeliverDTO);
				} else if (StringUtils.isBlank(orderDeliverDTO.getPackageNo())) {// 运单号为空
					errorList.add(orderDeliverDTO);
					orderOperatorErrorList.add(new OrderOperatorErrorDTO(subCode, OrderErrorCodes.ORDER_DELIVER_PACKAGE_CODE_IS_NULL, "运单号为空"));
				} else if (StringUtils.isBlank(orderDeliverDTO.getCompanyId()) || StringUtils.isBlank(orderDeliverDTO.getCompanyName())) {
					errorList.add(orderDeliverDTO);
					orderOperatorErrorList.add(new OrderOperatorErrorDTO(subCode, OrderErrorCodes.ORDER_DELIVER_LOGISTICS_COMPANY_INFO_IS_NULL, "物流公司信息为空"));
				} else {// 可用
					tmpMap.put(subCode, orderDeliverDTO);
				}
			}
		}
		usableList.addAll(tmpMap.values());
		return orderOperatorErrorList;
	}

	/**
	 * 
	 * <pre>
	 * 处理发货流程
	 * </pre>
	 * 
	 * @param dealOrderDeliverList
	 * @param errorList
	 * @return
	 */
	@Transactional
	private List<OrderOperatorErrorDTO> dealListForDeliver(List<OrderDelivery> dealOrderDeliverList, List<OrderDelivery> errorList) {

 		List<OrderOperatorErrorDTO> orderOperatorErrorList = new ArrayList<OrderOperatorErrorDTO>();
		Map<Long, OrderDelivery> subCodeMap = buildSubCodeMap(dealOrderDeliverList);
		Set<Long> subCodeSet = subCodeMap.keySet();
		List<SubOrder> subOrderDOList = subOrderService.selectListByCodeList(new ArrayList<>(subCodeSet));

		// 处理数据的集合初始化
		List<OrderDelivery> dbOrderDeliveryList = new ArrayList<OrderDelivery>();// 入库的订单物流状态信息列表数据
		List<SubOrder> dbSubOrderList = new ArrayList<SubOrder>();// 更新数据库子订单信息
		List<OrderStatusLog> dbOrderStatusLogList = new ArrayList<OrderStatusLog>();// 入库的订单状态变更日志对象列表
		List<Long> orderIdList = new ArrayList<Long>();// 订单ID列表，用于查询收货人电话
		Map<Long, String> smsContentMap = new HashMap<Long, String>();// 短信发送内容信息；key：订单ID，value：短信发送内容

		if (CollectionUtils.isNotEmpty(subOrderDOList)) {
			for (SubOrder subOrder : subOrderDOList) {
				if (OrderConstant.ORDER_STATUS.DELIVERY.code.equals(subOrder.getOrderStatus())) {// 有效的真实需要处理的数据
					OrderDelivery tmpOrderDelivery = subCodeMap.remove(subOrder.getOrderCode());

					// 构建入库数据对象列表
					List<OrderDelivery> orderDeliveryDOList = buildOrderDelivery(tmpOrderDelivery, subOrder);
					dbOrderDeliveryList.addAll(orderDeliveryDOList);
					SubOrder updateSubOrder = buildUpdateSubOrder(subOrder, OrderConstant.ORDER_STATUS.RECEIPT.code);
					updateSubOrder.setDeliveredTime(tmpOrderDelivery.getDeliveryTime() == null ? new Date() : tmpOrderDelivery.getDeliveryTime());
					dbSubOrderList.add(updateSubOrder);
					OrderStatusLog orderStatusLog = buildOrderStatusLog(subOrder, OrderConstant.ORDER_STATUS.RECEIPT.code);
					dbOrderStatusLogList.add(orderStatusLog);
					orderIdList.add(subOrder.getParentOrderId());
					String companyName = StringUtils.isEmpty(tmpOrderDelivery.getCompanyName()) ? tmpOrderDelivery.getCompanyId() : tmpOrderDelivery
							.getCompanyName();
					String smsContent = MessageFormat
							.format(SMSCONTENT_DELIVERY_TEMPLATE, subOrder.getOrderCode().toString(), companyName, tmpOrderDelivery.getPackageNo());
					String shortName = promoterInfoService.queryShortNameByChannelCode(subOrder.getChannelCode());
					if(StringUtil.isNoneBlank(shortName)){
						smsContent = "【"+ shortName +"】"+smsContent;
					}
					smsContentMap.put(subOrder.getParentOrderId(), smsContent);

				} else {
					errorList.add(subCodeMap.remove(subOrder.getOrderCode()));// 状态不为待发货

					orderOperatorErrorList.add(new OrderOperatorErrorDTO(subOrder.getOrderCode(), OrderErrorCodes.CHECK_ORDER_STATUS_ERROR, "订单状态不为[待发货]状态，当前状态为："
							+ OrderConstant.ORDER_STATUS.getCnName(subOrder.getOrderStatus())));
				}
			}
		}
		if (MapUtils.isNotEmpty(subCodeMap)) {
			for (OrderDelivery errorOrderDeliver : subCodeMap.values()) {
				errorList.add(errorOrderDeliver);
				orderOperatorErrorList.add(new OrderOperatorErrorDTO(errorOrderDeliver.getOrderCode(), OrderErrorCodes.INVALID_ORDER_CODE, "该订单编号的订单不存在"));
			}
		}

		// 批量入库操作
		dealOrderDeliverParamInDB(dbOrderDeliveryList, dbSubOrderList, dbOrderStatusLogList);
		// 操作库存
		for (OrderDelivery orderDelivery : dbOrderDeliveryList) {
			exWarehouseService(orderDelivery);
		}
		// 发送短信
		try {
			if (CollectionUtils.isNotEmpty(orderIdList) && MapUtils.isNotEmpty(smsContentMap)) {
				List<OrderConsignee> orderConsigneeDOs = orderConsigneeService.selectListByOrderIdList(orderIdList);// 订单收货人信息列表
				List<Sms> smsList = new ArrayList<Sms>();
				Sms sms = null;
				for (OrderConsignee orderConsignee : orderConsigneeDOs) {
					sms = new Sms(orderConsignee.getMobile(), smsContentMap.get(orderConsignee.getParentOrderId()));
					
					smsList.add(sms);
				}
				sendSmsService.batchSendSms(smsList);
			}
		} catch (Exception e) {
			logger.error("批量发货时发送短信异常", e);
		}
		sendMqMessageByDelivery(subOrderDOList);
		return orderOperatorErrorList;
	}
	/**
	 * 订单强制发货：更新库存
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void exWarehouseService(OrderDelivery orderDelivery){
		try{
			inventoryOperService.reduceInventoryForOrderDelivery(orderDelivery.getOrderCode());
		}catch(Exception e){
			logger.error("出库失败 > {} ",e.getMessage());
		}
	}

	/**
	 * 
	 * <pre>
	 * 批量入库操作
	 * </pre>
	 * 
	 * @param orderDeliveryDOList
	 * @param subOrderDOList
	 * @param orderStatusLogDOList
	 */
	private void dealOrderDeliverParamInDB(List<OrderDelivery> orderDeliveryDOList, List<SubOrder> subOrderDOList,
			List<OrderStatusLog> orderStatusLogDOList) {
		if (CollectionUtils.isNotEmpty(orderDeliveryDOList)) {// 插入订单发货信息
			orderDeliveryService.batchInsert(orderDeliveryDOList);
		}
		if (CollectionUtils.isNotEmpty(subOrderDOList)) {// 更新订单发货状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderStatus", OrderConstant.ORDER_STATUS.RECEIPT.code);
			map.put("subOrderList", subOrderDOList);
			map.put("deliveredTime", new Date());
			subOrderService.batchUpdateSubOrderStatusByCode(map);
		}
		if (CollectionUtils.isNotEmpty(orderStatusLogDOList)) {// 添加订单状态日志信息
			orderStatusLogService.batchInsert(orderStatusLogDOList);
		}
	}

	/**
	 * 
	 * <pre>
	 * 构建发货信息入库实体
	 * </pre>
	 * 
	 * @param orderDeliver
	 * @param subOrderDO
	 * @return
	 */
	private List<OrderDelivery> buildOrderDelivery(OrderDelivery orderDeliver, SubOrder subOrder) {
		OrderDelivery orderDelivery = null;
		List<OrderDelivery> list = new ArrayList<OrderDelivery>();
		String packageNos = orderDeliver.getPackageNo();
		// 如果
		String endCityString = orderDeliver.getEndCity();
		if (StringUtils.isBlank(endCityString)) {
			OrderConsignee orderConsignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
			if (orderConsignee != null) {
				endCityString = orderConsignee.getProvinceName() + orderConsignee.getCityName();
			}
		}
		if (StringUtils.isNotBlank(packageNos)) {
			String[] packageNoArray = packageNos.split(",");
			for (String packageNo : packageNoArray) {
				if (StringUtils.isNotBlank(packageNo)) {
					orderDelivery = new OrderDelivery();
					orderDelivery.setCompanyId(orderDeliver.getCompanyId());
					orderDelivery.setCompanyName(orderDeliver.getCompanyName());
					orderDelivery.setOrderCode(subOrder.getOrderCode());
					orderDelivery.setOrderCode(subOrder.getOrderCode());
					orderDelivery.setPackageNo(packageNo);
					Date deliveryTime = orderDeliver.getDeliveryTime() == null ? new Date() : orderDeliver.getDeliveryTime();
					orderDelivery.setDeliveryTime(deliveryTime);
					orderDelivery.setLinkInfo(orderDeliver.getLinkInfo());
					orderDelivery.setStartCity(orderDeliver.getStartCity());
					orderDelivery.setEndCity(orderDeliver.getEndCity());
					orderDelivery.setRefundInfo(null);
					orderDelivery.setSupplierId(subOrder.getSupplierId());
					orderDelivery.setSupplierName(subOrder.getSupplierName());
					orderDelivery.setFreight(orderDeliver.getFreight());
					orderDelivery.setWeight(orderDeliver.getWeight());
					orderDelivery.setCreateTime(new Date());
					orderDelivery.setCreateUser(orderDeliver.getCreateUser());
					orderDelivery.setUpdateTime(new Date());
					orderDelivery.setUpdateUser(orderDeliver.getCreateUser());
					list.add(orderDelivery);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * <pre>
	 *  构建 更新子订单状态入库实体
	 * </pre>
	 * 
	 * @param subOrderDO
	 * @param status
	 *            状态
	 * @return
	 */
	private SubOrder buildUpdateSubOrder(SubOrder subOrder, Integer status) {
		SubOrder updateSubOrder = new SubOrder();
		updateSubOrder.setId(subOrder.getId());
		updateSubOrder.setOrderCode(subOrder.getOrderCode());
		updateSubOrder.setOrderStatus(status);
		updateSubOrder.setUpdateTime(new Date());
		return updateSubOrder;
	}

	/**
	 * 
	 * <pre>
	 * 添加订单状态日志信息入库实体
	 * </pre>
	 * 
	 * @param subOrderDO
	 * @param status
	 * @return
	 */
	private OrderStatusLog buildOrderStatusLog(SubOrder subOrder, Integer status) {
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setName(OrderConstant.ORDER_STATUS.RECEIPT.cnName);
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
		orderStatusLog.setType(OrderStatusLogConstant.LOG_TYPE.TRACKING.code);
		orderStatusLog.setName(OrderConstant.ORDER_STATUS.getCnName(status));
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(status);
		orderStatusLog.setContent("子订单状态操作：" + OrderConstant.ORDER_STATUS.getCnName(status));
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName(Constant.AUTHOR_TYPE.SYSTEM);
		orderStatusLog.setCreateUserType(null);
		return orderStatusLog;
	}

	/**
	 * 
	 * <pre>
	 * 构建子订单编号列表
	 * </pre>
	 * 
	 * @param orderDeliverList
	 * @return Map<String, OrderDelivery> key:子订单编号
	 */
	private Map<Long, OrderDelivery> buildSubCodeMap(List<OrderDelivery> orderDeliverList) {
		Map<Long, OrderDelivery> subCodeMap = new HashMap<Long, OrderDelivery>();
		if (CollectionUtils.isNotEmpty(orderDeliverList)) {
			for (OrderDelivery orderDeliverDTO : orderDeliverList) {
				if (null!=orderDeliverDTO.getOrderCode()) {
					subCodeMap.put(orderDeliverDTO.getOrderCode(), orderDeliverDTO);
				}

			}
		}
		return subCodeMap;
	}

	/**
	 * 根据父订单编号获取支付ID号
	 * 
	 * @param orderNo
	 * @return
	 */
	public ResultInfo<Long> queryOrderPaymentInfoByOrderNo(Long orderNo) {
		if (null==orderNo) {
			return new ResultInfo<Long>(new FailInfo("订单编号不存在",OrderErrorCodes.INVALID_ORDER_CODE));
		}
		OrderInfo salesOrder = orderInfoService.selectOneByCode(orderNo);
		if (null == salesOrder) {
			SubOrder subOrder = subOrderService.selectOneByCode(orderNo);
			if(null == subOrder){
				return new ResultInfo<Long>(new FailInfo("订单编号不存在 ",OrderErrorCodes.INVALID_ORDER_CODE));
			}
			if (OrderConstant.ORDER_STATUS.PAYMENT.code.intValue() != subOrder.getOrderStatus()) {
				return new ResultInfo<Long>(new FailInfo(OrderErrorCodes.PAYMENT_ERROR_CODE.ALREADY_PAY.cnName,OrderErrorCodes.PAYMENT_ERROR_CODE.ALREADY_PAY.code));
			}
		}else if (OrderConstant.ORDER_STATUS.PAYMENT.code.intValue() != salesOrder.getOrderStatus()) {
			return new ResultInfo<Long>(new FailInfo(OrderErrorCodes.PAYMENT_ERROR_CODE.ALREADY_PAY.cnName,OrderErrorCodes.PAYMENT_ERROR_CODE.ALREADY_PAY.code));
		}
		PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderNo);
		if (paymentInfo == null || paymentInfo.getPaymentId() == null) {
			logger.error("根据订单编号{}查找支付信息为空！ ", orderNo);
			return new ResultInfo<Long>(new FailInfo(OrderErrorCodes.PAYMENT_ERROR_CODE.INFO_EMPTY.cnName,OrderErrorCodes.PAYMENT_ERROR_CODE.INFO_EMPTY.code));
		}
		if (paymentInfo.getStatus() != null && PaymentConstant.PAYMENT_STATUS.PAYED.code.intValue() == paymentInfo.getStatus()) {
			return new ResultInfo<Long>(new FailInfo(OrderErrorCodes.PAYMENT_ERROR_CODE.STATUS_ERROR.cnName,OrderErrorCodes.PAYMENT_ERROR_CODE.STATUS_ERROR.code));
		}
		return new ResultInfo<Long>(paymentInfo.getPaymentId());
	}

	@Override
	public List<OrderInfo> querySalesOrderByUnPayIsExpired(int minute) {
		return orderInfoService.querySalesOrderByUnPayIsExpired(minute);
	}
	@Override
	public List<SubOrder> querySubOrderBySeaOrderUnPayIsExpired(int minute){
		return subOrderService.querySubOrderBySeaOrderUnPayIsExpired(minute);
	}
	@Override
	public List<SubOrderExpressInfoDTO> queryExpressLogInfo(Long code, String expressNo) {
		Assert.notNull(code, "子订单编号不可为空");
		String packageNo = StringUtil.isEmpty(expressNo) ? null : expressNo.trim();
		List<Kuaidi100Express> expressLogInfoList = new ArrayList<Kuaidi100Express>();
		Map<String, OrderDelivery> orderDeliveryMap = new HashMap<String, OrderDelivery>();
		Map<String, RejectInfo> rejectInfoMap = new HashMap<String, RejectInfo>();
		List<SubOrderExpressInfoDTO> list = new ArrayList<SubOrderExpressInfoDTO>();
		if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) {// 子订单
			List<OrderDelivery> orderDeliveryDOList = orderDeliveryService.selectListBySubCodeAndPackageNo(code, packageNo);
			if (CollectionUtils.isNotEmpty(orderDeliveryDOList)) {
				List<Kuaidi100Express> kuaidi100ExpressDOList = new ArrayList<Kuaidi100Express>();
				Kuaidi100Express kuaidi100Express = null;
				for (OrderDelivery orderDelivery : orderDeliveryDOList) {
					orderDeliveryMap.put(orderDelivery.getPackageNo(), orderDelivery);// 构建订单物流信息map
					kuaidi100Express = new Kuaidi100Express();
					kuaidi100Express.setOrderCode(orderDelivery.getOrderCode());
					kuaidi100Express.setPackageNo(StringUtil.isEmpty(orderDelivery.getPackageNo())?null:orderDelivery.getPackageNo().trim());
					kuaidi100ExpressDOList.add(kuaidi100Express);// 组织物流日志记录查询参数列表O
				}
				expressLogInfoList = kuaidi100ExpressService.selectListBySubOrderCodeAndPackageNo(kuaidi100ExpressDOList);
			}
		} else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {// 退货
			List<RejectInfo> rejectInfoDOList = rejectInfoService.selectListByRejectNoAndPackageNo(code, packageNo);
			if (CollectionUtils.isNotEmpty(rejectInfoDOList)) {
				List<Kuaidi100Express> kuaidi100ExpressDOList = new ArrayList<Kuaidi100Express>();
				Kuaidi100Express kuaidi100Express = null;
				for (RejectInfo rejectInfo : rejectInfoDOList) {
					rejectInfoMap.put(rejectInfo.getExpressNo(), rejectInfo);// 构建订单物流信息map
					kuaidi100Express = new Kuaidi100Express();
					kuaidi100Express.setRejectCode(rejectInfo.getRejectCode());// 退货单号
					kuaidi100Express.setPackageNo(StringUtil.isEmpty(rejectInfo.getPackageNo())?null:rejectInfo.getPackageNo().trim());
					kuaidi100ExpressDOList.add(kuaidi100Express);// 组织物流日志记录查询参数列表O
				}
				expressLogInfoList = kuaidi100ExpressService.selectListByRejectNoAndPackageNo(kuaidi100ExpressDOList);
			}
		}
		// 组织具体的日志信息
		Map<String, ArrayList<ExpressLogInfoDTO>> expressLogInfoMap = new HashMap<String, ArrayList<ExpressLogInfoDTO>>();
		for (Kuaidi100Express tmpKuaidi100Express : expressLogInfoList) {
			ArrayList<ExpressLogInfoDTO> list2 = expressLogInfoMap.get(tmpKuaidi100Express.getPackageNo());
			if (CollectionUtils.isEmpty(list2)) {
				list2 = new ArrayList<ExpressLogInfoDTO>();
			}
			ExpressLogInfoDTO eliDTO = new ExpressLogInfoDTO();
			String dataTime = StringUtils.isNotBlank(tmpKuaidi100Express.getDataFtime())
					? tmpKuaidi100Express.getDataFtime() : tmpKuaidi100Express.getDataTime();
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

		return list;
	}

	/**
	 * 查询等待系统确认收货的订单
	 * @param receivedDays
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<SubOrder> querySubOrderPageByReceived(int receivedDays,int startPage,int pageSize){
		Date deliverEndTime = DateUtils.addDays(DateUtils.truncate(new Date(),Calendar.DATE), -receivedDays);
		SubOrderQO query = new SubOrderQO();
		query.setDeliverEndTime(deliverEndTime);
		query.setStartPage(startPage);
		query.setOrderStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
		query.setPageSize(pageSize);
		PageInfo<SubOrder> subPage = subOrderService.selectPageByQO(query);
		return subPage;
	}
	
	@Override
	public List<OrderInfo> querySalesOrderByUnPayOverFifteenMinutes(int minute) {
		return orderInfoService.querySalesOrderByUnPayOverFifteenMinutes(minute);
	}

	@Override
	public List<SubOrder> querySubOrderByWaitPutSeaWashes(Map<String, Object> map) {
		return subOrderService.querySubOrderByWaitPutSeaWashes(map);
	}

	@Override
	public boolean putWareHouseShippingBySubOrder(SubOrder subOrder) {
		return orderService.putWareHouseShippingBySubOrder(subOrder);
	}

	@Override
	public boolean putWareHouseShippingBySeaSubOrder(SubOrder subOrder){
		return orderService.putWareHouseShippingBySeaSubOrder(subOrder);
	}
	
	@Override
	public List<OrderPromotion> findPromotionListByOrderCode(Long orderCode) {
		List<OrderPromotion> promotionList = null;
		if (OrderUtils.isOrderCode(orderCode)) {	// 父单
			promotionList = orderPromotionService.selectListByOrderCode(orderCode);
		} else if (OrderUtils.isSubOrderCode(orderCode)) {	// 子单
			promotionList = orderPromotionService.selectListBySubCode(orderCode);
		} else {
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}
		
		return promotionList;
	}
	
	@Override
	public List<OrderPromotion> findCouponListByOrderCode(Long orderCode) {
		List<OrderPromotion> promotionList = findPromotionListByOrderCode(orderCode);
		return extractCouponList(promotionList);	// 抽取优惠券列表
	}

	// 抽取优惠券列表
	private List<OrderPromotion> extractCouponList(List<OrderPromotion> promotionList) {
		if (CollectionUtils.isNotEmpty(promotionList)) {
			/* 一个订单可能有多条促销信息， */
			Map<String, OrderPromotion> couponMap = new HashMap<String, OrderPromotion>();
			for (OrderPromotion pro : promotionList) {
				if (StringUtils.isNotBlank(pro.getCouponCode())) {
					OrderPromotion coupon = couponMap.get(pro.getCouponCode());
					if (null == coupon) {
						coupon = new OrderPromotion();
						coupon.setCouponCode(pro.getCouponCode());
						coupon.setCouponFaceAmount(pro.getCouponFaceAmount());
						coupon.setDiscount(pro.getDiscount());
						coupon.setCouponType(pro.getCouponType());
						couponMap.put(pro.getCouponCode(), coupon);
					} else {
						coupon.setDiscount(pro.getDiscount() + coupon.getDiscount());
					}
				}
			}
			return new ArrayList<OrderPromotion>(couponMap.values());
		}
		return Collections.emptyList();
	}
	
	private static final String COUPON_TYPE_0 = "优惠券";
	private static final String COUPON_TYPE_1 = "红包";
	
	// 获取优惠券类型中文文本
	private String getCouponTypeStr(Integer couponType) {
		if (null != couponType) {
			return CouponType.HAS_CONDITION.ordinal() == couponType ? COUPON_TYPE_0 : COUPON_TYPE_1;
		}
		return null;
	}
	
	private void setPayWayStr(OrderInfo orderInfo) {
		if (null != orderInfo && null != orderInfo.getPayWay()) {
			orderInfo.setPayWayStr(getPayWayMap().get(orderInfo.getPayWay().longValue()));
			orderInfo.setPayWayCodeListString(getChannelPayWayList(null,null));
		}
	}
	// 设置支付途径中文字符串
	private void setPayWayStrOfSubOrder(List<SubOrder> subList) {
		if (CollectionUtils.isNotEmpty(subList)) {
			for (SubOrder sub : subList) {
				setPayWayStr(sub);
			}
		}
	}

	// 设置支付途径中文字符串
	private void setPayWayStr(SubOrder order) {
		if (null != order && null != order.getPayWay()) {
			order.setPayWayStr(getPayWayMap().get(order.getPayWay().longValue()));
			order.setPayWayCodeListString(getChannelPayWayList(order.getType().longValue(),order.getSeaChannel()));
		}
	}

	// 设置支付途径中文字符串
	private void setPayWayStrOfOrder(List<OrderInfo> orderList) {
		if (CollectionUtils.isNotEmpty(orderList)) {
			for (OrderInfo order : orderList) {
				setPayWayStr(order);
			}
		}
	}	
	// 获取支付途径map
	private Map<Long, String> getPayWayMap() {
		if (PAY_WAY_MAP.isEmpty()) {
			List<PaymentGateway> payList = paymentGatewayService.selectAllOrderbyParentId();
			if (CollectionUtils.isNotEmpty(payList)) {
				for (PaymentGateway pay : payList) {
					if (null != pay && null != pay.getGatewayId() && null != pay.getGatewayName()) {
						PAY_WAY_MAP.put(pay.getGatewayId(), pay.getGatewayName());
					}
				}
			} else {
				logger.error("获取支付途径列表失败");
			}
		}
		return PAY_WAY_MAP;
	}

	private String getChannelPayWayList(Long orderType, Long channelId){
		List<PaymentGateway> availableGateways = paymentGatewayService.queryPaymentGateWayLists(orderType, channelId);
		String paywayChannelStr = "";
		if(CollectionUtils.isNotEmpty(availableGateways)){
			for(PaymentGateway paymentGateway:availableGateways){
				paywayChannelStr=paywayChannelStr+SPLIT_SIGN.COMMA+paymentGateway.getGatewayCode();
			}
		}
		return paywayChannelStr.replaceFirst(SPLIT_SIGN.COMMA, "");
	}

	@Override
	public List<SubOrder> querySubOrderToFisherAfterPayThirtyMinutes(Map<String, Object> inputArgument) {
		// TO Auto-generated method stub
		return subOrderService.querySubOrderToFisherAfterPayThirtyMinutes(inputArgument);
	}

	/**
	 * 查询订单对应的收货地址信息
	 * 
	 * @param orderCode
	 * @return OrderConsignee
	 */
	@Override
	public OrderConsignee queryOrderConsigneeInfoByOrderCode(Long code) {
		return orderConsigneeService. selectOneByOrderCode(code);
	}

	/**
	 * 查询订单对应的用户实名认证信息
	 * 
	 * @param orderCode
	 * @return MemRealinfoDO
	 */
	@Override
	public MemRealinfo getOrderMemRealinfoByOrderCode(Long orderCode) {
		return memRealinfoService.selectOneByOrderCode(orderCode);
	}
	/**
	 * 查询子订单对应的商品行信息
	 * 
	 * @param orderId
	 * @return OrderItem
	 */
	@Override
	public List<OrderItem> getOrderLInesInfoBySubOrderId(Long orderId) {
		// TO Auto-generated method stub
		return orderItemService.selectListBySubId(orderId);
	}


	@Override
	public OrderCountDTO findOrderCountDTOByMemberId(long memberId,List<Integer> orderTypeList,String channelCode) {
		int paymentCount = getPaymentCount(memberId,orderTypeList,channelCode);
		List<Integer> receptionCountTypeList  =new ArrayList<Integer>();
		for( Integer  type :orderTypeList){
			if(type!=8){
				receptionCountTypeList.add(type);
			}
		}
		int receptionCount = getReceptionCount(memberId,receptionCountTypeList,channelCode);
		
		int afterSaleCount = getAfterSaleCount(memberId,receptionCountTypeList,channelCode);
		List<Integer> unuseCountTypeList  =new ArrayList<Integer>();
		unuseCountTypeList.add(8);
	    int unuseCount=getReceptionCount(memberId,unuseCountTypeList,channelCode);	
		OrderCountDTO count = new OrderCountDTO();
		count.setPayment(paymentCount);
		count.setReception(receptionCount);
		count.setAfterSale(afterSaleCount);
		count.setUnusecount(unuseCount);
		return count;
	}

	
	// 获取“未支付”订单总数
	// 普通单为父单，海淘单为子单
	private int getPaymentCount(long memberId,List<Integer> orderTypeList,String channelCode) {
		return subOrderService.selectPaymentCount(memberId,orderTypeList,channelCode);
	}

	// 获取“待收货”订单总数
	private int getReceptionCount(long memberId,List<Integer> orderTypeList,String channelCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put("orderStatus", ORDER_STATUS.RECEIPT.code);
		params.put("deleted", OrderIsDeleted.NO.code);
		if(StringUtils.isNotBlank(channelCode)){
			params.put("channelCode",channelCode);
		}
		if(CollectionUtils.isNotEmpty(orderTypeList)){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " type in ("+StringUtil.join(orderTypeList, SPLIT_SIGN.COMMA)+")");
		}
		return subOrderService.queryByParamCount(params);
	}
	// 获取“待收货”订单总数
		private int getUnUseCount(long memberId,List<Integer> orderTypeList,String channelCode) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("memberId", memberId);
			params.put("orderStatus", ORDER_STATUS.UNUSE.code);
			params.put("deleted", OrderIsDeleted.NO.code);
			if(StringUtils.isNotBlank(channelCode)){
				params.put("channelCode",channelCode);
			}
			if(CollectionUtils.isNotEmpty(orderTypeList)){
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " type in ("+StringUtil.join(orderTypeList, SPLIT_SIGN.COMMA)+")");
			}
			return subOrderService.queryByParamCount(params);
		}

	// 获取售后订单数
	private int getAfterSaleCount(long memberId,List<Integer> orderTypeList,String channelCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", memberId);
		String subSql = "";
		if(CollectionUtils.isNotEmpty(orderTypeList)){
			subSql = " and o.type in ("+StringUtil.join(orderTypeList, SPLIT_SIGN.COMMA)+")";
		}
		if(StringUtils.isNotBlank(channelCode)){
			subSql +=" and o.channel_code = '"+channelCode+"'";
		}
		if(StringUtil.isNotBlank(subSql)){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " exists (select 1 from ord_sub_order o where o.order_code=RejectInfo.order_code "+subSql+")");
		}
		return rejectInfoService.queryByParamCount(params);
	}

	@Override
	public List<SubOrder> findSubOrderDTOListBySubCodeList(List<Long> subCodeList) {
		if (null != subCodeList && subCodeList.size() > PLATFORM_BATCH_DEAL_TIMES) {
			throw new OrderServiceException(OrderErrorCodes.OrderQueryError.COUNT_LIMIT.code, OrderErrorCodes.OrderQueryError.COUNT_LIMIT.cnName);
		}
		return subOrderService.selectListByCodeList(subCodeList);
	}

    @Override
    public List<SubOrderExpressInfoDTO> queryExpressLogInfoByUser(Long memberId, Long code, String packageNo) {
        Boolean b= orderCodeUserVerification.verifyCodeUser(memberId, code);
         if(b){
             return this.queryExpressLogInfo(code,packageNo);
         }
         return null;
    }
	
    @Override
    public List<SubOrder> queryUndeclaredSubOrders(Map<String, Object> map){
    	return subOrderService.queryUndeclaredSubOrders(map);
    }
    
    @Override
    public List<SubOrder> queryUnPutWaybillSubOrders(Map<String, Object> map){
    	return subOrderService.queryUnPutWaybillSubOrders(map);
    }
    
	// 设置支付途径中文字符串
	private void setPayWayStrOfSalesOrder(List<OrderInfo> orderList) {
		if (CollectionUtils.isNotEmpty(orderList)) {
			for (OrderInfo order : orderList) {
				setPayWayStr(order);
			}
		}
	}
	
	public void sendMqMessageByDelivery(List<SubOrder> subOrderList){
		if (CollectionUtils.isNotEmpty(subOrderList)) {
			List<Long> orderCodeList = new ArrayList<Long>();
			subOrderList.removeIf(new Predicate<SubOrder>(){
				@Override
				public boolean test(SubOrder subOrder) {
					return subOrder.getPromoterId()==null && subOrder.getShopPromoterId()==null && subOrder.getScanPromoterId()==null;
				}
			});
			if(CollectionUtils.isNotEmpty(subOrderList)){
				for (SubOrder subOrder : subOrderList) {
					orderCodeList.add(subOrder.getOrderCode());
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in ("+StringUtil.join(orderCodeList,SPLIT_SIGN.COMMA)+")");
				params.put("orderStatus", OrderConstant.ORDER_STATUS.RECEIPT.code);
				List<SubOrder> tempSubOrderList = subOrderService.queryByParam(params);
				if(CollectionUtils.isEmpty(tempSubOrderList)){
					return;
				}
				subOrderList.removeIf(new Predicate<SubOrder>(){
					@Override
					public boolean test(SubOrder subOrder) {
						Boolean notDelivery = Boolean.TRUE;
						for (SubOrder order : tempSubOrderList) {
							if(order.getOrderCode().equals(subOrder.getOrderCode())){
								notDelivery = Boolean.FALSE;
							}
						}
						return notDelivery;
					}
				});
				params.remove("orderStatus");
				params.put("couponType", CouponType.NO_CONDITION.ordinal());
				List<OrderPromotion> orderPromotionList = orderPromotionService.queryByParam(params);
				params.remove("couponType");
				List<OrderItem> orderItemList = orderItemService.queryByParam(params);
				
				Map<Long,Double> couponAmountMap = new HashMap<Long,Double>();
				if(CollectionUtils.isNotEmpty(orderPromotionList)){
					orderPromotionList.forEach(new Consumer<OrderPromotion>(){
						public void accept(OrderPromotion orderPromotion) {
							if(couponAmountMap.containsKey(orderPromotion.getOrderCode())){
								couponAmountMap.put(orderPromotion.getOrderCode(), formatToPrice(add(couponAmountMap.get(orderPromotion.getOrderCode()),orderPromotion.getDiscount())).doubleValue());
							}else{
								couponAmountMap.put(orderPromotion.getOrderCode(), orderPromotion.getDiscount());
							}
						}
					});
				}
				subOrderList.forEach(new Consumer<SubOrder>(){
					public void accept(SubOrder subOrder) {
						orderItemList.forEach(new Consumer<OrderItem>(){
							public void accept(OrderItem orderItem){
								if(subOrder.getOrderCode().equals(orderItem.getOrderCode())){
									subOrder.getOrderItemList().add(orderItem);
								}
							}
						});
						subOrder.setDiscount(0.00d);
						couponAmountMap.forEach(new BiConsumer<Long,Double>(){
							public void accept(Long t, Double u) {
								if(subOrder.getOrderCode().equals(t)){
									subOrder.setDiscount(u);
								}
							}
						});
						subOrder.setOrderStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
						try {
							rabbitMqProducer.sendP2PMessage(MqMessageConstant.ORDER_DELIVERY_SUCCESS, subOrder);
						} catch (MqClientException e) {
							logger.error("订单发货成功后发送消息失败\r\n{}", e);
						}
					}
				});
			}
			
		}
	}
}