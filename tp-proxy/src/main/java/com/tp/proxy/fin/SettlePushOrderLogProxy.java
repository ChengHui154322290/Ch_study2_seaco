package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettlePushOrderLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettlePushOrderLogService;
/**
 * 订单商品项结算信息推送日志代理层
 * @author szy
 *
 */
@Service
public class SettlePushOrderLogProxy extends BaseProxy<SettlePushOrderLog>{

	@Autowired
	private ISettlePushOrderLogService settlePushOrderLogService;

	@Override
	public IBaseService<SettlePushOrderLog> getService() {
		return settlePushOrderLogService;
	}
}
