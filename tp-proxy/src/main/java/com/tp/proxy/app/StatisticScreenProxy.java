package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticScreen;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticScreenService;
/**
 * 横竖屏代理层
 * @author szy
 *
 */
@Service
public class StatisticScreenProxy extends BaseProxy<StatisticScreen>{

	@Autowired
	private IStatisticScreenService statisticScreenService;

	@Override
	public IBaseService<StatisticScreen> getService() {
		return statisticScreenService;
	}
}
