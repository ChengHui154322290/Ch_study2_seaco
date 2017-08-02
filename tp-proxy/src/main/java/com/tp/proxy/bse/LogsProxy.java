package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.Logs;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ILogsService;
/**
 * base数据库操作日志表代理层
 * @author szy
 *
 */
@Service
public class LogsProxy extends BaseProxy<Logs>{

	@Autowired
	private ILogsService logsService;

	@Override
	public IBaseService<Logs> getService() {
		return logsService;
	}
}
