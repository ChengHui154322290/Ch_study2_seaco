package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticCrashDao;
import com.tp.model.app.StatisticCrash;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticCrashService;

@Service
public class StatisticCrashService extends BaseService<StatisticCrash> implements IStatisticCrashService {

	@Autowired
	private StatisticCrashDao statisticCrashDao;
	
	@Override
	public BaseDao<StatisticCrash> getDao() {
		return statisticCrashDao;
	}

}
