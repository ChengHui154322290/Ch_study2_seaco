package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleInfoLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleInfoLogService;
/**
 * 结算日志表代理层
 * @author szy
 *
 */
@Service
public class SettleInfoLogProxy extends BaseProxy<SettleInfoLog>{

	@Autowired
	private ISettleInfoLogService settleInfoLogService;

	@Override
	public IBaseService<SettleInfoLog> getService() {
		return settleInfoLogService;
	}
}
