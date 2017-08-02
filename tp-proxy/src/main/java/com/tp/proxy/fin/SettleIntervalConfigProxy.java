package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleIntervalConfig;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleIntervalConfigService;
/**
 * 风险管理结算间隔天数配置表代理层
 * @author szy
 *
 */
@Service
public class SettleIntervalConfigProxy extends BaseProxy<SettleIntervalConfig>{

	@Autowired
	private ISettleIntervalConfigService settleIntervalConfigService;

	@Override
	public IBaseService<SettleIntervalConfig> getService() {
		return settleIntervalConfigService;
	}
}
