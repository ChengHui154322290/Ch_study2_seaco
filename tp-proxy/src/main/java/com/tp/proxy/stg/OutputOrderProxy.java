package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.OutputOrder;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IOutputOrderService;
/**
 * 下单完成后向仓库发送发货出库订单代理层
 * @author szy
 *
 */
@Service
public class OutputOrderProxy extends BaseProxy<OutputOrder>{

	@Autowired
	private IOutputOrderService outputOrderService;

	@Override
	public IBaseService<OutputOrder> getService() {
		return outputOrderService;
	}
}
