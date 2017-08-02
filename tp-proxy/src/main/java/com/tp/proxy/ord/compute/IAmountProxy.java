package com.tp.proxy.ord.compute;

import com.tp.common.vo.Constant;
import com.tp.dto.ord.ShoppingCartDto;
/**
 * 计算金额
 * @author szy
 *
 */
public interface IAmountProxy<T extends ShoppingCartDto> {
	public static final Double MIN_AMOUNT=0.1D;
	public static final Double ZERO = Constant.ZERO;
	/**
	 * 计算总金额
	 * @param CartDto
	 * @return
	 */
	T computeAmount(T shoppingCartDto);
}
