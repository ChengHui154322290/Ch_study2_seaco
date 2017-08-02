package com.tp.proxy.ord.assemble;

import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;

/**
 * 组装订单
 * @author szy
 *
 */
public interface IOrderAssembleProxy {
	
	/**
	 * 组装订单信息
	 * @author szy
	 * @param CartDto 取选中的商品
	 */
	OrderDto assembleOrder(OrderInitDto orderInitDto);
}
