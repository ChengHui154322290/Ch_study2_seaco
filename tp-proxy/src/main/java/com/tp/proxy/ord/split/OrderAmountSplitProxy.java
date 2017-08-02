package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;

/**
 * 拆分订单金额到商品项
 * @author szy
 *
 */
@Service
public class OrderAmountSplitProxy implements IOrderAmountSplitProxy{
	/**
	 * 拆分订单金额(分摊)
	 */
	public OrderDto splitAmount(final OrderDto orderDto,final OrderInitDto orderInitDto){
		List<SubOrder> subOrderList = orderDto.getSubOrderList();
		for(SubOrder subOrder:subOrderList){
			List<OrderItem> orderItemList = subOrder.getOrderItemList();
			for(OrderItem orderItem:orderItemList){
				Double subTotal = toPrice(add(multiply(orderItem.getPrice(),orderItem.getQuantity()),orderItem.getTaxFee()));
				orderItem.setOriginalSubTotal(subTotal);
				orderItem.setSubTotal(toPrice(subtract(orderItem.getOriginalSubTotal(),orderItem.getDiscount())));
			}
		}
		return orderDto;
	}
}
