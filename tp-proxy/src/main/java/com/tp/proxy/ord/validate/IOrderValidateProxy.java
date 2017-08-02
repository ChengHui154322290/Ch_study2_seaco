package com.tp.proxy.ord.validate;

import com.tp.dto.common.FailInfo;

/**
 * 订单，购物车验证,责任链,所有关于下订单的验证都通过这个接口，方便管理
 * @author szy
 *
 */
public interface IOrderValidateProxy<T> {
	FailInfo validate(T shoppingCartDto,FailInfo failInfo);
}
