package com.tp.service.ord.local;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.ord.CartConstant;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.OrderReceiptConstant;
import com.tp.common.vo.ord.ParamValidator;
import com.tp.common.vo.ord.SalesOrderConstant;
import com.tp.common.vo.ord.SubOrderConstant;
import com.tp.common.vo.ord.OrderPromotionConstant.PromotionType;
import com.tp.common.vo.ord.SalesOrderConstant.OrderPayType;
import com.tp.common.vo.ord.TopicLimitItemConstant.TopicLimitType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CartCouponDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.OrderInfoDTO;
import com.tp.dto.ord.OrderInsertInfoDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.SeaOrderItemWithSupplierDTO;
import com.tp.dto.ord.SeaOrderItemWithWarehouseDTO;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.stg.OccupyInventoryDto;
import com.tp.dto.stg.WarehouseAreaDto;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ItemServiceException;
import com.tp.exception.OrderServiceException;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.model.sup.SupplierInfo;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.query.sup.SupplierQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.sup.SupplierCustomsRecordationResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IPriceService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.ord.ICartService;
import com.tp.service.ord.IOrderService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.local.ICartLocalService;
import com.tp.service.ord.local.IOrderSubmitLocalService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.JsonFormatUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 订单提交服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class OrderSubmitLocalService implements IOrderSubmitLocalService {

	private static final Logger log = LoggerFactory.getLogger(OrderSubmitLocalService.class);
	private static final double  TOTAL_AMOUNT_GOODS = 2000.00;
	private static final double ZERO = 0.0D;

	@Autowired
	private ICartLocalService cartLocalService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IPriceService priceService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private IInventoryOperService inventoryOperService;
	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IDistrictInfoService districtInfoService;
	@Autowired
	private IMemberDetailService memberDetailService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	@Autowired
	private ICheckTopicRemoteService checkTopicRemoteService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IGroupbuyInfoService groupbuyInfoService;
	

	@Override
	public List<PaymentInfo> orderSubmit(OrderInfoDTO orderInfoDTO, MemberInfo memberInfo, String ip, Integer orderSource) {

		Date startTime = new Date();
		// 传入参数信息校验
		argumentValidation(orderInfoDTO, memberInfo, ip, orderSource);

		// 实名认证信息
		MemRealinfo memRealinfo = null;
		if (CartConstant.TYPE_SEA == orderInfoDTO.getCartType().intValue()) {
			memRealinfo = seaAccountRealValidation(memberInfo.getId());
		}

		// 加载购物车中要下单商品
		CartDTO cartDTO = getCartInfo(memberInfo.getId(), orderInfoDTO.getCartType().intValue(),orderInfoDTO.getUuid());
		cartDTO.setMemberId(memberInfo.getId());

		// 校验商品ID 和 校验商品库存
		List<String> skuCodes = setCheckItemInfoList(cartDTO.getLineList());

		// 设置库存校验参数List
		List<SkuInventoryQuery> skuInventoryQuerys = setCheckStockInfoList(cartDTO.getLineList());

		// 校验商品信息
		checkItemInfo(skuCodes);

		// 校验商品是否可配送
		validateItemScope(memberInfo, ip, orderSource, cartDTO);

		// 校验商品库存
		checkItemInventoryInfo(skuInventoryQuerys);




		// 设置收货地址信息
		OrderConsignee orderConsignee = null;// 记录收货地址信息
		orderConsignee = setOrderConsigneeInfo(memberInfo.getId(), orderInfoDTO.getConsigneeId());

		// 收货人手机限购校验
		toplicMobileValidate(memberInfo, ip, orderSource, cartDTO, orderConsignee);

		// 验证送货范围
		validateDeliveryScope(cartDTO, orderConsignee);

		// 获取发票信息
		OrderReceipt orderReceipt = null;// 记录发票信息
		if (OrderReceiptConstant.INVOICE_REQUIRE == orderInfoDTO.getIsNeedInvoice().intValue()) {// 如果需要开发票
			orderReceipt = setReceiptInfo(orderInfoDTO);
		}

		// 获取优惠券信息
		List<Long> couponIds = null;// 获取用户使用优惠券ID
		if (CollectionUtils.isNotEmpty(orderInfoDTO.getCouponIds())) {
			couponIds = orderInfoDTO.getCouponIds();
		}

		//有团购商品则清空优惠券 并校验团购信息
		List<CartLineDTO> groupbuyCartLines = getGroupbuyLines(cartDTO);
		if(!groupbuyCartLines.isEmpty()){
			couponIds =null;
			if(orderInfoDTO.getGroupId() == null || groupbuyCartLines.size() > 1){
				throw new OrderServiceException("团购商品仅限团购页面购买");
			}
			CartLineDTO groupLine = groupbuyCartLines.get(0);
			ResultInfo groupCheckResult = groupbuyInfoService.checkForOrder(groupLine.getTopicId(),groupLine.getSkuCode(),orderInfoDTO.getGroupId(),memberInfo.getId());
			if(!groupCheckResult.isSuccess()){
				throw new OrderServiceException(groupCheckResult.getMsg()==null?"团购校验错误":groupCheckResult.getMsg().getMessage());
			}
			int boughtCount = checkTopicRemoteService.getBoughtCountWithGroupId(orderInfoDTO.getGroupId(),memberInfo.getId());
			if(boughtCount>0) throw new OrderServiceException("您已购买本团商品,请参加其他团或开团");
		}

		memberInfo = memberInfoService.queryById(memberInfo.getId());
		CartCouponDTO cartCouponDTO = new CartCouponDTO() ;
		cartDTO = setFirstMinus(cartDTO,orderSource);
		orderInfoDTO.firstMinus = cartDTO.firstMinus;
		if (CartConstant.TYPE_SEA != orderInfoDTO.getCartType().intValue()) {// 非海淘
			cartCouponDTO = getCounponInfo(memberInfo.getId(), cartDTO, couponIds);
			cartDTO = cartCouponDTO.getCartDTO();// 更新购物车信息
		}
		
		List<ItemResultDto> items = itemService.getSkuList(skuCodes);
		Map<String, ItemResultDto> itemMap = new HashMap<String, ItemResultDto>();
		if (CollectionUtils.isNotEmpty(items)) {
			for (ItemResultDto item : items) {
				itemMap.put(item.getSku(), item);
			}
		}
		
		// 拆单
		Map<Long, List<CartLineDTO>> xgOrderItemMapByStorage = new LinkedHashMap<Long, List<CartLineDTO>>(); // 记录自营订单拆单Map
		Map<Long, Map<Long, List<CartLineDTO>>> platformOrderItemMapByStorage = splitSalesOrder(cartDTO.getLineList(), xgOrderItemMapByStorage);
	
		// 设置父订单信息
		OrderInfo salesOrder = new OrderInfo();
		if (CartConstant.TYPE_SEA != orderInfoDTO.getCartType().intValue()) {
			salesOrder = setSalesOrderInfo(orderInfoDTO, cartDTO);// 设置父订单通用信息
		}
		salesOrder.setMemberId(memberInfo.getId());// 设置会员ID
		salesOrder.setPromoterId(memberInfo.getPromoterId());
		salesOrder.setScanPromoterId(memberInfo.getScanPromoterId());
		salesOrder.setShopPromoterId(orderInfoDTO.getShopPromoterId());
		if (StringUtils.isNotEmpty(memberInfo.getMobile())) {
			salesOrder.setAccountName(memberInfo.getMobile());// 设置账户名为手机号
		} else {
			salesOrder.setAccountName(memberInfo.getEmail());// 设置账户名邮箱
		}

		salesOrder.setIp(ip);// 设置下单IP
		salesOrder.setSource(orderSource);// 订单来源（1：MOBILE。2：PC）

		// 记录子订单和订单行对应关系
		Map<SubOrder, List<OrderItem>> subOrderMapOrderLine = new LinkedHashMap<SubOrder, List<OrderItem>>();
		// 记录订单行和促销信息对应关系
		Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo = new LinkedHashMap<OrderItem, TopicItemInfoDTO>();
		// 记录减库存参数信息
		List<OccupyInventoryDto> InventoryQuerys = new ArrayList<OccupyInventoryDto>();

		// 设置取自营和代销子订单,订单行信息
		if (null != xgOrderItemMapByStorage && !xgOrderItemMapByStorage.isEmpty()) {// 有自营订单
			Map<SubOrder, List<OrderItem>> ownSubOrderMapOrderLine = setSubOrderAndOrderLIneInfo(
					xgOrderItemMapByStorage, salesOrder, orderLineMapTopicItemInfo, InventoryQuerys, itemMap);
			subOrderMapOrderLine.putAll(ownSubOrderMapOrderLine);
		}
		
		// 设置第三方或者海淘子订单，订单行信息		
		Map<SubOrder, List<OrderItem>> platformSubOrderMapOrderLine =setPlatformOrSeaOrderInfo(platformOrderItemMapByStorage,orderInfoDTO,cartCouponDTO,couponIds,orderLineMapTopicItemInfo,salesOrder,InventoryQuerys,itemMap);
		if(null != platformSubOrderMapOrderLine && !platformSubOrderMapOrderLine.isEmpty()){
			subOrderMapOrderLine.putAll(platformSubOrderMapOrderLine);
		}

		// 拆分运费
		if (null != salesOrder && salesOrder.getFreight() > 0) {// 运费不为0
			if (CartConstant.TYPE_SEA == orderInfoDTO.getCartType().intValue()) {
				splitSeaOrderFreight(subOrderMapOrderLine);
			} else {
				calculateSubOrderFreight(cartDTO, subOrderMapOrderLine, new BigDecimal(cartDTO.getRealFee().toString()));
			}
		}
		// 拆分优惠券
		operateOrgAmount(subOrderMapOrderLine);
		Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion = splitCoupon(orderLineMapTopicItemInfo,
				subOrderMapOrderLine, cartCouponDTO);

		// 海淘订单添加实名认证和海关备案信息
		if (CartConstant.TYPE_SEA == orderInfoDTO.getCartType().intValue()) {
			setCustomInfo(subOrderMapOrderLine); // 添加海淘customcode和名字
			memRealinfo.setParentOrderCode(salesOrder.getParentOrderCode());// 设置父订单code
		}

		// 组织插入数据库订单数据
		OrderInsertInfoDTO orderInsertInfoDTO = getInsertOrderInfo(orderConsignee, orderReceipt, salesOrder,
				subOrderMapOrderLine, orderLineMapPromotion, memRealinfo);

		orderInsertInfoDTO.setGroupId(orderInfoDTO.getGroupId());

		// 减库存
		reduceInventory(InventoryQuerys);

		// 获取子订单code list
		List<Long> subOrderCodeList = new ArrayList<Long>();
		for (SubOrder subOrder : subOrderMapOrderLine.keySet()) {
			subOrderCodeList.add(subOrder.getOrderCode());
		}

		// 更新优惠券状态
		if (CollectionUtils.isNotEmpty(couponIds)) {
			updateCouponStatus(couponIds, subOrderCodeList);
		}

		// 订单信息插入数据库
		try {
				orderService.addOrder(orderInsertInfoDTO);
			// 推送支付信息
			List<PaymentInfo> paymentInfos = pushPaymentInfo(salesOrder, memRealinfo, subOrderMapOrderLine);

			// 清除购物车相应行
			cartService.deleteCartLineSimpleDTOBySettlement(memberInfo.getId(), cartDTO.getLineList());
			Date endTime = new Date();
			if (endTime.getTime() - startTime.getTime() > 2000) {
				log.warn("创建订单：会员id[{}]-结束，ip={}耗时{}毫秒", memberInfo.getId(), InetAddress.getLocalHost()
						.getHostAddress(), endTime.getTime() - startTime.getTime());
			}
			return paymentInfos;
		} catch (Exception e) {
			log.error("下单错误信息：订单插入数据库错误", e);
			// TO 库存回滚
			ResultInfo<String> resultInfo = inventoryRollback(subOrderCodeList);
			// 优惠券回滚
			if (resultInfo.success && CollectionUtils.isNotEmpty(couponIds)) {
				couponRollback(couponIds);
			}
			throw new OrderServiceException(OrderErrorCodes.DATABASE_INSERT_ERROR);
		}

	}

	private List<CartLineDTO> getGroupbuyLines(CartDTO cartDTO) {
		if(cartDTO == null) return Collections.emptyList();
		List<CartLineDTO> groupCartLine = new ArrayList<>();
		if(cartDTO.getLineList() != null && !cartDTO.getLineList().isEmpty()){
			for(CartLineDTO cartLineDTO: cartDTO.getLineList()){
				if(cartLineDTO != null &&  cartLineDTO.getSalesPattern() !=null && cartLineDTO.getSalesPattern().equals(SalesPartten.GROUP_BUY.getValue())){
					groupCartLine.add(cartLineDTO);
				}
			}
		}
		return groupCartLine;
	}

	/**
	 * <pre>
	 * * 设置第三方或者海淘子订单，订单行信息
	 * </pre>
	 * 
	 * @param platformOrderItemMapByStorage
	 * @param orderInfoDTO
	 * @param cartCouponDTO
	 * @param couponIds
	 * @param orderLineMapTopicItemInfo
	 * @param salesOrderDO
	 * @param InventoryQuerys
	 * @param itemMap
	 * @return
	 */
	
	Map<SubOrder, List<OrderItem>> setPlatformOrSeaOrderInfo(
			Map<Long, Map<Long, List<CartLineDTO>>> platformOrderItemMapByStorage, OrderInfoDTO orderInfoDTO,
			CartCouponDTO cartCouponDTO, List<Long> couponIds,
			Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo, OrderInfo salesOrder,
			List<OccupyInventoryDto> InventoryQuerys, Map<String, ItemResultDto> itemMap) {
		Map<SubOrder, List<OrderItem>> subOrderMapOrderLine = new LinkedHashMap<SubOrder, List<OrderItem>>();
		if (null != platformOrderItemMapByStorage && !platformOrderItemMapByStorage.isEmpty()) {// 有第三方订单
	
			if (CartConstant.TYPE_SEA == orderInfoDTO.getCartType().intValue()) {// 海淘
				CartCouponDTO cartCoupon = getSeaCouponInfo(platformOrderItemMapByStorage, couponIds,salesOrder.getMemberId(),orderInfoDTO.firstMinus);
				cartCouponDTO.setCouponDtoList(cartCoupon.getCouponDtoList());
				cartCouponDTO.setCuidSkuMap(cartCoupon.getCuidSkuMap());
				cartCouponDTO.setSeaOrderItemDTO(cartCoupon.getSeaOrderItemDTO());
				
				// seaOrderItem = cartCouponDTO.getSeaOrderItemDTO();
				// 设置父订单通用信息
				salesOrder = setSeaSalesOrderInfo(orderInfoDTO, cartCouponDTO.getSeaOrderItemDTO(), salesOrder);
				subOrderMapOrderLine = setSeaOrderInfo(cartCouponDTO.getSeaOrderItemDTO(), salesOrder,
						orderLineMapTopicItemInfo, InventoryQuerys, itemMap);
			} else {
				// platformKey供应商ID
				Iterator<Long> platformKit = platformOrderItemMapByStorage.keySet().iterator();
				while (platformKit.hasNext()) {
					Long platformKey = (Long) platformKit.next();
					Map<Long, List<CartLineDTO>> platformOrderItemMap = platformOrderItemMapByStorage.get(platformKey);
					Map<SubOrder, List<OrderItem>> seaSubOrderMapOrderLine = setSubOrderAndOrderLIneInfo(
							platformOrderItemMap, salesOrder, orderLineMapTopicItemInfo, InventoryQuerys, itemMap);
					subOrderMapOrderLine.putAll(seaSubOrderMapOrderLine);
				}
			}
		}
		return subOrderMapOrderLine;
	}
	
	/**
	 * <pre>
	 * * 拆单
	 * </pre>  
	 * 
	 * @param cartLInes
	 * @return 
	 */

	
	Map<Long, Map<Long, List<CartLineDTO>>> splitSalesOrder(List<CartLineDTO> cartLInes, Map<Long, List<CartLineDTO>> xgOrderItemMap) {
	
		List<CartLineDTO> xgCartLines = new ArrayList<CartLineDTO>();
		List<CartLineDTO> platformCartLines = new ArrayList<CartLineDTO>();
	
		// 按销售模式拆分订单 /** 销售模式：1-买断，2-代销，3-平台,4-海淘, 默认1 */
		sliptOrderItemBySaleType(cartLInes, xgCartLines, platformCartLines);
	
		// 自营的按仓库id拆单
		if (CollectionUtils.isNotEmpty(xgCartLines)) {
			Map<Long, List<CartLineDTO>> xgOrderItemMap1 = sliptOrderItemByStorage(xgCartLines);
			xgOrderItemMap.putAll(xgOrderItemMap1);
			 
		}
		// 第三方&海淘先按供应商拆单
		// 订单商品信息 key:供应商ID，value=商品（购物车行）信息列表
		Map<Long, List<CartLineDTO>> orderItemMapBySupplier = null;
		if (CollectionUtils.isNotEmpty(platformCartLines)) {
			orderItemMapBySupplier = sliptOrderItemBySupplier(platformCartLines);
		}
	
		// 第三方&海淘再按仓库拆分
		Map<Long, Map<Long, List<CartLineDTO>>> platformOrderItemMapByStorage = null;
		if (null != orderItemMapBySupplier && !orderItemMapBySupplier.isEmpty()) {
			platformOrderItemMapByStorage = sliptOrderItemByStorage(orderItemMapBySupplier);
			
		}
		return platformOrderItemMapByStorage;
		
	}

	/**
	 * <pre>
	 * * 收货人手机限购校验
	 * </pre>
	 * 
	 * @param userMenber
	 * @param ip
	 * @param orderSource
	 * @param cartDTO
	 * @param orderConsigneeDO
	 */
	private void toplicMobileValidate(MemberInfo userMenber, String ip, Integer orderSource, CartDTO cartDTO,
			OrderConsignee orderConsignee) {
		/* 获取商品限购信息 */
		List<TopicItemCartQuery> topicQueryList = new ArrayList<TopicItemCartQuery>();
		for (CartLineDTO cartLineDTO : cartDTO.getLineList()) {
			TopicItemCartQuery topicItemCartQuery = new TopicItemCartQuery();
			topicItemCartQuery.setMemberId(userMenber.getId());
			topicItemCartQuery.setArea(cartLineDTO.getAreaId());
			topicItemCartQuery.setSku(cartLineDTO.getSkuCode());
			topicItemCartQuery.setPlatform(orderSource);
			topicItemCartQuery.setTopicId(cartLineDTO.getTopicId());
			topicItemCartQuery.setAmount(cartLineDTO.getQuantity());
			topicItemCartQuery.setUip(ip);
			topicItemCartQuery.setMobile(orderConsignee.getMobile());
			topicQueryList.add(topicItemCartQuery);
		}

		List<TopicPolicyDTO> topicPolicyList = new ArrayList<TopicPolicyDTO>();
		try {
			topicPolicyList = topicService.queryTopicPolicyInfo(topicQueryList);
		} catch (Exception e) {
			log.error("根据skuCodeList: {} 获取商品限购信息失败！,error:{}", JSONArray.fromObject(topicQueryList).toString(), e);
			throw new OrderServiceException(OrderErrorCodes.GET_TOPIC_INFO_ERROR,"获取商品限购信息失败");
		}
		for (TopicPolicyDTO topicPolicyDTO : topicPolicyList) {
			ResultInfo<Boolean> result = checkTopicRemoteService.checkTopicPolicy(topicPolicyDTO);
			if (!result.success) {
				log.error("收货人手机号达到限购！topicPolicyDTO:{}", JSONObject.fromObject(topicPolicyDTO).toString());
				throw new OrderServiceException(OrderErrorCodes.PROMOTION_MOBILE_LIMIT_POLICY, "收货人手机号达到限购！");
			}
		}
	}

	/**
	 * <pre>
	 * 海淘订单根据用户所选优惠券计算购物车总价和运费，以及优惠券id对应商品map
	 * </pre>
	 * 
	 * @param platformOrderItemMapByStorage
	 * @param couponIds
	 * @return
	 */
	CartCouponDTO getSeaCouponInfo(Map<Long, Map<Long, List<CartLineDTO>>> platformOrderItemMapByStorage,
			List<Long> couponIds,Long memberId,Boolean firstMinus) {
		SeaOrderItemDTO seaOrderItem = buildSeaCartInfo(memberId,platformOrderItemMapByStorage);
		seaOrderItem.setFirstMinus(firstMinus);
		// 调海淘促销接口
		try {
			CartCouponDTO cartCouponDTO = priceService.hitaoOrderTotalPriceWithCoupon(seaOrderItem, couponIds);
			return cartCouponDTO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("海淘促销接口抛出异常错误:seaOrderItem：{},couponIds:{},error info:{}", JSONObject.fromObject(seaOrderItem)
					.toString(), JSONArray.fromObject(couponIds).toString(), e);
			throw new OrderServiceException(OrderErrorCodes.SEA_PROMOTION_ERROR);
		}
	}

	/**
	 * <pre>
	 * 拆分海淘订单运费
	 * </pre>
	 * 
	 * @param subOrderMapOrderLine
	 */
	private void splitSeaOrderFreight(Map<SubOrder, List<OrderItem>> subOrderMapOrderLine) {
		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEnty : subOrderMapOrderLine.entrySet()) {
			SubOrder subOrder = subOrderEnty.getKey();
			List<OrderItem> orderLines = subOrderEnty.getValue();
			double subFreight = subOrder.getFreight();
			OrderItem maxFreightOrderLine = null;
			BigDecimal freightUsedSum = BigDecimal.ZERO;
			if (subFreight > 0) {
				for (OrderItem orderLine : orderLines) {
					BigDecimal tempFreight = BigDecimalUtil.divide(
							BigDecimalUtil.multiply(orderLine.getOriginalSubTotal(), subFreight),
							subOrder.getOriginalTotal());
					orderLine.setFreight(BigDecimalUtil.formatToPrice(tempFreight).doubleValue());
					freightUsedSum = BigDecimalUtil.add(freightUsedSum, BigDecimalUtil.formatToPrice(tempFreight));
					if (maxFreightOrderLine == null
							|| maxFreightOrderLine.getFreight().doubleValue() < orderLine.getFreight()) {
						maxFreightOrderLine = orderLine;
					}
				}

				double balance = BigDecimalUtil.formatToPrice(BigDecimalUtil.subtract(freightUsedSum, subFreight)).doubleValue();
				if (balance != 0) {
					maxFreightOrderLine.setFreight(BigDecimalUtil.formatToPrice(
							BigDecimalUtil.add(maxFreightOrderLine.getFreight(), balance)).doubleValue());
				}
			}
		}
	}

	/**
	 * <pre>
	 * 设置海淘通关备案信息
	 * </pre>
	 * 
	 * @param subOrderMapOrderLine
	 */
	private void setCustomInfo(Map<SubOrder, List<OrderItem>> subOrderMapOrderLine) {
		List<SupplierQuery> queryList = new ArrayList<SupplierQuery>();
		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEnty : subOrderMapOrderLine.entrySet()) {
			SubOrder subOrder = subOrderEnty.getKey();
			SupplierQuery supplierQueryPojo = new SupplierQuery();
			supplierQueryPojo.setCustomsChannelId(subOrder.getSeaChannel());
			supplierQueryPojo.setSupplierId(subOrder.getSupplierId());
			queryList.add(supplierQueryPojo);
		}
		SupplierCustomsRecordationResult supplierResults = purchasingManagementService.getSupplierCustomsRecordation(queryList);
		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEnty : subOrderMapOrderLine.entrySet()) {
			SubOrder subOrder = subOrderEnty.getKey();
			// List<OrderItem> orderLines = subOrderEnty.getValue();
			for (SupplierCustomsRecordation customsRecordation : supplierResults.getSupplierCustomsRecordationList()) {
				if (subOrder.getSeaChannel().equals(customsRecordation.getCustomsChannelId())
						&& subOrder.getSupplierId().equals(customsRecordation.getSupplierId())) {
					subOrder.setCustomCode(customsRecordation.getRecordationNum());
					subOrder.setOrgName(customsRecordation.getCustomsChannelName());
					break;
				}
			}
		}
	}

	/**
	 * 
	 * 校验海淘实名认证信息
	 *
	 * @param memberId
	 * @return MemRealinfoDO
	 */
	private MemRealinfo seaAccountRealValidation(Long memberId) {
		MemberDetail userDetail = memberDetailService.selectByUid(memberId);
		if (null == userDetail) {
			log.error("调user实名认证接口selectByUid返回值：userDetail：{}", JSONObject.fromObject(userDetail).toString());
			throw new OrderServiceException(OrderErrorCodes.REAL_VERIFY_ERROR);
		}
		MemRealinfo memRealinfo = new MemRealinfo();
		memRealinfo.setMemberId(memberId);
		memRealinfo.setRealName(userDetail.getTrueName());
		memRealinfo.setIdentityCode(userDetail.getCertificateValue());// 证件号码
		memRealinfo.setIdentityFrontImg(userDetail.getPicA());
		memRealinfo.setIdentityBackImg(userDetail.getPicB());
		memRealinfo.setAddress(userDetail.getAddress());
		return memRealinfo;
	}

	/**
	 * <pre>
	 * 校验商品是否可配送
	 * </pre>
	 * 
	 * @param userMenber
	 * @param ip
	 * @param orderSource
	 * @param cartDTO
	 */
	private void validateItemScope(MemberInfo userMenber, String ip, Integer orderSource, CartDTO cartDTO) {
		List<TopicItemCartQuery> querys = new ArrayList<TopicItemCartQuery>();
		for (CartLineDTO cartLine : cartDTO.getLineList()) {
			TopicItemCartQuery query = new TopicItemCartQuery();

			query.setPlatform(orderSource);
			query.setArea(cartLine.getAreaId());
			query.setTopicId(cartLine.getTopicId());
			query.setSku(cartLine.getSkuCode());
			query.setAmount(cartLine.getQuantity());
			query.setMemberId(userMenber.getId());
			query.setUip(ip);

			querys.add(query);
		}

		ResultInfo<Boolean> result = topicService.validateTopicItemList(querys);
		if (!result.success) {
			FailInfo failInfo = result.getMsg();
			if (ErrorCodeType.LOCKED.ordinal() == failInfo.getCode()) { // 促销商品已锁定，做库存不足处理
				log.error(" querys:{},促销商品已锁定，做库存不足处理 error Info:{}", JSONArray.fromObject(querys).toString(),
						failInfo.getMessage());
				throw new OrderServiceException(OrderErrorCodes.CHECK_SKU_STOCK_ERROR);
			}
			log.error("调促销接口验证商品是否在销售范围 querys:{},error Info:{}", JSONArray.fromObject(querys).toString(),
					failInfo.getMessage());
			throw new OrderServiceException(OrderErrorCodes.ITEM_BEYOND_DELIVERY_ERROR);
		}


	}

	/**
	 * <pre>
	 * 根据用户所选优惠券计算购物车总价和运费，以及优惠券id对应商品map
	 * </pre>
	 * 
	 * @param memberId
	 * @param cartDTO
	 * @param couponIds
	 * @return cartCouponDTO
	 */
	private CartCouponDTO getCounponInfo(Long memberId, CartDTO cartDTO, List<Long> couponIds) {
		try {
			// 根据用户所选优惠券计算购物车总价和运费，以及优惠券id对应商品map
			CartCouponDTO cartCouponDTO = priceService.orderTotalPriceWithCoupon(cartDTO, couponIds, memberId);
			return cartCouponDTO;
		} catch (Exception e) {
			log.error("调促销接口orderTotalPriceWithCoupon异常:cartDTO:{},couponIds:{},memberId:{},error info:{}",
					JsonFormatUtils.format(cartDTO), JSONArray.fromObject(couponIds).toString(), memberId,
					e.getMessage());
			throw new OrderServiceException(OrderErrorCodes.CALCULATE_TOTALPRICE_FREIGHT_ERROR);
		}
	}

	/**
	 * 
	 * 
	 * 组织插入数据库参数
	 * 
	 * @param orderConsigneeDO
	 * @param orderReceiptDO
	 * @param salesOrderDO
	 * @param subOrderMapOrderLine
	 * @param orderLineMapPromotion
	 * @param memRealinfo
	 * @return OrderInsertInfoDTO
	 */
	private OrderInsertInfoDTO getInsertOrderInfo(OrderConsignee orderConsignee, OrderReceipt orderReceipt,
			OrderInfo salesOrder, Map<SubOrder, List<OrderItem>> subOrderMapOrderLine,
			Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion, MemRealinfo memRealinfo) {
		OrderInsertInfoDTO orderInsertInfoDTO = new OrderInsertInfoDTO();
		orderConsignee.setParentOrderCode(salesOrder.getParentOrderCode());
		orderConsignee.setParentOrderId(salesOrder.getId());
		orderInsertInfoDTO.setOrderConsignee(orderConsignee);// 收货人地址信息
		orderInsertInfoDTO.setSubOrderMapOrderItem(subOrderMapOrderLine);
		orderInsertInfoDTO.setOrderItemMapPromotion(orderLineMapPromotion);
		orderInsertInfoDTO.setOrderReceipt(orderReceipt);// 发票信息
		orderInsertInfoDTO.setOrderInfo(salesOrder);
		orderInsertInfoDTO.setMemRealinfo(memRealinfo);
		// 组织限购数据
		List<TopicLimitItem> topicLimitItems = new ArrayList<TopicLimitItem>();
		for (Map.Entry<SubOrder, List<OrderItem>> orderLineEnty : subOrderMapOrderLine.entrySet()) {
			// SubOrder subOrder = orderLineEnty.getKey();
			List<OrderItem> orderLines = orderLineEnty.getValue();
			for (OrderItem orderLine : orderLines) {
				// 用户手机号限购
				TopicLimitItem topicLimitItem = new TopicLimitItem();
				topicLimitItem.setTopicId(orderLine.getTopicId());
				topicLimitItem.setSkuCode(orderLine.getSkuCode());
				topicLimitItem.setBuyedQuantity(orderLine.getQuantity());
				topicLimitItem.setLimitType(TopicLimitType.MOBILE.code);
				topicLimitItem.setLimitValue(orderConsignee.getMobile());
				topicLimitItems.add(topicLimitItem);
			}
		}
		orderInsertInfoDTO.setTopicLimitItems(topicLimitItems);

		return orderInsertInfoDTO;
	}

	/**
	 * <pre>
	 * 验证送货范围
	 * </pre>
	 * 
	 * @param cartDTO
	 * @param orderConsigneeDO
	 */
	private void validateDeliveryScope(CartDTO cartDTO, OrderConsignee orderConsignee) {
		WarehouseAreaDto warehouseAreaDto = new WarehouseAreaDto();
		List<Long> warehouseIds = new ArrayList<Long>();
		warehouseAreaDto.setProvinceId(orderConsignee.getProvinceId());
		warehouseAreaDto.setCityId(orderConsignee.getCityId());
		warehouseAreaDto.setCountyId(orderConsignee.getCountyId());
		warehouseAreaDto.setStreetId(orderConsignee.getTownId());
		// 获取收货人大区ID
		DistrictInfo districtInfo = districtInfoService.queryById(orderConsignee.getProvinceId());
		if (null == districtInfo || null == districtInfo.getParentId()) {
			log.error("获取收货人大区ID" + "ProvinceId=" + orderConsignee.getProvinceId() + "返回值为空！");
			throw new OrderServiceException(OrderErrorCodes.GET_RECEIVER_AREA_ID_ERROR);
		}
		warehouseAreaDto.setRegionId(districtInfo.getParentId());

		for (CartLineDTO line : cartDTO.getLineList()) {
			warehouseIds.add(line.getWarehouseId());
		}
		warehouseAreaDto.setWarehouseIds(warehouseIds);


		Map<Long, ResultInfo<String>> warehouseMessage = warehouseService
				.checkWarehouseArea(warehouseAreaDto);
		Map<Long, ResultInfo<String>> errorMessages = new LinkedHashMap<Long, ResultInfo<String>>();
		for (Map.Entry<Long, ResultInfo<String>> warehouseEntry : warehouseMessage
				.entrySet()) {
			Long messageKey = warehouseEntry.getKey();
			ResultInfo<String> warehouseResultMessage = warehouseEntry.getValue();
			if (!warehouseResultMessage.success) {
				errorMessages.put(messageKey, warehouseResultMessage);
			}
		}

		if (errorMessages.size() > 0) {
			for (Map.Entry<Long, ResultInfo<String>> ErrorwarehouseEntry : errorMessages
					.entrySet()) {
				ResultInfo<String> errorResultMessage = ErrorwarehouseEntry.getValue();
				log.error("验证送货范围错误：入参:{},errorinfo={}", JsonFormatUtils.format(warehouseAreaDto),
						errorResultMessage.getMsg().getMessage());
			}
			throw new OrderServiceException(OrderErrorCodes.CHECK_WAREHOUSE_DELIVERY_ERROR);
		}
	}

	/**
	 * <pre>
	 * 推送支付
	 * </pre>
	 * 
	 * @param salesOrderDO
	 * @param memRealinfo
	 * @param subOrderMapOrderLine
	 * @return paymentInfo
	 */
	private List<PaymentInfo> pushPaymentInfo(OrderInfo salesOrder, MemRealinfo memRealinfo,
			Map<SubOrder, List<OrderItem>> subOrderMapOrderLine) {
		Boolean overseaFlag = false;
		List<PayPaymentSimpleDTO> PayPaymentSimples = new ArrayList<PayPaymentSimpleDTO>();

		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEntry : subOrderMapOrderLine.entrySet()) {
			SubOrder subOrder = subOrderEntry.getKey();
			if (OrderUtils.isSeaOrder(subOrder.getType())) {
				overseaFlag = true;
				break;
			}
		}
		if (true == overseaFlag) {
			for (Map.Entry<SubOrder, List<OrderItem>> subOrderEntry : subOrderMapOrderLine.entrySet()) {
				SubOrder subOrder = subOrderEntry.getKey();
				PayPaymentSimpleDTO payPaymentSimpleDTO = new PayPaymentSimpleDTO();
				payPaymentSimpleDTO.setUserId(subOrder.getMemberId());
				payPaymentSimpleDTO.setBizCode(subOrder.getOrderCode());
				payPaymentSimpleDTO.setBizType(PaymentConstant.BIZ_TYPE.SUBORDER.code);
				payPaymentSimpleDTO.setBizCreateTime(subOrder.getCreateTime());
				payPaymentSimpleDTO.setAmount(BigDecimalUtil.add(subOrder.getTotal(), subOrder.getFreight()).doubleValue());
				payPaymentSimpleDTO.setActionIP(salesOrder.getIp());
				payPaymentSimpleDTO.setOrderType(subOrder.getType().longValue());	
				payPaymentSimpleDTO.setIdentityType("01");// 证件类型
				payPaymentSimpleDTO.setIdentityCode(memRealinfo.getIdentityCode());// 证件号码
				payPaymentSimpleDTO.setRealName(memRealinfo.getRealName());// 姓名
				payPaymentSimpleDTO.setTaxFee(subOrder.getTaxFee());// 税款
				payPaymentSimpleDTO.setFreight(subOrder.getFreight());// 运费
				payPaymentSimpleDTO.setChannelId(subOrder.getSeaChannel());// 海淘渠道id
				PayPaymentSimples.add(payPaymentSimpleDTO);
			}
		} else {
			PayPaymentSimpleDTO payPaymentSimpleDTO = new PayPaymentSimpleDTO();
			payPaymentSimpleDTO.setUserId(salesOrder.getMemberId());
			payPaymentSimpleDTO.setBizCode(salesOrder.getParentOrderCode());
			payPaymentSimpleDTO.setBizType(PaymentConstant.BIZ_TYPE.ORDER.code);
			payPaymentSimpleDTO.setBizCreateTime(salesOrder.getCreateTime());
			payPaymentSimpleDTO.setAmount(BigDecimalUtil.add(salesOrder.getTotal(), salesOrder.getFreight())
					.doubleValue());
			payPaymentSimpleDTO.setActionIP(salesOrder.getIp());
			payPaymentSimpleDTO.setGatewayId(salesOrder.getPayWay());// 支付网关
			PayPaymentSimples.add(payPaymentSimpleDTO);
		}

		List<PaymentInfo> paymentInfos = paymentInfoService.insertPaymentInfoList(PayPaymentSimples);
		if (CollectionUtils.isEmpty(paymentInfos)) {
			log.error("推送支付错误：调用推送支付接口返回信息为空！");
			throw new OrderServiceException(OrderErrorCodes.PUSH_PAYMENT_ERROR);
		}
		for (PaymentInfo paymentInfo : paymentInfos) {
			if (null == paymentInfo.getPaymentId()) {
				log.error("推送支付错误：调用推送支付接口返回payID为空！");
				throw new OrderServiceException(OrderErrorCodes.PUSH_PAYMENT_ERROR);
			}
		}
		return paymentInfos;
	}

	/**
	 * <pre>
	 * 获取用户下单商品
	 * </pre>
	 * 
	 * @param memberId
	 * @param cartType
	 * @return
	 */
	private CartDTO getCartInfo(Long memberId, int cartType, String uuid) {
		
		CartDTO cartDTO = null;
		if (StringUtils.isNotBlank(uuid)) {// 快速购买
			cartDTO = (CartDTO) jedisCacheUtil.getCache(uuid);
		} else {
			cartDTO = cartLocalService.getValidateCart(memberId, cartType);
		}
	
		if (null == cartDTO) {
			log.info("购物车货物信息为空");
			throw new OrderServiceException(OrderErrorCodes.CHECK_CART_INFO_ERROR);
		}
		if (CartConstant.TYPE_SEA == cartType) {
			List<CartLineDTO> tempCartLines = new ArrayList<CartLineDTO>();
			for (Map.Entry<String, List<CartLineDTO>> cartLineEntry : cartDTO.getSeaMap().entrySet()) {
				tempCartLines.addAll(cartLineEntry.getValue());
			}
			cartDTO.setLineList(tempCartLines);
		}
		if (CollectionUtils.isEmpty(cartDTO.getLineList())) {
			log.info("购物车货物信息为空");
			throw new OrderServiceException(OrderErrorCodes.CHECK_CART_INFO_ERROR);
		}
	
		List<CartLineDTO> lineDTOTemp = new ArrayList<CartLineDTO>();
		for (CartLineDTO cartLineDTO : cartDTO.getLineList()) {
			if (cartLineDTO.getSelected()) {
				if (cartLineDTO.getSkuStatus().intValue() != CartConstant.ITEM_STATUS_1) {
					throw new OrderServiceException(OrderErrorCodes.CHECKCART_VALIDATE_SKU_ERROR);
				} else if (!cartLineDTO.getTopicItemInfo().getValidate()) {
					throw new OrderServiceException(OrderErrorCodes.TOPIC_OVERDUE_ERROR);
				} else {
					lineDTOTemp.add(cartLineDTO);
				}
			}
		}
		if (CollectionUtils.isEmpty(lineDTOTemp)) {
			log.error("购物车中没有选中的有效商品！入参:{}", JSONObject.fromObject(cartDTO).toString());
			throw new OrderServiceException(OrderErrorCodes.SELECTED_SKU_EMPTY_ERROR);
		}
		cartDTO.setLineList(lineDTOTemp);
	
		return cartDTO;
	}

	/**
	 * <pre>
	 * 减库存
	 * </pre>
	 * 
	 * @param InventoryQuerys
	 */
	private void reduceInventory(List<OccupyInventoryDto> InventoryQuerys) {
		Map<String, ResultInfo<String>> resultMessageMap = inventoryOperService
				.batchOccupyInventory(InventoryQuerys);
		Iterator<String> storageMessageKit = resultMessageMap.keySet().iterator();
		while (storageMessageKit.hasNext()) {
			String storageMessageKey = (String) storageMessageKit.next();
			ResultInfo<String> tempMessage = resultMessageMap.get(storageMessageKey);
			if (!tempMessage.success) {
				log.error("减库存接口调用失败,入参:{},错误信息:{}", JSONArray.fromObject(InventoryQuerys).toString(),
						tempMessage.getMsg().getMessage());
				throw new OrderServiceException(OrderErrorCodes.REDUCE_STOCK_ERROR);
			}
		}
	}

	/**
	 * <pre>
	 * 优惠券回滚
	 * </pre>
	 * 
	 * @param couponIds
	 */
	private void couponRollback(List<Long> couponIds) {
		try {
			couponUserService.updateCouponUserStatus(couponIds, CouponUserStatus.NORMAL.ordinal());
		} catch (Exception e) {
			log.error("优惠券回滚处理错误,入参:couponIds{},CouponUserStatus:{},errorinfo:{}", JSONArray.fromObject(couponIds)
					.toString(), CouponUserStatus.NORMAL.ordinal(), e);
			throw new OrderServiceException(OrderErrorCodes.ROLLBACK_COUPON_ERROR);
		}
	}

	/**
	 * <pre>
	 * 更新优惠券
	 * </pre>
	 * 
	 * @param orderCoupons
	 * @param orderCode
	 */
	private void updateCouponStatus(List<Long> couponIds, List<Long> subOrderCodeList) {
		try {
			couponUserService.updateCouponUserStatus(couponIds, CouponUserStatus.USED.ordinal());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新优惠券信息错误,入参couponIds:{},CouponUserStatus:{},errorinfo:{}", JSONArray.fromObject(couponIds)
					.toString(), CouponUserStatus.USED.ordinal(), e);
			inventoryRollback(subOrderCodeList);// 库存回滚处理！！
			throw new OrderServiceException(OrderErrorCodes.UPDATE_COUPON_INFO_ERROR);
		}
	}

	/**
	 * <pre>
	 * 库存回滚
	 * </pre>
	 * 
	 * @param orderCode
	 */
	private ResultInfo<String> inventoryRollback(List<Long> subOrderCodeList) {
		try {
			return  inventoryOperService.batchUnoccupyInventory(subOrderCodeList);// 库存回滚接口
		} catch (Exception e) {
			log.error(" 库存回滚接口调用失败,入参:{},错误信息:{}", JSONArray.fromObject(subOrderCodeList).toString(), e);
			return new ResultInfo<String>(new FailInfo("下单失败，库存回滚出错 "));
		}
	}

	/**
	 * <pre>
	 * 校验商品库存
	 * </pre>
	 * 
	 * @param skuInventoryQuerys
	 */
	private void checkItemInventoryInfo(List<SkuInventoryQuery> skuInventoryQuerys) {
		log.info("此方法已注");
//		Map<String, ResultInfo<String>> errorMessages = new LinkedHashMap<String, ResultInfo<String>>();
//		Map<String, ResultInfo<String>> stockMessageMap = inventoryQueryService
//				.checkAllInventory(skuInventoryQuerys);
//		Iterator<String> storageMessageKit = stockMessageMap.keySet().iterator();
//		while (storageMessageKit.hasNext()) {
//			String storageMessageKey = (String) storageMessageKit.next();
//			ResultInfo<String> stockMessage = stockMessageMap.get(storageMessageKey);
//			if (!stockMessage.success) {
//				errorMessages.put(storageMessageKey, stockMessage);
//			}
//		}
//
//		if (errorMessages.size() > 0) {
//			storageMessageKit = errorMessages.keySet().iterator();
//			while (storageMessageKit.hasNext()) {
//				String storageMessageKey = (String) storageMessageKit.next();
//				ResultInfo<String> errorMessage = errorMessages.get(storageMessageKey);
//				if (log.isDebugEnabled()) {
//					log.debug("下单错误：商品库存不足：" + "SKUCODE:" + storageMessageKey + errorMessage.getMsg().getMessage());
//				}
//			}
//			throw new OrderServiceException(OrderErrorCodes.CHECK_SKU_STOCK_ERROR);
//		}
	}

	/**
	 * <pre>
	 * 校验商品信息
	 * </pre>
	 * 
	 * @param skuCodes
	 */
	private void checkItemInfo(List<String> skuCodes) {
		try {
			ResultInfo<Boolean> itemResultMessage = itemService.checkItemList(skuCodes);
			if (!itemResultMessage.success) {
				log.error("下单错误：货物信息错误，商品SKUCODE为：" + itemResultMessage.getMsg().getMessage());
				throw new OrderServiceException(OrderErrorCodes.CHECK_SKU_INFO_ERROR);
			}
		} catch (ItemServiceException e) {
			log.error("检验商品信息抛出异常:入参:{},errorinfo:{}", JSONArray.fromObject(skuCodes).toString(), e);
			throw new OrderServiceException(OrderErrorCodes.CHECK_SKU_INFO_ERROR);
		}
	}

	/**
	 * <pre>
	 * 参数校验
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 * @param userMenber
	 * @param ip
	 * @param ip
	 * @param orderSource
	 */
	private void argumentValidation(OrderInfoDTO orderInfoDTO, MemberInfo userMenber, String ip, Integer orderSource) {
		ParamValidator validator = new ParamValidator("创建订单");

		validator.notNull(userMenber, "用户信息");
		validator.notNull(userMenber.getId(), "用户id");
		// validator.notNull(userMenber.getUsername(), "账户名");
		validator.notNull(orderInfoDTO, "订单信息");
		validator.notNull(orderInfoDTO.getConsigneeId(), "收货人id");
		validator.notNull(orderInfoDTO.getCartType(), "购物车类型");
		if (CartConstant.TYPE_COMMON == orderInfoDTO.getCartType().intValue()) {
			validator.range(orderInfoDTO.getPayType(), OrderPayType.COD, "支付方式");
		}
		validator.notNull(orderInfoDTO.getIsNeedInvoice(), "是否需要发票");

		if (OrderReceiptConstant.NEED_INVOICE.TRUE.code.equals(orderInfoDTO.getIsNeedInvoice())) { // 需要开发票的时候
			if (OrderReceiptConstant.ReceiptTitleType.CORPORATION.code.equals(orderInfoDTO.getInvoiceType())) { // 企业抬头
				validator.notNull(orderInfoDTO.getInvoiceTitle(), "发票抬头");
			}
		}
		validator.range(orderSource, PlatformEnum.WAP, "订单来源");
	}

	/**
	 * <pre>
	 * 设置商品优惠信息
	 * </pre>
	 * 
	 * @param orderLineMapTopicItemInfo
	 * @param orderLineMapDiscount
	 * @param orderCoupon
	 * @return orderLineMapDiscount
	 */
	private Map<OrderItem, List<OrderPromotion>> setPromotionInfo(
			Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo, OrderCouponDTO orderCoupon,
			Map<OrderItem, Double> orderLineMapDiscount,Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion) {
		// 将促销优惠信息加入MAP
		Iterator<OrderItem> orderLineKit = orderLineMapDiscount.keySet().iterator();
		OrderItem maxOrderItem = null;
		BigDecimal balance = BigDecimal.ZERO;
		while (orderLineKit.hasNext()) {
			OrderItem orderItem = orderLineKit.next();
			Double discount = orderLineMapDiscount.get(orderItem);
			if(CouponType.HAS_CONDITION.ordinal()==orderCoupon.getCouponType()){
				if(orderItem.getItemAmount()>=discount){
					orderItem.setItemAmount(formatToPrice(subtract(orderItem.getItemAmount(),discount)).doubleValue());
					if(null==maxOrderItem){
						maxOrderItem = orderItem;
					}else if(maxOrderItem.getItemAmount()<orderItem.getItemAmount()){
						maxOrderItem = orderItem;
					}
					orderItem.setOrderCouponAmount(formatToPrice(add(orderItem.getOrderCouponAmount(),discount)).doubleValue());
				}else{
					orderItem.setOrderCouponAmount(formatToPrice(add(orderItem.getOrderCouponAmount(),orderItem.getItemAmount())).doubleValue());
					balance = add(balance,subtract(discount,orderItem.getItemAmount()));
					orderItem.setItemAmount(ZERO);
				}
			}else{
				Double subTotal = add(add(orderItem.getItemAmount(),orderItem.getTaxFee()),orderItem.getFreight()).doubleValue();
				if(subTotal>0 && subTotal>=discount){
					Double itemAmount = formatToPrice(multiply(discount,divide(orderItem.getItemAmount(),subTotal,6))).doubleValue();
					Double taxFee = formatToPrice(multiply(discount,divide(orderItem.getTaxFee(),subTotal,6))).doubleValue();
					Double freight = formatToPrice(multiply(discount,divide(orderItem.getFreight(),subTotal,6))).doubleValue();
					if(discount!=add(add(itemAmount,taxFee),freight).doubleValue()){
						itemAmount = formatToPrice(add(itemAmount,subtract(discount,add(add(itemAmount,taxFee),freight)))).doubleValue();
					}
					orderItem.setItemAmount(formatToPrice(subtract(orderItem.getItemAmount(),itemAmount)).doubleValue());
					orderItem.setTaxFee(formatToPrice(subtract(orderItem.getTaxFee(),taxFee)).doubleValue());
					orderItem.setFreight(formatToPrice(subtract(orderItem.getFreight(),freight)).doubleValue());
					if(null==maxOrderItem){
						maxOrderItem = orderItem;
					}else if(maxOrderItem.getItemAmount()<orderItem.getItemAmount()){
						maxOrderItem = orderItem;
					}
					if(CouponType.NO_CONDITION.ordinal()==orderCoupon.getCouponType()){
						orderItem.setCouponAmount(formatToPrice(add(orderItem.getCouponAmount(),discount)).doubleValue());
					}
				}else{
					balance = add(balance,subtract(discount,subTotal));
					if(CouponType.NO_CONDITION.ordinal()==orderCoupon.getCouponType()){
						orderItem.setCouponAmount(formatToPrice(add(orderItem.getCouponAmount(),add(add(orderItem.getItemAmount(),orderItem.getTaxFee()),orderItem.getFreight()))).doubleValue());
					}
					orderItem.setItemAmount(ZERO);
					orderItem.setTaxFee(ZERO);
					orderItem.setFreight(ZERO);
				}
			}
			orderItem.setSubTotal(formatToPrice(add(add(orderItem.getItemAmount(),orderItem.getTaxFee()),orderItem.getFreight())).doubleValue());
			// 设置促销优惠信息
			OrderPromotion orderPromotion = new OrderPromotion();
			orderPromotion.setType(PromotionType.ALL.code);
			orderPromotion.setCouponCode(String.valueOf(orderCoupon.getCouponUserId()));// 优惠券码
			orderPromotion.setCouponFaceAmount(orderCoupon.getFaceValue().doubleValue());
			orderPromotion.setCouponType(orderCoupon.getCouponType());
			orderPromotion.setSourceType(orderCoupon.getSourceType());
			orderPromotion.setDiscount(discount);
			orderPromotion.setSupplierId(orderCoupon.getSourceId());
			orderPromotion.setPromoterId(orderCoupon.getPromoterId());
			// 加入商品促销信息
			orderPromotion.setPromotionId(orderLineMapTopicItemInfo.get(orderItem).getTopicId());
			orderPromotion.setPromotionName(orderLineMapTopicItemInfo.get(orderItem).getTopicName());
			List<OrderPromotion> tempOrderPromotionList = orderLineMapPromotion.get(orderItem);
			if(null==tempOrderPromotionList){
				tempOrderPromotionList = new ArrayList<OrderPromotion>();
			}
			tempOrderPromotionList.add(orderPromotion);
			orderLineMapPromotion.put(orderItem, tempOrderPromotionList);
		}
		if(balance.doubleValue()!=0 && maxOrderItem!=null){
			maxOrderItem.setSubTotal(formatToPrice(subtract(maxOrderItem.getSubTotal(),balance)).doubleValue());
			maxOrderItem.setItemAmount(formatToPrice(subtract(maxOrderItem.getItemAmount(),balance)).doubleValue());
		}
		return orderLineMapPromotion;
	}

	/**
	 * <pre>
	 * 设置子订单和订单行相关信息
	 * </pre>
	 * 
	 * @param orderItemMapByStorage
	 * @param salesOrderDO
	 * @param subOrderMapOrderLine
	 * @param orderLineMapTopicItemInfo
	 * @param itemsRequests
	 */
	private Map<SubOrder, List<OrderItem>> setSubOrderAndOrderLIneInfo(
			Map<Long, List<CartLineDTO>> orderItemMapByStorage, OrderInfo salesOrderDO,
			Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo, List<OccupyInventoryDto> occupyInventorys,
			final Map<String, ItemResultDto> itemMap) {
		Map<SubOrder, List<OrderItem>> subOrderMapOrderLine = new LinkedHashMap<SubOrder, List<OrderItem>>();
		List<CartLineDTO> tempCartLineDTOs = null;
		
		Iterator<Long> kit = orderItemMapByStorage.keySet().iterator();

		while (kit.hasNext()) {
			Long key = (Long) kit.next();
			tempCartLineDTOs = orderItemMapByStorage.get(key);
			List<OccupyInventoryDto> subOccupyInventoryList = new ArrayList<OccupyInventoryDto>();
			// 记录订单行list
			List<OrderItem> tempOrderLineLists = new ArrayList<OrderItem>();
			double totalSalePrice = 0d;
			int quantity = 0;
			setSupplierInfoAlias(tempCartLineDTOs);
			for (CartLineDTO cartLineDTO : tempCartLineDTOs) {
				totalSalePrice = BigDecimalUtil.add(totalSalePrice, cartLineDTO.getSubTotal()).doubleValue();// 计算商品总价
				quantity += cartLineDTO.getQuantity();// 计算商品行商品总数
				// 保存订单商品行信息
				OrderItem tempOrderLine = setOrderLineInfo(cartLineDTO);// 设置订单行通用信息
				ItemResultDto item = itemMap.get(cartLineDTO.getSkuCode());
				if (null != item) {
					tempOrderLine.setBarCode(item.getBarcode());
				}
				// 记录订单行和促销信息map
				orderLineMapTopicItemInfo.put(tempOrderLine, cartLineDTO.getTopicItemInfo());
				// 将订单行信息插入订单行List
				tempOrderLineLists.add(tempOrderLine);

				// 减库存参数获取
				subOccupyInventoryList.add(setReduceStockLists(cartLineDTO));
			}

			// 设置子订单通用信息

			SubOrder tempSubOrder = setSubOrderInfo(salesOrderDO, tempCartLineDTOs);
			tempSubOrder.setOriginalTotal(totalSalePrice);// 子单应付总价（打折前，但不含运费）
			tempSubOrder.setQuantity(quantity);
			tempSubOrder.setTaxFee(0d);// 设置税费

			if (CollectionUtils.isNotEmpty(subOccupyInventoryList)) {
				for (OccupyInventoryDto occupyInventoryDto : subOccupyInventoryList) {
					occupyInventoryDto.setOrderCode(tempSubOrder.getOrderCode());
				}
			}
			occupyInventorys.addAll(subOccupyInventoryList);
			// 记录子订单和订单行MAP
			subOrderMapOrderLine.put(tempSubOrder, tempOrderLineLists);
		}
		return subOrderMapOrderLine;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 设置子订单通用信息
	 * </pre>
	 * 
	 * @param salesOrderDO
	 * @param tempCartLineDTOs
	 * @return tempSubOrder
	 */
	private SubOrder setSubOrderInfo(OrderInfo salesOrder, List<CartLineDTO> tempCartLineDTOs) {
		SubOrder tempSubOrder = new SubOrder();
		tempSubOrder.setRemark(salesOrder.getRemark());// 订单remark
		tempSubOrder.setSupplierName(tempCartLineDTOs.get(0).getSupplierName());
		tempSubOrder.setSupplierAlias(tempCartLineDTOs.get(0).getSupplierName());
		if(StringUtils.isNotBlank(tempCartLineDTOs.get(0).getSupplierAlias())){
			tempSubOrder.setSupplierAlias(tempCartLineDTOs.get(0).getSupplierAlias());
		}
		tempSubOrder.setSupplierId(tempCartLineDTOs.get(0).getSupplierId());
		tempSubOrder.setOrderStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);// 1：等待付款
		tempSubOrder.setWarehouseId(tempCartLineDTOs.get(0).getWarehouseId());
		tempSubOrder.setWarehouseName(tempCartLineDTOs.get(0).getWarehouseName());
		// tempSubOrder.setBalance(balance);
		tempSubOrder.setDeleted(SubOrderConstant.DELETED_FALSE);// 逻辑删除 -否
		tempSubOrder.setOrderCode(orderService.generateCode(OrderCodeType.SON));// 设置子订单编号
		tempSubOrder.setMemberId(salesOrder.getMemberId());
		tempSubOrder.setAccountName(salesOrder.getAccountName());
		tempSubOrder.setParentOrderCode(salesOrder.getParentOrderCode());// 父订单code
		tempSubOrder.setDiscount(0d);// 初始化订单折扣
		tempSubOrder.setFreight(0d);// 初始化子订单运费
		tempSubOrder.setShopPromoterId(salesOrder.getShopPromoterId());
		tempSubOrder.setPromoterId(salesOrder.getPromoterId());
		tempSubOrder.setScanPromoterId(salesOrder.getScanPromoterId());
		tempSubOrder.setSource(salesOrder.getSource());
		// 设置type
		if (SalesOrderConstant.SALE_TYPE_OWN == tempCartLineDTOs.get(0).getSaleType().intValue()) {
			tempSubOrder.setType(SubOrderConstant.GENERAL_ORDER);// 自营
		} else if (SalesOrderConstant.SALE_TYPE_PLATFORM == tempCartLineDTOs.get(0).getSaleType().intValue()) {
			tempSubOrder.setType(SubOrderConstant.PLATFORM_ORDER);// 第三方
		} else if (SalesOrderConstant.SALE_TYPE_SEA == tempCartLineDTOs.get(0).getSaleType().intValue()) {
			tempSubOrder.setType(tempCartLineDTOs.get(0).getStorageType());// 海淘订单类型
			tempSubOrder.setSeaChannel(tempCartLineDTOs.get(0).getSeaChannel());// 海淘渠道
			tempSubOrder.setSeaChannelName(tempCartLineDTOs.get(0).getSeaChannelName());
			tempSubOrder.setPutStatus(SubOrderConstant.PutStatus.NEW.code);// 推送到仓库的状态（0:等待推送，1：已推送，2：推送失败）
		}
		return tempSubOrder;
	}

	/**
	 * 
	 * <pre>
	 * 设置减库存参数List
	 * </pre>
	 * 
	 * @param skuInventoryQuerys
	 * @param cartLineDTO
	 * @param orderCode
	 */
	private OccupyInventoryDto setReduceStockLists(CartLineDTO cartLineDTO) {
		OccupyInventoryDto occupyInventoryDto = new OccupyInventoryDto();
		occupyInventoryDto.setApp(App.PROMOTION);// 请求类型
		occupyInventoryDto.setBizId(cartLineDTO.getTopicId().toString());// 设置业务ID,对应促销ID
		occupyInventoryDto.setInventory(cartLineDTO.getQuantity());
		occupyInventoryDto.setSku(cartLineDTO.getSkuCode());
		return occupyInventoryDto;

	}

	/**
	 * 
	 * <pre>
	 * 设置发票信息
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 */
	private OrderReceipt setReceiptInfo(OrderInfoDTO orderInfoDTO) {
		OrderReceipt orderReceipt = new OrderReceipt();
		orderReceipt.setTitle(orderInfoDTO.getInvoiceTitle());// 发票抬头
		orderReceipt.setType(orderInfoDTO.getInvoiceCarrier());// 发票类型（1：普通纸质。2：电子票。3：增值税发票）
		orderReceipt.setTitleType(orderInfoDTO.getInvoiceType());// 抬头类型（1.个人
		return orderReceipt;
	}

	/**
	 * 
	 * <pre>
	 * 设置校校验商品库存参数list
	 * </pre>
	 * 
	 * @param cartLines
	 */
	private List<String> setCheckItemInfoList(List<CartLineDTO> cartLines) {
		List<String> skuCodes = new ArrayList<String>();
		for (CartLineDTO cartLineDTO : cartLines) {
			// 设置商品SKUcode List
			skuCodes.add(cartLineDTO.getSkuCode());
		}
		return skuCodes;
	}

	/**
	 * 
	 * <pre>
	 * 设置校验商品库存参数list
	 * </pre>
	 * 
	 * @param cartLines
	 * @return SkuInventoryQuerys
	 */
	private List<SkuInventoryQuery> setCheckStockInfoList(List<CartLineDTO> cartLines) {
		List<SkuInventoryQuery> skuInventoryQuerys = new ArrayList<SkuInventoryQuery>();// 库存检验参数List
		for (CartLineDTO cartLineDTO : cartLines) {
			// 设置商品SKUcode List
			SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
			skuInventoryQuery.setApp(App.PROMOTION);
			skuInventoryQuery.setQuantity(cartLineDTO.getQuantity());
			skuInventoryQuery.setBizId(cartLineDTO.getTopicId().toString());
			skuInventoryQuery.setSku(cartLineDTO.getSkuCode());
			skuInventoryQuerys.add(skuInventoryQuery);
		}
		return skuInventoryQuerys;
	}

	/**
	 * 
	 * <pre>
	 * 根据销售模式拆分订单商品数据
	 * </pre>
	 * 
	 * @param cartLineDTOs
	 * @param xgCartLines
	 * @param platformCartLines
	 */
	private void sliptOrderItemBySaleType(List<CartLineDTO> cartLineDTOs, List<CartLineDTO> xgCartLines,
			List<CartLineDTO> platformCartLines) {

		for (CartLineDTO cartLineDTO : cartLineDTOs) {
			// 将商品（购物车行）数据按照销售模式拆分
			if (SalesOrderConstant.SALE_TYPE_OWN == (cartLineDTO.getSaleType().intValue())) {// 销售模式：0-自营
				xgCartLines.add(cartLineDTO);
			}
			if (SalesOrderConstant.SALE_TYPE_PLATFORM == cartLineDTO.getSaleType().intValue()
					|| SalesOrderConstant.SALE_TYPE_SEA == cartLineDTO.getSaleType().intValue()) {// 销售模式：1-平台
																									// 2-海淘
				platformCartLines.add(cartLineDTO);

			}

		}

	}

	/**
	 * 
	 * <pre>
	 * 根据供应商拆分订单商品数据
	 * </pre>
	 * 
	 * @param cartLineDTOs
	 */
	private Map<Long, List<CartLineDTO>> sliptOrderItemBySupplier(List<CartLineDTO> cartLineDTOs) {
		Map<Long, List<CartLineDTO>> orderItemMap = new LinkedHashMap<Long, List<CartLineDTO>>();
		List<CartLineDTO> tempCartLineDTOs = null;
		for (CartLineDTO cartLineDTO : cartLineDTOs) {
			// 将商品（购物车行）数据按照供应商拆分
			if (orderItemMap.containsKey(cartLineDTO.getSupplierId())) {
				tempCartLineDTOs = orderItemMap.get(cartLineDTO.getSupplierId());
			} else {
				tempCartLineDTOs = new ArrayList<CartLineDTO>();
			}
			tempCartLineDTOs.add(cartLineDTO);
			orderItemMap.put(cartLineDTO.getSupplierId(), tempCartLineDTOs);
		}
		return orderItemMap;
	}

	/**
	 * 
	 * <pre>
	 * 根据自营商品所在仓库拆分订单商品数据
	 * </pre>
	 */
	private Map<Long, List<CartLineDTO>> sliptOrderItemByStorage(List<CartLineDTO> cartLineDTOs) {
		Map<Long, List<CartLineDTO>> xgOrderItemMapByStorage = new LinkedHashMap<Long, List<CartLineDTO>>();
		List<CartLineDTO> tempCartLineDTOs = null;
		for (CartLineDTO cartLineDTO : cartLineDTOs) {// 根据仓库ID取出相应的购物车行
			// 将商品（购物车行）数据按照仓库拆分
			if (xgOrderItemMapByStorage.containsKey(cartLineDTO.getWarehouseId())) {
				tempCartLineDTOs = xgOrderItemMapByStorage.get(cartLineDTO.getWarehouseId());
			} else {
				tempCartLineDTOs = new ArrayList<CartLineDTO>();
			}
			tempCartLineDTOs.add(cartLineDTO);
			xgOrderItemMapByStorage.put(cartLineDTO.getWarehouseId(), tempCartLineDTOs);
		}
		return xgOrderItemMapByStorage;
	}

	/**
	 * 
	 * <pre>
	 * 根据第三方商品所在仓库拆分订单商品数据
	 * </pre>
	 * 
	 * @param orderItemMapBySupplier
	 */
	private Map<Long, Map<Long, List<CartLineDTO>>> sliptOrderItemByStorage(
			Map<Long, List<CartLineDTO>> orderItemMapBySupplier) {
		Map<Long, Map<Long, List<CartLineDTO>>> orderItemMapByStorage = new LinkedHashMap<Long, Map<Long, List<CartLineDTO>>>();
		List<CartLineDTO> tempCartLineDTOs = null;
		// 根据供应商ID取出相应的购物车行
		Iterator<Long> kit = orderItemMapBySupplier.keySet().iterator();
		while (kit.hasNext()) {
			Long key = (Long) kit.next();

			Map<Long, List<CartLineDTO>> tempOrderItemMap = new LinkedHashMap<Long, List<CartLineDTO>>();
			List<CartLineDTO> cartLineDTOs = orderItemMapBySupplier.get(key);
			for (CartLineDTO cartLineDTO : cartLineDTOs) {
				// 将商品（购物车行）数据按照仓库拆分
				if (tempOrderItemMap.containsKey(cartLineDTO.getWarehouseId())) {
					tempCartLineDTOs = tempOrderItemMap.get(cartLineDTO.getWarehouseId());
				} else {
					tempCartLineDTOs = new ArrayList<CartLineDTO>();
				}
				tempCartLineDTOs.add(cartLineDTO);
				tempOrderItemMap.put(cartLineDTO.getWarehouseId(), tempCartLineDTOs);
			}
			// 按供应商ID再插入map
			orderItemMapByStorage.put(key, tempOrderItemMap);
		}
		return orderItemMapByStorage;
	}

	/**
	 * <pre>
	 * 计算优惠商品总金额
	 * </pre>
	 * 
	 * @param subOrderMapOrderLine
	 * @param skuCodeList
	 * @param orderLineMapPromotion
	 * @param couponType
	 * @return
	 */
	private BigDecimal getDiscountOrderSaleSum(Map<SubOrder, List<OrderItem>> subOrderMapOrderLine,
			List<String> skuCodeList,int couponType) {
		BigDecimal realSum = BigDecimal.ZERO;
		Iterator<SubOrder> subOrderKit = subOrderMapOrderLine.keySet().iterator();
		while (subOrderKit.hasNext()) {
			SubOrder subOrderKey = (SubOrder) subOrderKit.next();
			List<OrderItem> orderLines = subOrderMapOrderLine.get(subOrderKey);
			for (OrderItem orderLine : orderLines) {
				if (skuCodeList.contains(orderLine.getSkuCode())) {// 假如是优惠商品
					realSum = add(realSum, orderLine.getItemAmount());
					// 假如是红包的时候要加上税费和运费
					if (CouponType.NO_CONDITION.ordinal() == couponType
						|| CouponType.FIRST_MINUS.ordinal()==couponType) {// 红包
						realSum = add(add(realSum, orderLine.getFreight()),orderLine.getTaxFee());// 加上税费
					}
				}
			}
		}
		return realSum;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 设置收货地址信息
	 * </pre>
	 * 
	 * @param userId
	 * @param consigneeId
	 */
	private OrderConsignee setOrderConsigneeInfo(Long userId, Long consigneeId) {
		OrderConsignee orderConsignee = new OrderConsignee();
		// 接口传入参数设置
		ConsigneeAddress consigneeAddress = new ConsigneeAddress();
		consigneeAddress.setId(consigneeId);
		consigneeAddress.setUserId(userId);
		consigneeAddress.setState(Boolean.TRUE);// 有效地址
		// 调用户接口获取收货地址信息
		List<ConsigneeAddress> consigneeAddresss = consigneeAddressService.queryByObject(consigneeAddress);
		if (CollectionUtils.isEmpty(consigneeAddresss)) {
			log.error("下单错误：收货地址为空！入参:{}", JSONObject.fromObject(consigneeAddress).toString());
			throw new OrderServiceException("收货地址为空");
		}
		// 设置收货地址信息
		ConsigneeAddress consigneeAddressDto = consigneeAddresss.get(0);
		orderConsignee.setName(consigneeAddressDto.getName());// 收货人
		orderConsignee.setAddress(consigneeAddressDto.getAddress());
		orderConsignee.setProvinceId(consigneeAddressDto.getProvinceId());
		orderConsignee.setProvinceName(consigneeAddressDto.getProvince());
		orderConsignee.setCityId(consigneeAddressDto.getCityId());
		orderConsignee.setCityName(consigneeAddressDto.getCity());
		orderConsignee.setCountyId(consigneeAddressDto.getCountyId());
		orderConsignee.setCountyName(consigneeAddressDto.getCounty());
		orderConsignee.setTownId(consigneeAddressDto.getStreetId());
		orderConsignee.setTownName(consigneeAddressDto.getStreet());
		orderConsignee.setEmail(consigneeAddressDto.getEmail());
		orderConsignee.setMobile(consigneeAddressDto.getMobile());
		orderConsignee.setPostcode("020000");
		orderConsignee.setTelephone(consigneeAddressDto.getPhone());
		orderConsignee.setConsigneeId(consigneeAddressDto.getId());// 会员收货地址ID

		return orderConsignee;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 保存父订单信息
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 * @param cartDTO
	 * @return salesOrderDO
	 */
	private OrderInfo setSalesOrderInfo(OrderInfoDTO orderInfoDTO, CartDTO cartDTO) {
		OrderInfo salesOrder = new OrderInfo();
		salesOrder.setPayType(orderInfoDTO.getPayType());// 支付方式（1：在线支付。2：货到付款）
		salesOrder.setPayWay(orderInfoDTO.getPayWay());// 支付途径
		salesOrder.setAreaId(cartDTO.getLineList().get(0).getAreaId());// 大区ID
		salesOrder.setFreight(cartDTO.getRealFee() > 0 ? cartDTO.getRealFee() : 0);// 整单总运费
		salesOrder.setOriginalTotal(cartDTO.getTopicRealSum());// 整单应付总价（打折前，但不含运费）
		salesOrder.setTotal(cartDTO.getRealSum() > 0 ? cartDTO.getRealSum() : 0);// 实付总价（打折后，但不含运费）
		salesOrder.setOrderStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);// 设置付款状态为：等待付款
		salesOrder.setQuantity(cartDTO.getQuantity());// 订单总数量
		salesOrder.setIsReceipt(orderInfoDTO.getIsNeedInvoice());// 设置是否开具发票
		salesOrder.setDeleted(SalesOrderConstant.DELETED_FALSE);// 逻辑删除 - 否
		salesOrder.setParentOrderCode(orderService.generateCode(OrderCodeType.PARENT));// 设置支付流水号
		salesOrder.setRemark(orderInfoDTO.getOrderRemark());// 订单remark
		salesOrder.setShopPromoterId(orderInfoDTO.getShopPromoterId());
		return salesOrder;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 保存订单商品行信息
	 * </pre>
	 * 
	 * @param cartLineDTO
	 * @return tempOrderLine
	 */
	private OrderItem setOrderLineInfo(CartLineDTO cartLineDTO) {
		OrderItem tempOrderLine = new OrderItem();
		tempOrderLine.setListPrice(cartLineDTO.getListPrice());
		tempOrderLine.setQuantity(cartLineDTO.getQuantity());
		tempOrderLine.setPrice(cartLineDTO.getSalePrice());
		tempOrderLine.setSalesType(cartLineDTO.getSaleType());
		tempOrderLine.setImg(cartLineDTO.getItemPic());// 图片路径
		tempOrderLine.setSkuCode(cartLineDTO.getSkuCode());// SKU编号
		tempOrderLine.setBarCode(cartLineDTO.getBarcode());// 商品条形码
		tempOrderLine.setSpuCode(cartLineDTO.getItemCode());// 商品编号
		tempOrderLine.setSpuId(cartLineDTO.getItemId());// 商品ID
		tempOrderLine.setSpuName(cartLineDTO.getItemName());// 商品名称
		tempOrderLine.setBrandName(cartLineDTO.getBrandName());// 商品品牌
		tempOrderLine.setSupplierId(cartLineDTO.getSupplierId());
		tempOrderLine.setSupplierName(cartLineDTO.getSupplierName());
		tempOrderLine.setWarehouseId(cartLineDTO.getWarehouseId());
		tempOrderLine.setTopicId(cartLineDTO.getTopicItemInfo().getTopicId());// 设置促销ID
		tempOrderLine.setUnit(cartLineDTO.getUnit());
		tempOrderLine.setTaxFee(0d);
		// 设置销售属性
		tempOrderLine.setSalesProperty(JsonFormatUtils.format(cartLineDTO.getSalePropertyList()));// 设置销售属性
		if (null == cartLineDTO.getWeightNet() || null == cartLineDTO.getQuantity()) {// 如果weight为空,或者数量为空
			tempOrderLine.setWeight(0D);
		} else {
			tempOrderLine.setWeight(BigDecimalUtil.multiply(cartLineDTO.getWeight(), cartLineDTO.getQuantity())
					.doubleValue());// 设置重量
		}
		tempOrderLine.setTaxRate(cartLineDTO.getTarrifRate());// 税率

		if (SalesOrderConstant.SALE_TYPE_SEA == cartLineDTO.getSaleType().intValue()) {// 海淘
			tempOrderLine.setTaxFee(cartLineDTO.getTaxfFee());// 设置海淘税费
			tempOrderLine.setProductCode(cartLineDTO.getProductCode());
			tempOrderLine.setRefundDays(cartLineDTO.getRefundDays());
		}
		if (SalesOrderConstant.SALE_TYPE_SEA == cartLineDTO.getSaleType().intValue()) {
			tempOrderLine.setOriginalSubTotal(BigDecimalUtil.add(cartLineDTO.getSubTotal(), cartLineDTO.getTaxfFee())
					.doubleValue());// 订单行原始金额(按促销价钱计算)+税费
		} else {
			tempOrderLine.setOriginalSubTotal(cartLineDTO.getSubTotal());
		}
		tempOrderLine.setFreight(0d);// 初始化行运费
		
		//设置活动是否预占库存（5.4）
		tempOrderLine.setTopicInventoryFlag(cartLineDTO.getTopicItemInfo().getTopicInventoryFlag());
		return tempOrderLine;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 计算子订单运费
	 * </pre>
	 * 
	 ** @param cartDTO
	 * @param subOrderMapOrderLine
	 * @param totalFreight
	 */
	private void calculateSubOrderFreight(CartDTO cartDTO, Map<SubOrder, List<OrderItem>> subOrderMapOrderLine,
			BigDecimal totalFreight) {
		BigDecimal freightUsedSum = BigDecimal.ZERO;
		BigDecimal realSum = new BigDecimal(cartDTO.getTopicRealSum());
		OrderItem maxFreightOrderLine = null;
		SubOrder maxFreigthSubOrder = null;
		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEntry : subOrderMapOrderLine.entrySet()) {
			SubOrder subOrder = subOrderEntry.getKey();
			List<OrderItem> orderLineList = subOrderEntry.getValue();

			BigDecimal subOrderFreight = BigDecimal.ZERO;
			for (OrderItem orderLine : orderLineList) {
				BigDecimal tempFreight = BigDecimalUtil.divide(
						BigDecimalUtil.multiply(orderLine.getOriginalSubTotal(), totalFreight), realSum);
				orderLine.setFreight(BigDecimalUtil.formatToPrice(tempFreight).doubleValue());
				subOrderFreight = BigDecimalUtil.add(subOrderFreight, BigDecimalUtil.formatToPrice(tempFreight));

				if (maxFreightOrderLine == null
						|| maxFreightOrderLine.getFreight().doubleValue() < orderLine.getFreight()) {
					maxFreightOrderLine = orderLine;
					maxFreigthSubOrder = subOrder;
				}
			}
			freightUsedSum = BigDecimalUtil.add(freightUsedSum, subOrderFreight);
			subOrder.setFreight(BigDecimalUtil.formatToPrice(subOrderFreight).doubleValue());
			if (subOrder.getFreight() < 0) {
				subOrder.setFreight(0.00);
			}
		}

		double balance = BigDecimalUtil.formatToPrice(freightUsedSum.subtract(totalFreight)).doubleValue();
		if (balance != 0) {
			maxFreightOrderLine.setFreight(BigDecimalUtil.formatToPrice(
					BigDecimalUtil.add(maxFreightOrderLine.getFreight(), balance)).doubleValue());
			maxFreigthSubOrder.setFreight(BigDecimalUtil.formatToPrice(
					BigDecimalUtil.add(maxFreigthSubOrder.getFreight(), balance)).doubleValue());
		}
	}

	/**
	 * 
	 * <pre>
	 * 将优惠券拆分到订单行
	 * </pre>
	 * 
	 * @param subOrderMap
	 * @param discount
	 * @param orderLineMapDiscount
	 * @param skuCodeList
	 * @param orderLineMapPromotion
	 */
	private Map<OrderItem,Double> calculateOrderLineCouponDiscount(Map<SubOrder, List<OrderItem>> subOrderMap,
			OrderCouponDTO orderCoupon, List<String> skuCodeList) {
		BigDecimal discount = new BigDecimal(orderCoupon.getFaceValue());
		BigDecimal usedCouponDiscount = BigDecimal.ZERO;// 记录本次订单商品已享受折扣总数
		// 计算优惠商品总价
		BigDecimal realSum = getDiscountOrderSaleSum(subOrderMap, skuCodeList,orderCoupon.getCouponType().intValue());
		Map<OrderItem,Double> orderItemDiscountMap = new HashMap<OrderItem,Double>();
		OrderItem maxOrderItem = null;
		for (Map.Entry<SubOrder, List<OrderItem>> subOrderEntry : subOrderMap.entrySet()) {
			List<OrderItem> orderLineList = subOrderEntry.getValue();
			for (OrderItem orderLine : orderLineList) {
				if (skuCodeList.contains(orderLine.getSkuCode())) {
					if (BigDecimalUtil.formatToPrice(realSum).compareTo(BigDecimalUtil.formatToPrice(discount)) < 1) {
						Double itemSubTotal = orderLine.getItemAmount();
						if (CouponType.NO_CONDITION.ordinal() == orderCoupon.getCouponType()
								|| CouponType.FIRST_MINUS.ordinal()==orderCoupon.getCouponType()) {
							itemSubTotal = formatToPrice(add(add(itemSubTotal, orderLine.getTaxFee()),orderLine.getFreight())).doubleValue();
						}
						usedCouponDiscount = usedCouponDiscount.add(new BigDecimal(itemSubTotal));
						orderItemDiscountMap.put(orderLine, itemSubTotal);
					} else {
						if(null==maxOrderItem){
							maxOrderItem = orderLine;
						}
						Double itemSubTotal = orderLine.getItemAmount();
						if(maxOrderItem.getItemAmount()<itemSubTotal){
							maxOrderItem = orderLine;
						}
						if (CouponType.NO_CONDITION.ordinal() == orderCoupon.getCouponType()
							|| CouponType.FIRST_MINUS.ordinal()==orderCoupon.getCouponType()) {// 红包
							itemSubTotal = add(add(itemSubTotal, orderLine.getTaxFee()),orderLine.getFreight()).doubleValue();
						}
						Double itemDiscount = formatToPrice(multiply(discount,divide(itemSubTotal,realSum,6))).doubleValue();
						usedCouponDiscount = usedCouponDiscount.add(new BigDecimal(itemDiscount));
						orderItemDiscountMap.put(orderLine, itemDiscount);
					}
				}
			}
		}
		if(maxOrderItem!=null && usedCouponDiscount.compareTo(discount)!=0){
			orderItemDiscountMap.put(maxOrderItem, formatToPrice(add(orderItemDiscountMap.get(maxOrderItem),subtract(discount,usedCouponDiscount))).doubleValue());
		}
		return orderItemDiscountMap;
	}

	/**
	 * <pre>
	 * 拆分优惠券
	 * </pre>
	 * 
	 * @param orderLineMapTopicItemInfo
	 * @param subOrderMap
	 * @param cartCouponDTO
	 * @return orderLineMapPromotion
	 */
	private Map<OrderItem, List<OrderPromotion>> splitCoupon(
			Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo,
			Map<SubOrder, List<OrderItem>> subOrderMap, CartCouponDTO cartCouponDTO) {
		Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion = new LinkedHashMap<OrderItem, List<OrderPromotion>>();
		if (CollectionUtils.isNotEmpty(cartCouponDTO.getCouponDtoList())) {
			// 按优惠券类型升序排列. 优惠券类型 0 : 满减券 1：现金券
			Collections.sort(cartCouponDTO.getCouponDtoList(), new Comparator<OrderCouponDTO>() {
				public int compare(OrderCouponDTO cl1, OrderCouponDTO cl2) {
					if (cl1.getCouponType() == null || cl2.getCouponType() == null) {
						return 0;
					}
					return cl1.getCouponType().compareTo(cl2.getCouponType());
				}
			});

			for (OrderCouponDTO orderCoupon : cartCouponDTO.getCouponDtoList()) {
				Map<OrderItem, Double> orderLineMapDiscount = new LinkedHashMap<OrderItem, Double>();//订单商品项对应优惠
				List<String> skuCodeList = new ArrayList<String>();
				skuCodeList = cartCouponDTO.getCuidSkuMap().get(orderCoupon.getCouponUserId());
				// 优惠券拆分到商品行
				orderLineMapDiscount = calculateOrderLineCouponDiscount(subOrderMap, orderCoupon, skuCodeList);
				setPromotionInfo(orderLineMapTopicItemInfo, orderCoupon, orderLineMapDiscount,orderLineMapPromotion);
			}
		}
		operateAmount(subOrderMap);
		return orderLineMapPromotion;
	}

	private void operateOrgAmount(Map<SubOrder, List<OrderItem>> subOrderMap){
		subOrderMap.forEach(new BiConsumer<SubOrder,List<OrderItem>>(){
			public void accept(SubOrder t, List<OrderItem> u) {
				BigDecimal total = BigDecimal.ZERO;
				for(OrderItem orderItem:u){
					orderItem.setOriginalSubTotal(toPrice(add(add(orderItem.getOrigItemAmount(),orderItem.getOrigFreight()),orderItem.getOrigTaxFee())));
					orderItem.setSubTotal(orderItem.getOriginalSubTotal());
					orderItem.setItemAmount(formatToPrice(multiply(orderItem.getPrice(),orderItem.getQuantity())).doubleValue());
					total = add(total,orderItem.getOriginalSubTotal());
				}
				t.setOriginalTotal(formatToPrice(total).doubleValue());
			}
		});
	}
	
	private void operateAmount(Map<SubOrder, List<OrderItem>> subOrderMap){
		subOrderMap.forEach(new BiConsumer<SubOrder,List<OrderItem>>(){
			public void accept(SubOrder t, List<OrderItem> u) {
				t.setTotal(ZERO);
				t.setTaxFee(ZERO);
				t.setFreight(ZERO);
				BigDecimal total = BigDecimal.ZERO;
				BigDecimal freigth = BigDecimal.ZERO;
				BigDecimal taxFee = BigDecimal.ZERO;
				for(OrderItem orderItem:u){
					orderItem.setPrice(formatToPrice(divide(orderItem.getItemAmount(),orderItem.getQuantity())).doubleValue());
					total = total.add(add(orderItem.getItemAmount(),orderItem.getTaxFee()));
					freigth = add(freigth,orderItem.getFreight());
					taxFee = add(taxFee,orderItem.getFreight());
				}
				t.setTotal(formatToPrice(total).doubleValue());
				t.setTaxFee(formatToPrice(taxFee).doubleValue());
				t.setFreight(formatToPrice(freigth).doubleValue());
			}
		});
	}
	
	/**
	 * <pre>
	 * 设置海淘购物车相关信息
	 * </pre>
	 * 
	 * @param cartDTO
	 * @return SeaOrderItemDTO
	 */
	@Override
	public SeaOrderItemDTO splitSeaOrder(CartDTO cartDTO) {
		// TO Auto-generated method stub
		SeaOrderItemDTO seaOrderItem = null;
		List<CartLineDTO> cartLineLists = new ArrayList<CartLineDTO>();

		for (Map.Entry<String, List<CartLineDTO>> cartEnty : cartDTO.getSeaMap().entrySet()) {
			List<CartLineDTO> cartLines = cartEnty.getValue();
			cartLineLists.addAll(cartLines);
		}

		// 订单商品信息 key:供应商ID，value=商品（购物车行）信息列表
		Map<Long, List<CartLineDTO>> orderItemMapBySupplier = sliptOrderItemBySupplier(cartLineLists);

		// 再按仓库拆分
		Map<Long, Map<Long, List<CartLineDTO>>> seaOrderItemMapByStorage = sliptOrderItemByStorage(orderItemMapBySupplier);

		// 海淘订单商品信息
		if (null != seaOrderItemMapByStorage && !seaOrderItemMapByStorage.isEmpty()) {// 有海淘订单
			seaOrderItem = buildSeaCartInfo(null,seaOrderItemMapByStorage);
		}
		return seaOrderItem;
	}

	/**
	 * <pre>
	 * 设置海淘购物车相关信息
	 * </pre>
	 * 
	 * @param seaOrderItemMapByStorage
	 * @return SeaOrderItemDTO
	 */
	private SeaOrderItemDTO buildSeaCartInfo(Long memberId,Map<Long, Map<Long, List<CartLineDTO>>> seaOrderItemMapByStorage) {
		// 获取通关渠道编号
		Map<Long, String> channelCodeMap = getChannelCode();

		SeaOrderItemDTO seaOrderItem = new SeaOrderItemDTO();
		seaOrderItem.setMemberId(memberId);
		List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSuppliers = new ArrayList<SeaOrderItemWithSupplierDTO>();
		// platformKey供应商ID
		Iterator<Long> platformKit = seaOrderItemMapByStorage.keySet().iterator();
		while (platformKit.hasNext()) {
			Long platformKey = (Long) platformKit.next();
			Map<Long, List<CartLineDTO>> seaOrderItemMap = seaOrderItemMapByStorage.get(platformKey);
			SeaOrderItemWithSupplierDTO seaOrderItemWithSupplier = new SeaOrderItemWithSupplierDTO();
			List<SeaOrderItemWithWarehouseDTO> seaOrderItemWithWarehouses = new ArrayList<SeaOrderItemWithWarehouseDTO>();
			seaOrderItemWithSupplier.setSupplierId(platformKey);
			for (Map.Entry<Long, List<CartLineDTO>> seaOrderEntry : seaOrderItemMap.entrySet()) {
				Long seaOrderWarehouseId = seaOrderEntry.getKey();
				List<CartLineDTO> lineLists = seaOrderEntry.getValue();
				SeaOrderItemWithWarehouseDTO seaOrderItemWithWarehouse = new SeaOrderItemWithWarehouseDTO();
				seaOrderItemWithWarehouse.setWarehouseId(seaOrderWarehouseId);
				seaOrderItemWithWarehouse.setWarehouseName(lineLists.get(0).getWarehouseName());
				seaOrderItemWithWarehouse.setCartLineList(lineLists);
				seaOrderItemWithWarehouse.setStorageType(lineLists.get(0).getStorageType());
				seaOrderItemWithWarehouse.setSeaChannel(lineLists.get(0).getSeaChannel());
				seaOrderItemWithWarehouse.setSeaChannelCode(channelCodeMap.get(lineLists.get(0).getSeaChannel()));
				seaOrderItemWithWarehouse.setSeaChannelName(lineLists.get(0).getSeaChannelName());
				seaOrderItemWithWarehouses.add(seaOrderItemWithWarehouse);

				seaOrderItemWithSupplier.setSupplierName(lineLists.get(0).getSupplierName());
				seaOrderItemWithSupplier.setFreightTempleteId(lineLists.get(0).getFreightTemplateId());
			}
			seaOrderItemWithSupplier.setSeaOrderItemWithWarehouseList(seaOrderItemWithWarehouses);
			seaOrderItemWithSuppliers.add(seaOrderItemWithSupplier);
		}
		seaOrderItem.setSeaOrderItemWithSupplierList(seaOrderItemWithSuppliers);
		return seaOrderItem;
	}

	/**
	 * 
	 * <pre>
	 * 获取通过渠道编号
	 * </pre>
	 *
	 * @return Map<Long, String> key：通过渠道ID，value：通关渠道编号
	 */
	private Map<Long, String> getChannelCode() {
		Map<Long, String> map = new HashMap<Long, String>();
		List<ClearanceChannels> list = clearanceChannelsService.getAllClearanceChannelsByStatus(2);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ClearanceChannels clearanceChannels : list) {
				map.put(clearanceChannels.getId(), clearanceChannels.getCode());
			}
		}
		return map;
	}

	/**
	 * <pre>
	 * 设置海淘订单相关信息
	 * </pre>
	 * 
	 * @param orderItemMapByStorage
	 * @param salesOrderDO
	 * @param subOrderMapOrderLine
	 * @param orderLineMapTopicItemInfo
	 * @param itemsRequests
	 */
	private Map<SubOrder, List<OrderItem>> setSeaOrderInfo(SeaOrderItemDTO seaOrderItem, OrderInfo salesOrderDO,
			Map<OrderItem, TopicItemInfoDTO> orderLineMapTopicItemInfo, List<OccupyInventoryDto> occupyInventorys,
			final Map<String, ItemResultDto> itemMap) {
		Map<SubOrder, List<OrderItem>> subOrderMapOrderLine = new LinkedHashMap<SubOrder, List<OrderItem>>();		
		for (SeaOrderItemWithSupplierDTO seaOrderItemWithSupplier : seaOrderItem.getSeaOrderItemWithSupplierList()) {
			for (SeaOrderItemWithWarehouseDTO seaOrderItemWithWarehouse : seaOrderItemWithSupplier
					.getSeaOrderItemWithWarehouseList()) {// 子订单
				List<CartLineDTO> tempCartLines = seaOrderItemWithWarehouse.getCartLineList();
				List<OccupyInventoryDto> subOccupyInventoryList = new ArrayList<OccupyInventoryDto>();
				// 记录订单行list
				List<OrderItem> tempOrderLineLists = new ArrayList<OrderItem>();

				double totalPrice = seaOrderItemWithWarehouse.getTotalPrice();
				double taxFees = seaOrderItemWithWarehouse.getActualUsedTaxfee();// 子订单税费
				if (seaOrderItemWithWarehouse.getItemTotalQuantity() > 1) {
					if (totalPrice >= TOTAL_AMOUNT_GOODS) {// 非单件商品金额大于2000
						throw new OrderServiceException(OrderErrorCodes.TOTAL_OVER_THOUSAND,"海淘订单金额超过2000");
					}
				}

				for (CartLineDTO cartLineDTO : tempCartLines) {
					if (taxFees <= 0) {
						cartLineDTO.setTaxfFee(0d);// 不收税
					}
					// 保存订单商品行信息
					OrderItem tempOrderLine = setOrderLineInfo(cartLineDTO);// 设置订单行通用信息
					
					ItemResultDto item = itemMap.get(cartLineDTO.getSkuCode());
					if (null != item) {
						tempOrderLine.setBarCode(item.getBarcode());
					}
					// 记录订单行和促销信息map
					orderLineMapTopicItemInfo.put(tempOrderLine, cartLineDTO.getTopicItemInfo());
					// 将订单行信息插入订单行List
					tempOrderLineLists.add(tempOrderLine);

					// 减库存参数获取
					subOccupyInventoryList.add(setReduceStockLists(cartLineDTO));
				}

				setSupplierInfoAlias(tempCartLines);
				// 设置子订单通用信息
				SubOrder tempSubOrder = setSubOrderInfo(salesOrderDO, tempCartLines);
				tempSubOrder.setOriginalTotal(seaOrderItemWithWarehouse.getTotalPrice());// 子单应付总价（打折前，但不含运费）
				tempSubOrder.setQuantity(seaOrderItemWithWarehouse.getItemTotalQuantity());
				tempSubOrder.setTaxFee(seaOrderItemWithWarehouse.getActualUsedTaxfee());// 设置税费
				tempSubOrder.setFreight(seaOrderItemWithWarehouse.getTotalFreight());
				if (tempSubOrder.getFreight() < 0) {
					tempSubOrder.setFreight(0D);
				}
				if (CollectionUtils.isNotEmpty(subOccupyInventoryList)) {
					for (OccupyInventoryDto occupyInventoryDto : subOccupyInventoryList) {
						occupyInventoryDto.setOrderCode(tempSubOrder.getOrderCode());
					}
				}
				occupyInventorys.addAll(subOccupyInventoryList);
				// 记录子订单和订单行MAP
				subOrderMapOrderLine.put(tempSubOrder, tempOrderLineLists);
			}
		}
		return subOrderMapOrderLine;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 保存海淘父订单信息
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 * @param seaOrderItem
	 * @return salesOrderDO
	 */
	private OrderInfo setSeaSalesOrderInfo(OrderInfoDTO orderInfoDTO, SeaOrderItemDTO seaOrderItem,
			OrderInfo salesOrder) {
		salesOrder.setAreaId(seaOrderItem.getSeaOrderItemWithSupplierList().get(0).getSeaOrderItemWithWarehouseList()
				.get(0).getCartLineList().get(0).getAreaId());// 大区ID
		salesOrder.setFreight(seaOrderItem.getTotalFreight() > 0 ? seaOrderItem.getTotalFreight() : 0);// 整单总运费
		salesOrder.setOriginalTotal(BigDecimalUtil.formatToPrice(
				BigDecimalUtil.add(seaOrderItem.getTotalPrice(), seaOrderItem.getTotalTaxfee())).doubleValue());// 整单应付总价（打折前，含税费，但不含运费）

		// 实付总价（打折后，含税费，但不含运费）
		salesOrder.setTotal(BigDecimalUtil.formatToPrice(
				BigDecimalUtil.subtract(seaOrderItem.getPayPrice(), salesOrder.getFreight())).doubleValue());
		if (salesOrder.getTotal() < 0) {
			salesOrder.setTotal(0D);
		}
		salesOrder.setOrderStatus(OrderConstant.ORDER_STATUS.PAYMENT.code);// 设置付款状态为：等待付款
		salesOrder.setQuantity(seaOrderItem.getQuantity());// 订单总数量
		salesOrder.setIsReceipt(orderInfoDTO.getIsNeedInvoice());// 设置是否开具发票
		salesOrder.setDeleted(SalesOrderConstant.DELETED_FALSE);// 逻辑删除 - 否
		salesOrder.setParentOrderCode(orderService.generateCode(OrderCodeType.PARENT));// 设置支付流水号
		salesOrder.setRemark(orderInfoDTO.getOrderRemark());// 订单remark
		return salesOrder;
	}

	/**
	 * *
	 * 
	 * <pre>
	 * 获取供应商别名
	 * </pre>
	 * 
	 * @param lineList
	 */
	private void setSupplierInfoAlias(final List<CartLineDTO> lineList){
		List<Long> ids = new ArrayList<Long>();
		for(CartLineDTO cartLineDTO:lineList){
			ids.add(cartLineDTO.getSupplierId());
		}
		SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(ids);
		if(null!=supplierResult && CollectionUtils.isNotEmpty(supplierResult.getSupplierInfoList())){
			for(SupplierInfo supplier:supplierResult.getSupplierInfoList()){
				for(CartLineDTO cartLineDTO:lineList){
					if(supplier.getId().equals(cartLineDTO.getSupplierId())){
						cartLineDTO.setSupplierAlias(supplier.getAlias());
					}
				}
			}
		}
	}
	
	/**
     * 首次下单减
     * @param seaOrderItemDTO
     * @return
     */
    public CartDTO setFirstMinus(final CartDTO cartDTO,Integer orderSource){
    	cartDTO.setSourceType(orderSource);
    	if(orderSource!=null && (PlatformEnum.IOS.code == orderSource || PlatformEnum.ANDROID.code==orderSource)){
    		Map<String,Object> params = new HashMap<String,Object>();
        	params.put("memberId", cartDTO.getMemberId());
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " source in ("+PlatformEnum.IOS.code+SPLIT_SIGN.COMMA+PlatformEnum.ANDROID.code+") and order_status !="+OrderConstant.ORDER_STATUS.CANCEL.getCode());
        	Integer count = subOrderService.queryByParamCount(params);
        	if(count==0){
        		cartDTO.firstMinus = Boolean.TRUE;
        	}
    	}
    	return cartDTO;
    }
}
