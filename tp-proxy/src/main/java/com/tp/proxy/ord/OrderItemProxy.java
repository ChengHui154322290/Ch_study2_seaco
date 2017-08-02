package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderItem;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderItemService;
/**
 * 订单行表代理层
 * @author szy
 *
 */
@Service
public class OrderItemProxy extends BaseProxy<OrderItem>{

	@Autowired
	private IOrderItemService orderItemService;

	@Override
	public IBaseService<OrderItem> getService() {
		return orderItemService;
	}
}
