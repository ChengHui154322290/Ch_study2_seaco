package com.tp.proxy.ord.validate;

import com.tp.common.vo.ord.CartConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CartItemInfo;
import com.tp.service.prd.IItemService;

/**
 * 商品项验证过滤器
 * @author szy
 *
 */
public abstract class CartItemValidateFilter implements IOrderValidateProxy<CartItemInfo> {

	@Override
	public FailInfo validate(CartItemInfo cartItemInfo, FailInfo failInfo) {
		if(null!=failInfo){
			return failInfo;
		}
		return validate(cartItemInfo);
	}

	abstract FailInfo validate(CartItemInfo cartItemInfo);
}

/**商品购买最大数量限制验证*/
class ItemQuantityLimitValidate extends CartItemValidateFilter{
	FailInfo validate(CartItemInfo cartItemInfo) {
		if (cartItemInfo.getQuantity() > CartConstant.MAX_SKU_QUANTITY) {
			return new FailInfo(String.format("单个商品购买数量超过最大限制数%d", cartItemInfo.getSkuCode(), CartConstant.MAX_SKU_QUANTITY));
		}
		return null;
	}
}

/**商品有效性验证*/
class ItemEnableValidate extends CartItemValidateFilter{
	private IItemService itemService;
	public ItemEnableValidate(IItemService itemService){
		this.itemService = itemService;
	}
	FailInfo validate(CartItemInfo cartItemInfo) {
		ResultInfo<Boolean> resultInfo = itemService.checkItem(cartItemInfo.getSkuCode());
		if(!resultInfo.success){
			return resultInfo.getMsg();
		}
		return null;
	}
}
