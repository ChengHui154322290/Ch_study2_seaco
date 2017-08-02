package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderPoint;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderPointService;
/**
 * 订单积分使用表代理层
 * @author szy
 *
 */
@Service
public class OrderPointProxy extends BaseProxy<OrderPoint>{

	@Autowired
	private IOrderPointService orderPointService;

	@Override
	public IBaseService<OrderPoint> getService() {
		return orderPointService;
	}
}
