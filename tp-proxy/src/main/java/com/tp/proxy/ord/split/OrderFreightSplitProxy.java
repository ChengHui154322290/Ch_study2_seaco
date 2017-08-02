package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;

/**
 * 拆分运费到子订单及商品项
 * @author szy
 *
 */
@Service
public class OrderFreightSplitProxy implements IOrderAmountSplitProxy{

	@Override
	public OrderDto splitAmount(OrderDto orderDto, OrderInitDto orderInitDto) {
		List<SubOrder> subOrderList = orderDto.getSubOrderList();
		for(SubOrder subOrder:subOrderList){
			if(subOrder.getFreight()>0){
				List<OrderItem> orderItemList = subOrder.getOrderItemList();
				Double subTotal = Constant.ZERO;
				OrderItem maxOrderItem = orderItemList.get(0);
				for(OrderItem orderItem:orderItemList){
					Double freight = toPrice(multiply(divide(maxOrderItem.getSubTotal(),subOrder.getTotal()),subOrder.getFreight()));
					orderItem.setFreight(freight);
					subTotal = add(subTotal,freight).doubleValue();
				}
				maxOrderItem.setFreight(toPrice(add(maxOrderItem.getFreight(),subtract(subOrder.getFreight(),subTotal))));
			}
		}
		return orderDto;
	}
}
