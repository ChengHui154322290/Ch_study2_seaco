package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticNewversion;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticNewversionService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class StatisticNewversionProxy extends BaseProxy<StatisticNewversion>{

	@Autowired
	private IStatisticNewversionService statisticNewversionService;

	@Override
	public IBaseService<StatisticNewversion> getService() {
		return statisticNewversionService;
	}
}
