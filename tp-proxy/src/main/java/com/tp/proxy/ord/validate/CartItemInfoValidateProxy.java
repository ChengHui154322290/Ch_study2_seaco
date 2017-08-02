package com.tp.proxy.ord.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.ord.CartConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.model.mmp.TopicItem;
import com.tp.model.ord.CartItemInfo;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IInventoryQueryService;

/**
 * 购物车商品单一验证
 * @author szy
 *
 */
@Service
public class CartItemInfoValidateProxy implements IOrderValidateProxy<ShoppingCartDto> {
	
	@Autowired
	private IItemService itemService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private ICheckTopicRemoteService checkTopicRemoteService;
	@Autowired
	private ITopicItemService topicItemService;

	@Override
	public FailInfo validate(ShoppingCartDto shoppingCartDto, FailInfo failInfo) {
		List<CartItemInfo> cartItemInfoList = shoppingCartDto.getCartItemInfoList();
		//是否有商品
		failInfo = validateCartItemList(cartItemInfoList);
		if(null!=failInfo){
			return failInfo;
		}
		Map<String,String> failInfoMap = new HashMap<String,String>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			FailInfo itemFailInfo = validateCartItem(cartItemInfo,shoppingCartDto);
			cartItemInfo.setFailInfo(itemFailInfo);
			if(null!=itemFailInfo){
				failInfoMap.put(cartItemInfo.getSkuCode(), itemFailInfo.getMessage());
			}
		}
		if(MapUtils.isNotEmpty(failInfoMap)){
			return new FailInfo(failInfoMap);
		}
		return null;
	}

	/**
	 * 验证是否有选中的商品
	 * @param cartItemInfoList
	 * 
	 * @return
	 */
	public FailInfo validateCartItemList(List<CartItemInfo> cartItemInfoList){
		if(CollectionUtils.isEmpty(cartItemInfoList)){
			return new FailInfo("没有选择商品");
		}
		return null;
	}
	
	/**
	 * 验证单个商品
	 * @param cartItemInfo
	 * @return
	 */
	public FailInfo validateCartItem(CartItemInfo cartItemInfo,ShoppingCartDto shoppingCartDto){
		if(StorageConstant.StorageType.FAST.value.equals(cartItemInfo.getStorageType())
		  && !OrderConstant.OrderType.FAST.name().equals(shoppingCartDto.getChannelCode())){
			return new FailInfo("速购商品请到<a href='http://fast.51seaco.com'>速购平台</a>购买");
		}
		/* 单个商品购买数量不能超过99个 */
		if (cartItemInfo.getQuantity() > CartConstant.MAX_SKU_QUANTITY) {
			return new FailInfo(String.format("单个商品购买数量超过最大限制数%d", cartItemInfo.getSkuCode(), CartConstant.MAX_SKU_QUANTITY));
		}
		/* 该商品的有效性检查 */
		ResultInfo<Boolean> rm = itemService.checkItem(cartItemInfo.getSkuCode());
		if (!rm.success) {
			return rm.getMsg();
		}
		/* 获取商品限购信息 */
		TopicPolicyDTO topicPolicy = getTopicPolicyInfo(cartItemInfo, shoppingCartDto.getIp());
		if (null!=topicPolicy) {
			/* 本地限购检查 */
			ResultInfo<Boolean> returnData = checkTopicRemoteService.checkTopicPolicy(topicPolicy);
			if (!returnData.success) {
				FailInfo failInfo = returnData.getMsg();
				if (failInfo.getCode() != null && failInfo.getCode() == OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY) {
					return new FailInfo("用户已达到购买上限 ",OrderErrorCodes.LIMIT_USER_ERROR);
				} else if (failInfo.getCode() != null && failInfo.getCode() == OrderErrorCodes.PROMOTION_IP_LIMIT_POLICY) {
					return new FailInfo("IP已达到购买上限",OrderErrorCodes.LIMIT_IP_ERROR);
				} else {
					return new FailInfo("找不到专题对应的商品 ",OrderErrorCodes.LIMIT_IP_ERROR);
				}
			} else {
				/* 促销限购检查 */
				ResultInfo<Boolean> topicRM = validateSingleTopicItem(cartItemInfo, shoppingCartDto.getIp());
				if (!topicRM.success) {
					FailInfo failInfo = topicRM.getMsg();
					Integer errorCode = failInfo.getCode();
					if (errorCode == ErrorCodeType.LIMIT_AMOUNT.ordinal()) {
						return new FailInfo("商品购买数量超过限购数量",OrderErrorCodes.QUANTITY_SKU_RESTRICTION_ERROR);
					} else if (errorCode== ErrorCodeType.AREA.ordinal()) {
						return new FailInfo("专题不适用当前地区",OrderErrorCodes.AREA_TOPIC_CONSISTENCY__ERROR);
					} else if (errorCode == ErrorCodeType.PLATFORM.ordinal()) {
						return new FailInfo("专题不适用当前平台",OrderErrorCodes.PLATFORM_TOPIC_CONSISTENCY__ERROR);
					} else if (errorCode == ErrorCodeType.REGISTER_TIME.ordinal()) {
						return new FailInfo("用户注册时间不在活动范围内",OrderErrorCodes.REGISTIME_RESTRICTION_ERROR);
					} else if (errorCode == ErrorCodeType.UID.ordinal()) {// 5002
						return new FailInfo("此用户已达到购买上限",OrderErrorCodes.LIMIT_USER_ERROR);
					} else if (errorCode == ErrorCodeType.UIP.ordinal()) {// 5003
						return new FailInfo("此IP已达到购买上限",OrderErrorCodes.LIMIT_IP_ERROR);
					} else if (errorCode == ErrorCodeType.OTHER.ordinal()) {
						return new FailInfo("找不到专题对应的商品",OrderErrorCodes.TOPIC_SKU_CONSISTENCY_ERROR);
					} else if (errorCode == ErrorCodeType.OVERDUE.ordinal()) {
						return new FailInfo("商品已失效，加入购物车失败",OrderErrorCodes.ADDCART_VALIDATE_SKU_ERROR);
					}else if (errorCode == ErrorCodeType.LOCKED.ordinal()) {	// 促销商品锁定，显示库存不足
						return new FailInfo("商品有效库存不足",OrderErrorCodes.QUANTITY_SKU_VALIDATE_ERROR);
					}else{
						return failInfo;
					}
				}
			}
		}
		TopicItem query = new TopicItem();
		query.setTopicId(cartItemInfo.getTopicId());
		query.setSku(cartItemInfo.getSkuCode());
		query.setDeletion(DEFAULTED.NO);
		TopicItem topicItem = topicItemService.queryUniqueByObject(query);
		cartItemInfo.setWarehouseId(topicItem.getStockLocationId());
		cartItemInfo.setSupplierId(topicItem.getSupplierId());
		cartItemInfo.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
		boolean stockCheck = inventoryQueryService.checkInventoryQuantity(App.PROMOTION, cartItemInfo.getTopicId().toString(), cartItemInfo.getSkuCode(), 
				cartItemInfo.getWarehouseId(), DEFAULTED.YES.equals(cartItemInfo.getTopicInventoryFlag()), cartItemInfo.getQuantity());
		
		
		/* 该商品的库存检查 */
//		boolean stockCheck = inventoryQueryService.checkInventoryQuantity(App.PROMOTION, cartItemInfo.getTopicId().toString(), cartItemInfo.getSkuCode(), cartItemInfo.getQuantity());
		if (!stockCheck) {
			return new FailInfo(String.format("所购商品库存不足",cartItemInfo.getItemName()));
		}
		return null;
	}
	
	private TopicPolicyDTO getTopicPolicyInfo(CartItemInfo cartItemInfo, String ip) {
		TopicItemCartQuery query = new TopicItemCartQuery();
		List<TopicItemCartQuery> queryList = new ArrayList<TopicItemCartQuery>();
		query.setPlatform(cartItemInfo.getPlatformId());
		query.setArea(cartItemInfo.getAreaId());
		query.setTopicId(cartItemInfo.getTopicId());
		query.setSku(cartItemInfo.getSkuCode());
		query.setAmount(cartItemInfo.getQuantity());
		query.setMemberId(cartItemInfo.getMemberId());
		query.setUip(ip);
		queryList.add(query);

		try {
			List<TopicPolicyDTO> topicPolicyList = topicService.queryTopicPolicyInfo(queryList);
			if(CollectionUtils.isNotEmpty(topicPolicyList)){
				return topicPolicyList.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param skuCode
	 * @param quantity
	 * @param platformId
	 * @param areaId
	 * @param topicId
	 * @return
	 */
	private ResultInfo<Boolean> validateSingleTopicItem(CartItemInfo cartItemInfo, String ip) {
		TopicItemCartQuery query = new TopicItemCartQuery();
		query.setPlatform(cartItemInfo.getPlatformId());
		query.setArea(cartItemInfo.getAreaId());
		query.setTopicId(cartItemInfo.getTopicId());
		query.setSku(cartItemInfo.getSkuCode());
		query.setAmount(cartItemInfo.getQuantity());
		query.setMemberId(cartItemInfo.getMemberId());
		query.setUip(ip);
		return topicService.validateSingleTopicItem(query);
	}
}
