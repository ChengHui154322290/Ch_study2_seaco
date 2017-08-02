package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InputOrderDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInputOrderDetailService;
/**
 * 入库订单明细
代理层
 * @author szy
 *
 */
@Service
public class InputOrderDetailProxy extends BaseProxy<InputOrderDetail>{

	@Autowired
	private IInputOrderDetailService inputOrderDetailService;

	@Override
	public IBaseService<InputOrderDetail> getService() {
		return inputOrderDetailService;
	}
}
