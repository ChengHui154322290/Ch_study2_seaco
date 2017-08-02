package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticUpgrade;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticUpgradeService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class StatisticUpgradeProxy extends BaseProxy<StatisticUpgrade>{

	@Autowired
	private IStatisticUpgradeService statisticUpgradeService;

	@Override
	public IBaseService<StatisticUpgrade> getService() {
		return statisticUpgradeService;
	}
}
