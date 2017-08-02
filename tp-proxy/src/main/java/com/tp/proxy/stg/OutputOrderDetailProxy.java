package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.OutputOrderDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IOutputOrderDetailService;
/**
 * 出库订单明细代理层
 * @author szy
 *
 */
@Service
public class OutputOrderDetailProxy extends BaseProxy<OutputOrderDetail>{

	@Autowired
	private IOutputOrderDetailService outputOrderDetailService;

	@Override
	public IBaseService<OutputOrderDetail> getService() {
		return outputOrderDetailService;
	}
}
