package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticStart;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticStartService;
/**
 * 各终端APP每次启动时投递一次，投递数据用于统计装机量数据，启动相关数据等代理层
 * @author szy
 *
 */
@Service
public class StatisticStartProxy extends BaseProxy<StatisticStart>{

	@Autowired
	private IStatisticStartService statisticStartService;

	@Override
	public IBaseService<StatisticStart> getService() {
		return statisticStartService;
	}
}
