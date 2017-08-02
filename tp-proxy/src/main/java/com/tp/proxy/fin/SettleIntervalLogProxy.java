package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleIntervalLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleIntervalLogService;
/**
 * 风险管理结算间隔天数配置日志表代理层
 * @author szy
 *
 */
@Service
public class SettleIntervalLogProxy extends BaseProxy<SettleIntervalLog>{

	@Autowired
	private ISettleIntervalLogService settleIntervalLogService;

	@Override
	public IBaseService<SettleIntervalLog> getService() {
		return settleIntervalLogService;
	}
}
