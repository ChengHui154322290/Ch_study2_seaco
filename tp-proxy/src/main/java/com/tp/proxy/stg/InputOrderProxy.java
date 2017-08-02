package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InputOrder;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInputOrderService;
/**
 * 入库订单代理层
 * @author szy
 *
 */
@Service
public class InputOrderProxy extends BaseProxy<InputOrder>{

	@Autowired
	private IInputOrderService inputOrderService;

	@Override
	public IBaseService<InputOrder> getService() {
		return inputOrderService;
	}
}
