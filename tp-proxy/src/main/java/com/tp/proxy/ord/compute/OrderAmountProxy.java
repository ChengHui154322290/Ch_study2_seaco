package com.tp.proxy.ord.compute;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.proxy.ord.split.OrderCouponSplitProxy;
import com.tp.proxy.ord.split.OrderFirstSplitProxy;

@Service
public class OrderAmountProxy implements IAmountProxy<OrderInitDto> {
	
	@Autowired
	private CartAmountProxy cartAmountProxy;
	@Autowired
	private FreightAmountProxy freightAmountProxy;
	@Autowired
	private OrderFirstSplitProxy orderFirstSplitProxy;
	@Autowired
	private OrderCouponSplitProxy orderCouponSplitProxy;
	@Autowired
	private PointAmountProxy pointAmountProxy;
	
	public OrderInitDto computeAmount(OrderInitDto orderInitDto) {
		orderInitDto = (OrderInitDto)cartAmountProxy.computeAmount(orderInitDto);//商品基本价,税金
		orderInitDto = freightAmountProxy.computeAmount(orderInitDto);//运费
		orderInitDto = initOrderItemPromotion(orderInitDto);
		orderInitDto = orderFirstSplitProxy.splitFirstMinus(orderInitDto);//首单立减
		orderInitDto = orderCouponSplitProxy.splitCouponList(orderInitDto);//计算优惠、拆分到订单商品项
		orderInitDto = (OrderInitDto)cartAmountProxy.computeTotalAmount(orderInitDto);
		//如果订单金额小于0.1元，则订单金额等于0.1元
		//orderInitDto = (OrderInitDto)cartAmountProxy.addAmountIfZero(orderInitDto);
		orderInitDto = pointAmountProxy.computeAmount(orderInitDto);
		orderInitDto = (OrderInitDto)cartAmountProxy.computeTotalAmount(orderInitDto);
		return orderInitDto;
	}

	public OrderInitDto initOrderItemPromotion(OrderInitDto orderInitDto){
		orderInitDto.getOrderItemList().forEach(new Consumer<OrderItem>(){
			public void accept(OrderItem t) {
				t.setOrderPromotionList(new ArrayList<OrderPromotion>());
			}
		});
		orderInitDto.getPreSubOrderList().forEach(new Consumer<PreOrderDto>(){
			public void accept(PreOrderDto subOrder) {
				subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
					public void accept(OrderItem t) {
						t.setOrderPromotionList(new ArrayList<OrderPromotion>());
					}
				});
			}
		});
		return orderInitDto;
	}
	
	
}
