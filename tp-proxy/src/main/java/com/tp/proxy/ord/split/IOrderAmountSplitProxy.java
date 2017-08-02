package com.tp.proxy.ord.split;

import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;

/**
 * 拆分金额
 * @author szy
 *
 */
public interface IOrderAmountSplitProxy {
	public static final Integer DIVIDE_SCALE = 6;
	public OrderDto splitAmount(final OrderDto orderDto,final OrderInitDto orderInitDto);
}
