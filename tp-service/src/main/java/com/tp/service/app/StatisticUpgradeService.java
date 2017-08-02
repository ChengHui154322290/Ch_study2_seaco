package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticUpgradeDao;
import com.tp.model.app.StatisticUpgrade;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticUpgradeService;

@Service
public class StatisticUpgradeService extends BaseService<StatisticUpgrade> implements IStatisticUpgradeService {

	@Autowired
	private StatisticUpgradeDao statisticUpgradeDao;
	
	@Override
	public BaseDao<StatisticUpgrade> getDao() {
		return statisticUpgradeDao;
	}

}
