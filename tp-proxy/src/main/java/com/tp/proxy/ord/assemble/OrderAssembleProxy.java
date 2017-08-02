package com.tp.proxy.ord.assemble;

import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.Constant.LOG_AUTHOR_TYPE;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.common.vo.ord.SubOrderConstant;
import com.tp.common.vo.ord.TopicLimitItemConstant.TopicLimitType;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.SimpleFullDiscountDTO;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;
import com.tp.proxy.mem.MemberDetailProxy;
import com.tp.proxy.pay.PaymentGatewayProxy;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.util.StringUtil;

/**
 * 根据拆分后的信息组装订单
 * 订单主信息
 * 订单信息
 * 订单商品项
 * 订单优惠信息
 * 订单发票
 * 订单收货人
 * 海淘凭证信息
 * 日志表
 * 活动限购订单商品统计表
 * @author szy
 *
 */
@Service
public class OrderAssembleProxy implements IOrderAssembleProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderAssembleProxy.class);
	
	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;
	@Autowired
	private MemberDetailProxy memberDetailProxy;
	@Autowired
	private PaymentGatewayProxy paymentGatewayProxy;

	@Override
	public OrderDto assembleOrder(OrderInitDto orderInitDto) {
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderInfo(initOrderInfo(orderInitDto));
		initMergePay(orderInitDto,orderDto.getOrderInfo());
		orderDto.setSubOrderList(initSubOrderList(orderInitDto,orderDto));
		orderDto.setOrderItemList(initOrderItemList(orderInitDto,orderDto));
		orderDto.setOrderPromotionList(initOrderPromotionList(orderInitDto,orderDto));
		orderDto.setOrderReceipt(initOrderReceipt(orderInitDto,orderDto));
		orderDto.setOrderConsignee(initOrderConsignee(orderInitDto,orderDto));
		orderDto.setOrderStatusLogList(initOrderStatusLog(orderInitDto,orderDto));
		orderDto.setTopicLimitItemList(initTopicLimitItem(orderInitDto,orderDto));
		orderDto.setPaymentInfoList(initPaymentInfoList(orderInitDto,orderDto));
		orderDto.setMemRealinfo(initMemRealinfo(orderInitDto,orderDto));
		orderDto.setOrderChannelTrack(initChannelTrack(orderInitDto,orderDto));
		return orderDto;
	}

	/**
	 * 组装父订单信息
	 * @param orderInitDto
	 * @return OrderInfo
	 */
	public OrderInfo initOrderInfo(OrderInitDto orderInitDto){
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setMemberId(orderInitDto.getMemberId());
		orderInfo.setPayType(OrderConstant.OrderPayType.ONLINE.code);
		orderInfo.setPayWay(orderInitDto.getPayWay());// 支付途径
		orderInfo.setAreaId(orderInitDto.getAreaId());// 大区ID
		orderInfo.setFreight(orderInitDto.getFreight());// 整单总运费
		orderInfo.setOriginalTotal(orderInitDto.getPayableAmount());// 整单应付总价（打折前）
		orderInfo.setTotal(orderInitDto.getSummation());// 实付总价（打折后）
		orderInfo.setItemTotal(orderInitDto.getActuallyAmount());
		orderInfo.setOrderStatus(ORDER_STATUS.PAYMENT.code);// 设置付款状态为：等待付款
		orderInfo.setQuantity(orderInitDto.getQuantityCount());// 订单总数量
		orderInfo.setBatchNum(orderInitDto.getToken());
		orderInfo.setIsReceipt(orderInitDto.getIsNeedInvoice());// 设置是否开具发票
		orderInfo.setDeleted(Constant.DELECTED.NO);// 逻辑删除 - 否
		orderInfo.setCreateUser(LOG_AUTHOR_TYPE.MEMBER+orderInitDto.memberAccount);
		orderInfo.setUpdateUser(orderInfo.getCreateUser());
		orderInfo.setParentOrderCode(orderCodeGeneratorService.generate(OrderCodeType.PARENT));// 设置支付流水号
		orderInfo.setRemark(orderInitDto.getOrderRemark());// 订单remark
		orderInfo.setBatchNum(orderInitDto.getToken());
		orderInfo.setSource(orderInitDto.getOrderSource());
		orderInfo.setAreaId(orderInitDto.getAreaId());
		orderInfo.setIp(orderInitDto.getIp());
		orderInfo.setFreight(orderInitDto.getFreight());
		orderInfo.setQuantity(orderInitDto.getQuantityCount());
		orderInfo.setTaxTotal(orderInitDto.getTaxes());
		orderInfo.setRemark(orderInitDto.getOrderRemark());
		orderInfo.setAccountName(orderInitDto.getMemberAccount());
		orderInfo.setDiscountTotal(orderInitDto.getDiscountTotal());
		orderInfo.setPromoterId(orderInitDto.getPromoterId());
		orderInfo.setShopPromoterId(orderInitDto.getShopPromoterId());
		orderInfo.setScanPromoterId(orderInitDto.getScanPromoterId());
		orderInfo.setChannelCode(orderInitDto.getChannelCode());
		orderInfo.setTpin(orderInitDto.getTpin());
		orderInfo.setUuid(orderInitDto.getUuid());
		orderInfo.setReceiveTel(orderInitDto.getReceiveTel());
		if(orderInitDto.getUsedPointSign()){
			orderInfo.setTotalPoint(orderInitDto.getUsedPoint());
		}
		return orderInfo;
	}
	
	/**
	 * 订单信息
	 * @param orderInitDto
	 * @return
	 */
	public List<SubOrder> initSubOrderList(OrderInitDto orderInitDto,OrderDto orderDto){
		MemberInfo memberInfo = orderInitDto.getMemberInfo();
		List<SubOrder> subOrderList = new ArrayList<SubOrder>();
		ConsigneeAddress consigneeAddress = orderInitDto.getConsigneeAddress();
		if(CollectionUtils.isNotEmpty(orderInitDto.getPreSubOrderList())){
			for(PreOrderDto preSubOrder:orderInitDto.getPreSubOrderList()){
				preSubOrder.setAccountName(orderDto.getOrderInfo().getAccountName());
				preSubOrder.setSource(orderDto.getOrderInfo().getSource());
				preSubOrder.setTrackSource(preSubOrder.getSource());
				preSubOrder.setRemark(orderDto.getOrderInfo().getRemark());
				preSubOrder.setConsigneeMobile(consigneeAddress.getMobile());
				preSubOrder.setConsigneeName(consigneeAddress.getName());
				preSubOrder.setGroupId(orderInitDto.getGroupId());
				preSubOrder.setPromoterId(orderInitDto.getPromoterId());
				preSubOrder.setShopPromoterId(orderInitDto.getShopPromoterId());
				preSubOrder.setScanPromoterId(orderInitDto.getScanPromoterId() );
				/**if(memberInfo!=null && memberInfo.getShopPromoterId()!=null){
					preSubOrder.setShopPromoterId(memberInfo.getShopPromoterId());
				}*/
				preSubOrder.setPayWay(orderInitDto.getPayWay());
				preSubOrder.setDeleted(SubOrderConstant.DELETED_FALSE);
				preSubOrder.setChannelCode(orderInitDto.getChannelCode());
				SubOrder subOrder = createSubOrderInfo(orderDto.getOrderInfo(),preSubOrder);
				for(Map.Entry<SimpleFullDiscountDTO, List<OrderItem>> entry:preSubOrder.getFullDiscountMap().entrySet()){
					for(OrderItem orderItem:entry.getValue()){
						createOrderItemInfo(orderItem,subOrder);
						orderItem.setIp(orderInitDto.getIp());
						orderItem.setPrice(toPrice(divide(orderItem.getItemAmount(),orderItem.getQuantity())));
					}
				}
				subOrderList.add(subOrder);
			}
		}
		return subOrderList;
	}
	
	/**
	 * 订单商品项
	 * @param orderInitDto
	 * @return
	 */
	public List<OrderItem> initOrderItemList(OrderInitDto orderInitDto,OrderDto orderDto){
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		List<SubOrder> subOrderList = orderDto.getSubOrderList();
		for(SubOrder subOrder:subOrderList){
			orderItemList.addAll(subOrder.getOrderItemList());
		}
		return orderItemList;
	}
	
	/**
	 * 订单优惠信息
	 * @param orderInitDto
	 * @return
	 */
	public List<OrderPromotion> initOrderPromotionList(OrderInitDto orderInitDto,OrderDto orderDto){
		List<OrderCouponDTO> orderCouponList = orderInitDto.getOrderCouponList();
		List<OrderPromotion> orderPromotionList = new ArrayList<OrderPromotion>();
		if(CollectionUtils.isNotEmpty(orderCouponList)){
			List<OrderItem> preOrderItem = orderInitDto.getOrderItemList();
			List<OrderItem> orderItemList = orderDto.getOrderItemList();
			preOrderItem.forEach(new Consumer<OrderItem>(){
				public void accept(OrderItem orderItem) {
					orderPromotionList.addAll(orderItem.getOrderPromotionList());
					orderItemList.forEach(new Consumer<OrderItem>(){
						public void accept(OrderItem t) {
							if(orderItem.getSkuCode().equals(t.getSkuCode()) && orderItem.getTopicId().equals(t.getTopicId())){
								t.setOrderPromotionList(orderItem.getOrderPromotionList());
							}
						}
					});
				}
			});
		}
		return orderPromotionList;
	}
	
	/**
	 * 订单发票
	 * @param orderInitDto
	 * @return
	 */
	public OrderReceipt initOrderReceipt(OrderInitDto orderInitDto,OrderDto orderDto){
		if(Constant.TF.YES.equals(orderInitDto.getIsNeedInvoice())){
			OrderReceipt orderReceipt = new OrderReceipt();
			orderReceipt.setTitle(orderInitDto.getInvoiceTitle());// 发票抬头
			orderReceipt.setType(orderInitDto.getInvoiceCarrier());// 发票类型（1：普通纸质。2：电子票。3：增值税发票）
			orderReceipt.setTitleType(orderInitDto.getInvoiceType());// 抬头类型（1.个人
			orderReceipt.setParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
			return orderReceipt;
		}
		return null;
	}
	
	/**
	 * 订单收货人
	 * @param orderInitDto
	 * @return
	 */
	public OrderConsignee initOrderConsignee(OrderInitDto orderInitDto,OrderDto orderDto){
		ConsigneeAddress consigneeAddress = orderInitDto.getConsigneeAddress();
		OrderConsignee orderConsignee = new OrderConsignee();
		// 设置收货地址信息
		orderConsignee.setName(consigneeAddress.getName());// 收货人姓名与身份证一致
		orderConsignee.setAddress(consigneeAddress.getAddress());
		orderConsignee.setProvinceId(consigneeAddress.getProvinceId());
		orderConsignee.setProvinceName(consigneeAddress.getProvince());
		orderConsignee.setCityId(consigneeAddress.getCityId());
		orderConsignee.setCityName(consigneeAddress.getCity());
		orderConsignee.setCountyId(consigneeAddress.getCountyId());
		orderConsignee.setCountyName(consigneeAddress.getCounty());
		orderConsignee.setTownId(consigneeAddress.getStreetId());
		orderConsignee.setTownName(consigneeAddress.getStreet());
		orderConsignee.setEmail(consigneeAddress.getEmail());
		orderConsignee.setMobile(consigneeAddress.getMobile());
		orderConsignee.setPostcode("020000");
		orderConsignee.setTelephone(consigneeAddress.getPhone());
		orderConsignee.setConsigneeId(consigneeAddress.getId());// 会员收货地址ID
		orderConsignee.setIdentityCard(consigneeAddress.getIdentityCard());
		orderConsignee.setParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
		return orderConsignee;
	}
	
	/**
	 * 日志表
	 * @param orderInitDto
	 * @return
	 */
	public List<OrderStatusLog> initOrderStatusLog(OrderInitDto orderInitDto,OrderDto orderDto){
		List<OrderStatusLog> orderStatusLogList = new ArrayList<OrderStatusLog>();
		// 插入父订单状态log
		OrderInfo orderInfo = orderDto.getOrderInfo();
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(orderInfo.getParentOrderCode());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);
		orderStatusLog.setContent("提交订单，等待支付");// 设置日志内容
		orderStatusLog.setCreateUserId(orderInfo.getMemberId());
		orderStatusLog.setCreateUserName(orderInfo.getAccountName());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLogList.add(orderStatusLog);
		List<SubOrder> subOrderList = orderDto.getSubOrderList();
		for(SubOrder subOrder:subOrderList){
			OrderStatusLog subOrderStatusLog = new OrderStatusLog();
			BeanUtils.copyProperties(orderStatusLog, subOrderStatusLog);
			subOrderStatusLog.setOrderCode(subOrder.getOrderCode());
			orderStatusLogList.add(subOrderStatusLog);
		}
		return orderStatusLogList;
	}
	
	/**
	 * 活动限购订单商品统计表
	 * @param orderInitDto
	 * @return
	 */
	public List<TopicLimitItem> initTopicLimitItem(OrderInitDto orderInitDto,OrderDto orderDto){
		ConsigneeAddress consigneeAddress = orderInitDto.getConsigneeAddress();
		List<OrderItem> cartItemInfoList = orderDto.getOrderItemList();
		// 组织限购数据
		List<TopicLimitItem> topicLimitItemList = new ArrayList<TopicLimitItem>();
		for (OrderItem orderLine : cartItemInfoList) {
			// 用户手机号限购
			TopicLimitItem topicLimitItem = new TopicLimitItem();
			topicLimitItem.setTopicId(orderLine.getTopicId());
			topicLimitItem.setSkuCode(orderLine.getSkuCode());
			topicLimitItem.setBuyedQuantity(orderLine.getQuantity());
			topicLimitItem.setLimitType(TopicLimitType.MOBILE.code);
			topicLimitItem.setLimitValue(consigneeAddress.getMobile());
			if(OrderConstant.OrderType.BUY_COUPONS.code.equals(orderLine.getSalesType())){
				topicLimitItem.setLimitValue(orderInitDto.getReceiveTel());
			}
			topicLimitItemList.add(topicLimitItem);
		}
		return topicLimitItemList;
	}
	
	/**
	 * 支付信息
	 * @param orderInitDto
	 * @param orderDto
	 * @return
	 */
	public List<PayPaymentSimpleDTO> initPaymentInfoList(OrderInitDto orderInitDto,OrderDto orderDto){
		List<PayPaymentSimpleDTO> paymentInfoList = new ArrayList<PayPaymentSimpleDTO>();
		OrderInfo orderInfo = orderDto.getOrderInfo();
		if(PaymentConstant.MERGE_PAY_TYPE.TRUE.code.equals(orderInfo.getMergePay())){
			PayPaymentSimpleDTO payPaymentSimpleDTO = new PayPaymentSimpleDTO();
			payPaymentSimpleDTO.setUserId(orderInfo.getMemberId());
			payPaymentSimpleDTO.setBizCode(orderInfo.getParentOrderCode());
			payPaymentSimpleDTO.setBizType(PaymentConstant.BIZ_TYPE.MERGEORDER.code);
			payPaymentSimpleDTO.setBizCreateTime(orderInfo.getCreateTime());
			payPaymentSimpleDTO.setAmount(orderInfo.getTotal());
			payPaymentSimpleDTO.setActionIP(orderInitDto.getIp());
			payPaymentSimpleDTO.setOrderType(-1L);	
			payPaymentSimpleDTO.setIdentityType("01");// 证件类型
			payPaymentSimpleDTO.setIdentityCode(orderDto.getOrderConsignee().getIdentityCard());// 证件号码
			payPaymentSimpleDTO.setRealName(orderDto.getOrderConsignee().getName());// 姓名
			payPaymentSimpleDTO.setTaxFee(orderInfo.getTaxTotal());// 税款
			payPaymentSimpleDTO.setFreight(orderInfo.getFreight());// 运费
			payPaymentSimpleDTO.setChannelId(-1L);// 海淘渠道id
			payPaymentSimpleDTO.setGatewayId(orderInfo.getPayWay());
			paymentInfoList.add(payPaymentSimpleDTO);
		}
		for (SubOrder subOrder : orderDto.getSubOrderList()) {
			PayPaymentSimpleDTO payPaymentSimpleDTO = new PayPaymentSimpleDTO();
			payPaymentSimpleDTO.setUserId(subOrder.getMemberId());
			payPaymentSimpleDTO.setBizCode(subOrder.getOrderCode());
			payPaymentSimpleDTO.setBizType(PaymentConstant.BIZ_TYPE.SUBORDER.code);
			payPaymentSimpleDTO.setBizCreateTime(subOrder.getCreateTime());
			payPaymentSimpleDTO.setAmount(subOrder.getTotal());
			payPaymentSimpleDTO.setActionIP(orderInitDto.getIp());
			payPaymentSimpleDTO.setOrderType(subOrder.getType().longValue());	
			payPaymentSimpleDTO.setIdentityType("01");// 证件类型
			payPaymentSimpleDTO.setIdentityCode(orderDto.getOrderConsignee().getIdentityCard());// 证件号码
			payPaymentSimpleDTO.setRealName(orderDto.getOrderConsignee().getName());// 姓名
			payPaymentSimpleDTO.setTaxFee(subOrder.getTaxFee());// 税款
			payPaymentSimpleDTO.setFreight(subOrder.getFreight());// 运费
			payPaymentSimpleDTO.setChannelId(subOrder.getSeaChannel());// 海淘渠道id
			payPaymentSimpleDTO.setGatewayId(subOrder.getPayWay());
			paymentInfoList.add(payPaymentSimpleDTO);
		}
		return paymentInfoList;
	}
	
	/**
	 * *
	 * 
	 * <pre>
	 * 设置子订单通用信息
	 * </pre>
	 * 
	 * @param salesOrderDO
	 * @param tempcartItemInfos
	 * @return tempSubOrder
	 */
	private SubOrder createSubOrderInfo(OrderInfo orderInfo, PreOrderDto preSubOrder) {
		preSubOrder.setRemark(orderInfo.getRemark());// 订单remark
		preSubOrder.setOrderStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);// 1：等待付款
		preSubOrder.setDeleted(SubOrderConstant.DELETED_FALSE);// 逻辑删除 -否
		preSubOrder.setOrderCode(orderCodeGeneratorService.generate(OrderCodeType.SON));// 设置子订单编号
		preSubOrder.setMemberId(orderInfo.getMemberId());
		preSubOrder.setAccountName(orderInfo.getAccountName());
		preSubOrder.setParentOrderCode(orderInfo.getParentOrderCode());// 父订单code
		preSubOrder.setPutStatus(SubOrderConstant.PutStatus.NEW.code);// 推送到仓库的状态（0:等待推送，1：已推送，2：推送失败）
		preSubOrder.resetPutCustomsStatus();
		preSubOrder.setPutCustomsTimes(0);
		Integer quantity = 0;
		for(OrderItem orderItem:preSubOrder.getOrderItemList()){
			quantity += orderItem.getQuantity();
		}
		preSubOrder.setQuantity(quantity);
		SubOrder subOrder = new SubOrder();
		BeanUtils.copyProperties(preSubOrder, subOrder);
		return subOrder;
	}
	
	private OrderItem createOrderItemInfo(OrderItem orderItem,SubOrder subOrder) {
		orderItem.setParentOrderCode(subOrder.getParentOrderCode());
		orderItem.setOrderCode(subOrder.getOrderCode());
		return orderItem;
	}
	
	
	/**
	 * 凑数是否进行合并支付
	 */
	public OrderInitDto initMergePay(OrderInitDto orderInitDto,OrderInfo orderInfo){
		if(CollectionUtils.isNotEmpty(orderInitDto.getPreSubOrderList()) && orderInitDto.getPreSubOrderList().size() > 1){
			orderInfo.setMergePay(1);
		}
		return orderInitDto;
	}
	
	/**
	 * 身份证信息
	 * @param orderInitDto
	 * @param orderDto
	 * @return
	 */
	private MemRealinfo initMemRealinfo(OrderInitDto orderInitDto, OrderDto orderDto) {
		OrderConsignee orderConsignee = orderDto.getOrderConsignee();
		MemRealinfo memRealinfo = new MemRealinfo();
		memRealinfo.setMemberId(orderInitDto.getMemberId());
		memRealinfo.setParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
		if(orderInitDto.getConsigneeAddress()!=null){
			memRealinfo.setIdentityFrontImg(orderInitDto.getConsigneeAddress().getFrontImg());
			memRealinfo.setIdentityBackImg(orderInitDto.getConsigneeAddress().getBackImg());
		}
		if(StringUtil.isNotBlank(orderConsignee.getIdentityCard())){
			memRealinfo.setIdentityCode(orderConsignee.getIdentityCard());
			memRealinfo.setRealName(orderConsignee.getName());
		}else{
			MemberDetail memberDetail = memberDetailProxy.findByUserId(orderInitDto.getMemberId());
			if(null!=memberDetail){
				memRealinfo.setIdentityCode(memberDetail.getCertificateValue());
				memRealinfo.setRealName(memberDetail.getTrueName());

			}else{
				return null;
			}
		}
		return memRealinfo;
	}

	/**
	 * 组装渠道跟踪信息
	 * @param orderInitDto
	 * @param orderDto
	 * @return
	 */
	private OrderChannelTrack initChannelTrack(OrderInitDto orderInitDto, OrderDto orderDto) {
		OrderChannelTrack orderChannelTrack = orderInitDto.getOrderChannelTrack();
		if(orderChannelTrack!=null){
			orderChannelTrack.setMemberId(orderInitDto.getMemberId());
			orderChannelTrack.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+orderInitDto.getMemberAccount());
		}
		return orderChannelTrack;
	}
}
