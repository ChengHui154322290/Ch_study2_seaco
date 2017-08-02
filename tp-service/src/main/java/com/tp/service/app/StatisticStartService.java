package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticStartDao;
import com.tp.model.app.StatisticStart;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticStartService;

@Service
public class StatisticStartService extends BaseService<StatisticStart> implements IStatisticStartService {

	@Autowired
	private StatisticStartDao statisticStartDao;
	
	@Override
	public BaseDao<StatisticStart> getDao() {
		return statisticStartDao;
	}

}
