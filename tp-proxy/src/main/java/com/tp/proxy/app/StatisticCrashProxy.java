package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticCrash;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticCrashService;
/**
 * 记录程序崩溃的信息。代理层
 * @author szy
 *
 */
@Service
public class StatisticCrashProxy extends BaseProxy<StatisticCrash>{

	@Autowired
	private IStatisticCrashService statisticCrashService;

	@Override
	public IBaseService<StatisticCrash> getService() {
		return statisticCrashService;
	}
}
