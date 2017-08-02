package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderDelivery;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderDeliveryService;
/**
 * 订单物流状态表代理层
 * @author szy
 *
 */
@Service
public class OrderDeliveryProxy extends BaseProxy<OrderDelivery>{

	@Autowired
	private IOrderDeliveryService orderDeliveryService;

	@Override
	public IBaseService<OrderDelivery> getService() {
		return orderDeliveryService;
	}
}
