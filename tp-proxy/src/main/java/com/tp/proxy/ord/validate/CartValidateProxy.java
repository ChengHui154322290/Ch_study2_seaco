package com.tp.proxy.ord.validate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.CartConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.enums.common.OrderErrorCodeEnum;
import com.tp.model.ord.CartItemInfo;
import com.tp.proxy.ord.split.OrderSplitProxy;
import com.tp.service.ord.ICartItemService;

@Service
public class CartValidateProxy implements IOrderValidateProxy<ShoppingCartDto> {

	@Autowired
	private ICartItemService cartItemInfoService;
	@Autowired
	private CartItemInfoValidateProxy  cartItemInfoValidateProxy;
	
	@Override
	public FailInfo validate(ShoppingCartDto shoppingCartDto, FailInfo failInfo){
		if(failInfo!=null){
			return failInfo;
		}
		if(shoppingCartDto==null || CollectionUtils.isEmpty(shoppingCartDto.getCartItemInfoList())){
			return null;
		}
		Integer itemTotal = shoppingCartDto.getCartItemInfoList().size();
		/* 购物车不能超过99行 */
		if (itemTotal > CartConstant.MAX_LINE_QUANTITY) {
			return new FailInfo(OrderErrorCodeEnum.CART_ERROR.MAX_LINE_QUANTITY_ERROR);
		}
		Map<String,String> failInfoMap = new HashMap<String,String>();
		for(CartItemInfo cartItemInfo:shoppingCartDto.getCartItemInfoList()){
			FailInfo itemFailInfo = cartItemInfoValidateProxy.validate(shoppingCartDto, failInfo);
			if(null!=itemFailInfo){
				cartItemInfo.setFailInfo(itemFailInfo);
				failInfoMap.put(cartItemInfo.getSkuCode()+Constant.SPLIT_SIGN.UNDERLINE+cartItemInfo.getTopicId(), itemFailInfo.getDetailMessage());
			}
		}
		if(MapUtils.isNotEmpty(failInfoMap)){
			return new FailInfo(failInfoMap);
		}
		return null;
	}
	
	public FailInfo validate(CartItemInfo cartItemInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", cartItemInfo.getMemberId());
		Integer itemCount = cartItemInfoService.queryByParamCount(params);
		/* 购物车不能超过99行 */
		if (itemCount > CartConstant.MAX_LINE_QUANTITY) {
			return new FailInfo(OrderErrorCodeEnum.CART_ERROR.MAX_LINE_QUANTITY_ERROR);
		}
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		shoppingCartDto.setMemberId(cartItemInfo.getMemberId());
		shoppingCartDto.setIp(null);
		shoppingCartDto.getCartItemInfoList().add(cartItemInfo);
		return cartItemInfoValidateProxy.validate(shoppingCartDto, null);
	}
	
	public FailInfo validatePassLimit(ShoppingCartDto shoppingCartDto, FailInfo failInfo){
		if(null!=failInfo){
			return failInfo;
		}
		for(PreOrderDto subOrder:shoppingCartDto.getPreSubOrderList()){
			if(OrderUtils.isSeaOrder(subOrder.getType()) 
			   && !OrderConstant.OrderType.DOMESTIC.getCode().equals(subOrder.getStorageType())
			   && !ClearanceChannelsEnum.HWZY.id.equals(subOrder.getSeaChannel())
			   && subOrder.getItemTotal()>=OrderSplitProxy.CUSTOMS_RATE_LIMIT){
				subOrder.setWarnMessage("你有一单已超过海关规定限额"+OrderSplitProxy.CUSTOMS_RATE_LIMIT);
				return new FailInfo("你有一单已超过海关规定限额"+OrderSplitProxy.CUSTOMS_RATE_LIMIT);
			}
		};
		return null;
	}
}
