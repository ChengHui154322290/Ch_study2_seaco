package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.FisherDeliveryLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IFisherDeliveryLogService;
/**
 * 费舍尔发货日志表代理层
 * @author szy
 *
 */
@Service
public class FisherDeliveryLogProxy extends BaseProxy<FisherDeliveryLog>{

	@Autowired
	private IFisherDeliveryLogService fisherDeliveryLogService;

	@Override
	public IBaseService<FisherDeliveryLog> getService() {
		return fisherDeliveryLogService;
	}
}
