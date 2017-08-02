package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderConsignee;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderConsigneeService;
/**
 * 订单收货人表代理层
 * @author szy
 *
 */
@Service
public class OrderConsigneeProxy extends BaseProxy<OrderConsignee>{

	@Autowired
	private IOrderConsigneeService orderConsigneeService;

	@Override
	public IBaseService<OrderConsignee> getService() {
		return orderConsigneeService;
	}
}
