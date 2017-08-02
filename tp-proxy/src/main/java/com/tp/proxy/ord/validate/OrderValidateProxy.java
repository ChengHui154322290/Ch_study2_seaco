package com.tp.proxy.ord.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.OrderReceiptConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.ord.split.OrderSplitProxy;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.util.StringUtil;

/**
 * 下单时验证
 * @author szy
 *
 */
@Service
public class OrderValidateProxy implements IOrderValidateProxy<OrderInitDto> {
	
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private ICheckTopicRemoteService checkTopicRemoteService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IOrderValidateProxy<ShoppingCartDto> cartValidateProxy;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	@Autowired
	private IGroupbuyInfoService groupbuyInfoService;
	
	
	@Override
	public FailInfo validate(OrderInitDto orderInitDto, FailInfo failInfo){
		failInfo = cartValidateProxy.validate(orderInitDto, failInfo);
		failInfo = validateClearance(orderInitDto, failInfo);
		failInfo = validateItemScope(orderInitDto,failInfo);
		failInfo = validateItemInventoryInfo(orderInitDto,failInfo);
		failInfo = validateToplicMobile(orderInitDto,failInfo);
		failInfo = validateCouponList(orderInitDto,failInfo);
		failInfo = ((CartValidateProxy)cartValidateProxy).validatePassLimit(orderInitDto,failInfo);
		failInfo = validateGroupbuy(orderInitDto,failInfo);
		return failInfo;
	}

	/**
	 *团购相关校验
	 * @param orderInitDto
	 * @param failInfo
     * @return
     */
	private FailInfo validateGroupbuy(OrderInitDto orderInitDto,FailInfo failInfo){
		if(failInfo !=null) return failInfo;
		List<CartItemInfo>  cartItemList = orderInitDto.getCartItemInfoList();
		List<CartItemInfo> groupbuyItems = new ArrayList<>();
		for(CartItemInfo cartItemInfo:cartItemList){
			if(cartItemInfo.getTopicItem()!=null && cartItemInfo.getTopicItem().getTopic()!=null &&
					SalesPartten.GROUP_BUY.getValue().equals(cartItemInfo.getTopicItem().getTopic().getSalesPartten())){
				groupbuyItems.add(cartItemInfo);
			}
		}
		if(groupbuyItems.isEmpty()) return null;

		orderInitDto.setCouponIds(null);

		if(groupbuyItems.size()>1 || orderInitDto.getGroupId() ==null) return new FailInfo("团购商品仅限团购活动中购买");

		ResultInfo checkResult = groupbuyInfoService.checkForOrder(groupbuyItems.get(0).getTopicItem().getTopicId(),groupbuyItems.get(0).getSkuCode(),orderInitDto.getGroupId(),orderInitDto.getMemberId());
		if(!checkResult.isSuccess()){
			return checkResult.getMsg()==null? new FailInfo("团购校验错误") : checkResult.getMsg();
		}

		int boughtCount = checkTopicRemoteService.getBoughtCountWithGroupId(orderInitDto.getGroupId(),orderInitDto.getMemberId());
		if(boughtCount>1){
			return new FailInfo("您已购买本团商品,请参加其他团或开团");
		}
		return null;

	}

	/**
	 * 传入参数验证
	 * @param orderInitDto
	 * @return
	 */
	public FailInfo validateParameters(final OrderInitDto orderInitDto){
		if(StringUtil.isBlank(orderInitDto.getToken())){
			return new FailInfo("没有找到要购买的商品");
		}
		if(null==orderInitDto.getOrderSource()){
			return new FailInfo("暂时不支付此平台下单");
		}
		if(null==orderInitDto.getMemberId()){
			return new FailInfo("会员未登录或超时，请重新登录");
		}
		MemberInfo memberInfo = memberInfoService.queryById(orderInitDto.getMemberId());
		if(null==memberInfo){
			return new FailInfo("你还未注册，请注册后再下单");
		}
		if(null==orderInitDto.getConsigneeId()){
			return new FailInfo("收货信息不能为空");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", orderInitDto.getMemberId());
		params.put("id", orderInitDto.getConsigneeId());
		ConsigneeAddress consigneeAddress = consigneeAddressService.queryUniqueByParams(params);
		if(orderInitDto.getConsigneeId()==0){
			 consigneeAddress=new ConsigneeAddress();
			 consigneeAddress.setId(0l);
		}
		if(null==consigneeAddress && orderInitDto.getConsigneeId()!=0){
			return new FailInfo("根据你填写的收货信息未找到收货地址");
		}else if(hasValidateCard(orderInitDto)){
//			boolean hasHWZY =	hasHWZY(orderInitDto);
			if(orderInitDto.isCheckAuth()  && orderInitDto.getConsigneeId()!=0){
				if(StringUtils.isBlank(consigneeAddress.getIdentityCard())){
					return new FailInfo("请先实名认证",-104);
				}
				//海外直邮需要身份证正反面照片
//				if(hasHWZY && (StringUtils.isBlank(consigneeAddress.getIdentityCard())|| StringUtils.isBlank(consigneeAddress.getFrontImg()) || StringUtils.isBlank(consigneeAddress.getBackImg()))){
//					return new FailInfo("请实名认证并上传身份证正反面照片",-105);
//				}
			}
		}


		if (Constant.TF.YES.equals(orderInitDto.getIsNeedInvoice()) 
		&& OrderReceiptConstant.ReceiptTitleType.CORPORATION.code.equals(orderInitDto.getInvoiceType())
		&& StringUtil.isBlank(orderInitDto.getInvoiceTitle())) {
			return new FailInfo("开企业发票请输入企业抬头");
		}
		//验证通过后把会员及收货信息放入,性能考虑(不建议)
		orderInitDto.setMemberInfo(memberInfo);
		orderInitDto.setConsigneeAddress(consigneeAddress);
		return null;
	}

	private boolean hasHWZY(OrderInitDto orderInitDto) {
		boolean hasHWZY = false;
		boolean needBreak = false;
		if(orderInitDto != null && orderInitDto.getPreSubOrderList()!=null){
            for(PreOrderDto preOrderDto: orderInitDto.getPreSubOrderList()){
                if (needBreak) break;
                if(preOrderDto == null) continue;
                if(preOrderDto.getOrderItemList()!=null){
                    for(OrderItem orderItem: preOrderDto.getOrderItemList()){
                        if(orderItem == null) continue;
                        if(orderItem.getSeaChannel()!=null && orderItem.getSeaChannel().equals(ClearanceChannelsEnum.HWZY.id)){
                            hasHWZY = true;
                            needBreak = true;
                            break;
                        }
                    }
                }
            }
        }
		return hasHWZY;
	}
	
	/**是否需要实名认证*/
	private boolean hasValidateCard(OrderInitDto orderInitDto) {
		if(orderInitDto != null && orderInitDto.getPreSubOrderList()!=null){
            for(PreOrderDto preOrderDto: orderInitDto.getPreSubOrderList()){
                if(preOrderDto == null) continue;
                if(preOrderDto.getOrderItemList()!=null){
                    for(OrderItem orderItem: preOrderDto.getOrderItemList()){
                        if(orderItem == null) continue;
                        if(OrderUtils.isSeaOrder(orderItem.getSalesType()) && orderItem.getSeaChannel()!=null && !OrderConstant.OrderType.DOMESTIC.code.equals(orderItem.getSalesType())){
                        	return true;
                        }
                    }
                }
            }
        }
		return false;
	}
	/**
	 * 验证通关
	 * @param orderInitDto
	 * @param failInfo
	 * @return
	 */
	public FailInfo validateClearance(final OrderInitDto orderInitDto,FailInfo failInfo){
		if(failInfo!=null){
			return failInfo;
		}
		List<PreOrderDto> preSubOrderList = orderInitDto.getPreSubOrderList();
		for(PreOrderDto subOrder:preSubOrderList){
			if(OrderUtils.isSeaOrder(subOrder.getType())
				&& !OrderConstant.OrderType.DOMESTIC.getCode().equals(subOrder.getStorageType())
				&& !ClearanceChannelsEnum.HWZY.id.equals(subOrder.getSeaChannel())
				&& subOrder.getItemTotal() > OrderSplitProxy.CUSTOMS_RATE_LIMIT){
				subOrder.setWarnMessage("你有一单已超过海关规定限额"+OrderSplitProxy.CUSTOMS_RATE_LIMIT);
				return new FailInfo("你有一单已超过海关规定限额"+OrderSplitProxy.CUSTOMS_RATE_LIMIT);
			}
		}
		return null;
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
	private FailInfo validateItemScope(final OrderInitDto orderInitDto,FailInfo failInfo) {
		if(failInfo!=null){
			return failInfo;
		}
		List<TopicItemCartQuery> querys = new ArrayList<TopicItemCartQuery>();
		for (SubOrder subOrder : orderInitDto.getPreSubOrderList()) {
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				public void accept(OrderItem orderItem) {
					TopicItemCartQuery query = new TopicItemCartQuery();
					query.setPlatform(orderInitDto.getOrderSource());
					query.setArea(orderInitDto.getAreaId());
					query.setTopicId(orderItem.getTopicId());
					query.setSku(orderItem.getSkuCode());
					query.setAmount(orderItem.getQuantity());
					query.setMemberId(orderInitDto.getMemberId());
					query.setUip(orderInitDto.getIp());
					querys.add(query);
				}
			});
		}
		ResultInfo<Boolean> result = topicService.validateTopicItemList(querys);
		if (!result.success) {
			failInfo = result.getMsg();
			if (ErrorCodeType.LOCKED.ordinal() == failInfo.getCode()) { // 促销商品已锁定，做库存不足处理
				return new FailInfo("商品库存不足错误",OrderErrorCodes.CHECK_SKU_STOCK_ERROR);
			}
			return failInfo;
		}
		return null;
	}
	
	/**
	 * 校验商品库存
	 * @param skuInventoryQuerys
	 */
	private FailInfo validateItemInventoryInfo(OrderInitDto orderInitDto,FailInfo failInfo) {
		

		if(null!=failInfo){
			return failInfo;
		}
		List<SkuInventoryQuery> skuInventoryQuerys = new ArrayList<SkuInventoryQuery>();// 库存检验参数List
		for (SubOrder subOrder : orderInitDto.getPreSubOrderList()) {
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				public void accept(OrderItem orderItem) {
					SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
					skuInventoryQuery.setApp(App.PROMOTION);
					skuInventoryQuery.setQuantity(orderItem.getQuantity());
					skuInventoryQuery.setBizId(orderItem.getTopicId().toString());
					skuInventoryQuery.setSku(orderItem.getSkuCode());
					skuInventoryQuery.setWarehouseId(orderItem.getWarehouseId());
					skuInventoryQuery.setBizPreOccupy(DEFAULTED.YES.equals(orderItem.getTopicInventoryFlag()));
					skuInventoryQuerys.add(skuInventoryQuery);
				}
			});
		}
//		Map<String, ResultInfo<String>> errorMessages = new LinkedHashMap<String, ResultInfo<String>>();
//		Map<String, ResultInfo<String>> stockMessageMap = inventoryQueryService.checkAllInventory(skuInventoryQuerys);
		Integer errorCount = 0;
		Map<String, Boolean> stockMessageMap = inventoryQueryService.checkSalableInventory(skuInventoryQuerys);
		Iterator<String> storageMessageKit = stockMessageMap.keySet().iterator();
		while (storageMessageKit.hasNext()) {
			String storageMessageKey = (String) storageMessageKit.next();
			Boolean stockResult = stockMessageMap.get(storageMessageKey);
			if (Boolean.FALSE == stockResult) {
				errorCount++;
			}
		}
		if (errorCount > 0) {
			return new FailInfo(OrderErrorCodes.CHECK_SKU_STOCK_ERROR,"商品库存不足");
		}
		return null;
//	
//		if(null!=failInfo){
//			return failInfo;
//		}
//		List<SkuInventoryQuery> skuInventoryQuerys = new ArrayList<SkuInventoryQuery>();// 库存检验参数List
//		for (SubOrder subOrder : orderInitDto.getPreSubOrderList()) {
//			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
//				public void accept(OrderItem orderItem) {
//					SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//					skuInventoryQuery.setApp(App.PROMOTION);
//					skuInventoryQuery.setQuantity(orderItem.getQuantity());
//					skuInventoryQuery.setBizId(orderItem.getTopicId().toString());
//					skuInventoryQuery.setSku(orderItem.getSkuCode());
//					skuInventoryQuerys.add(skuInventoryQuery);
//				}
//			});
//		}
//		Map<String, ResultInfo<String>> errorMessages = new LinkedHashMap<String, ResultInfo<String>>();
//		Map<String, ResultInfo<String>> stockMessageMap = inventoryQueryService.checkAllInventory(skuInventoryQuerys);
//		Iterator<String> storageMessageKit = stockMessageMap.keySet().iterator();
//		while (storageMessageKit.hasNext()) {
//			String storageMessageKey = (String) storageMessageKit.next();
//			ResultInfo<String> stockMessage = stockMessageMap.get(storageMessageKey);
//			if (!stockMessage.success) {
//				errorMessages.put(storageMessageKey, stockMessage);
//			}
//		}
//		if (errorMessages.size() > 0) {
//			return new FailInfo(OrderErrorCodes.CHECK_SKU_STOCK_ERROR,"商品库存不足");
//		}
//		return null;
	}
	
	/**
	 * * 收货人手机限购校验
	 * @param userMenber
	 * @param ip
	 * @param orderSource
	 * @param cartDTO
	 * @param orderConsigneeDO
	 */
	private FailInfo validateToplicMobile(OrderInitDto orderInitDto,FailInfo failInfo) {
		if(null!=failInfo){
			return failInfo;
		}
		/* 获取商品限购信息 */
		List<TopicItemCartQuery> topicQueryList = new ArrayList<TopicItemCartQuery>();
		for (SubOrder subOrder : orderInitDto.getPreSubOrderList()) {
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				public void accept(OrderItem orderItem) {
					TopicItemCartQuery topicItemCartQuery = new TopicItemCartQuery();
					topicItemCartQuery.setMemberId(orderItem.getMemberId());
					topicItemCartQuery.setArea(orderInitDto.getAreaId());
					topicItemCartQuery.setSku(orderItem.getSkuCode());
					topicItemCartQuery.setPlatform(orderInitDto.getPlatformType());
					topicItemCartQuery.setTopicId(orderItem.getTopicId());
					topicItemCartQuery.setAmount(orderItem.getQuantity());
					topicItemCartQuery.setUip(orderInitDto.getIp());
					if(null!=orderInitDto.getConsigneeAddress()){
						topicItemCartQuery.setMobile(orderInitDto.getConsigneeAddress().getMobile());
					}
					topicQueryList.add(topicItemCartQuery);
				}
			});
		}
		List<TopicPolicyDTO> topicPolicyList = new ArrayList<TopicPolicyDTO>();
		try {
			topicPolicyList = topicService.queryTopicPolicyInfo(topicQueryList);
		} catch (Exception e) {
			return new FailInfo(OrderErrorCodes.GET_TOPIC_INFO_ERROR,"获取商品限购信息失败");
		}
		for (TopicPolicyDTO topicPolicyDTO : topicPolicyList) {
			ResultInfo<Boolean> result = checkTopicRemoteService.checkTopicPolicy(topicPolicyDTO);
			if (!result.success) {
				return new FailInfo(OrderErrorCodes.PROMOTION_MOBILE_LIMIT_POLICY,"收货人手机号达到限购");
			}
		}
		return null;
	}
	
	/**
	 * 验证优惠券是否使用
	 * @param userMenber
	 * @param ip
	 * @param orderSource
	 * @param cartDTO
	 * @param orderConsigneeDO
	 */
	public FailInfo validateCouponList(OrderInitDto orderInitDto,FailInfo failInfo) {
		if(null!=failInfo){
			return failInfo;
		}
		List<Long> couponIdList = orderInitDto.getCouponIds();
		if(CollectionUtils.isNotEmpty(couponIdList)){
			List<OrderCouponDTO> orderCouponList = couponUserService.queryCouponUserByIds(couponIdList);
			orderInitDto.setOrderCouponList(orderCouponList);//设置到下单信息，计算订单金额时使用
			if(CollectionUtils.isEmpty(orderCouponList)){
				return new FailInfo("优惠券号码错误");
			}
			for(Long couponId:couponIdList){
				boolean exists = false;
				for(OrderCouponDTO coupon:orderCouponList){
					if(couponId.equals(coupon.getCouponId())){ 
						exists = true;
						if(!coupon.getCouponUserId().equals(orderInitDto.getMemberId())){
							return new FailInfo("不能使用别人的优惠券");
						}
						if (coupon.getCouponStatus() == CouponUserStatus.INVALID.ordinal()) {
							return new FailInfo("优惠券无效");
						} else if (coupon.getCouponStatus() == CouponUserStatus.OVERDUE.ordinal()) {
							return new FailInfo("优惠券过期");
						} else if (coupon.getCouponStatus() == CouponUserStatus.USED.ordinal()) {
							return new FailInfo("优惠券已被使用");
						}
					}
				}
				if(exists){
					return new FailInfo("优惠券号码错误");
				}
			}
		}
		return null;
	}
	/**
	 * 锁定优惠券验证
	 * @param orderInitDto
	 * @param failInfo
	 * @return
	 */
	public FailInfo validateCouponListLock(OrderInitDto orderInitDto,FailInfo failInfo) {
		if(null!=failInfo){
			return failInfo;
		}
		if(CollectionUtils.isNotEmpty(orderInitDto.getCouponIds())){
			for(Long couponId:orderInitDto.getCouponIds()){
				if(!jedisCacheUtil.lock("order:couponUser:"+couponId,10)){
					return new FailInfo("此优惠券正在使用，请等待系统验证");
				}
			}
		}
		return null;
	}
	
	public FailInfo validateFastOrderDeliveryAddress(OrderInitDto orderInitDto,FailInfo failInfo){
		if(null!=failInfo){
			return failInfo;
		}
		ConsigneeAddress consigneeAddress = orderInitDto.getConsigneeAddress();
		for(SubOrder subOrder:orderInitDto.getPreSubOrderList()){
			if(OrderConstant.FAST_ORDER_TYPE.equals(subOrder.getType()) && CollectionUtils.isNotEmpty(subOrder.getDeliveryAddressList())){
				List<Long> deliveryAddressList = subOrder.getDeliveryAddressList();
				if(!deliveryAddressList.contains(consigneeAddress.getStreetId())
				 &&!deliveryAddressList.contains(consigneeAddress.getCountyId()) 
				 &&!deliveryAddressList.contains(consigneeAddress.getCityId())
				 &&!deliveryAddressList.contains(consigneeAddress.getProvinceId())){
					return new FailInfo("您选择的收货地址无法配送");
				}
			}
		};
		return null;
	}
}
