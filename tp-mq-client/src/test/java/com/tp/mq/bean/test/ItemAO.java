package com.tp.mq.bean.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tp.mq.MqMessageCallBack;

public class ItemAO implements MqMessageCallBack {

	private OrderService orderService;

	private static final Log logger = LogFactory.getLog(ItemAO.class);

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public String getName() {
		return orderService.getName();
	}

	public boolean execute(Object o) {
		logger.debug("================================== 9999999 all topic message===================================" + orderService.getName());
		orderService.getName();
		return true;
	}
	
}
