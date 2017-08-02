package com.tp.proxy.ord.split;

import com.tp.dto.ord.ShoppingCartDto;

public interface IOrderSplitProxy<T extends ShoppingCartDto> {

	/**
	 * 拆分订单
	 * @param orderInitDto
	 * @return
	 */
	public T split(T shoppingCartDto);
}
