package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticNewversionDao;
import com.tp.model.app.StatisticNewversion;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticNewversionService;

@Service
public class StatisticNewversionService extends BaseService<StatisticNewversion> implements IStatisticNewversionService {

	@Autowired
	private StatisticNewversionDao statisticNewversionDao;
	
	@Override
	public BaseDao<StatisticNewversion> getDao() {
		return statisticNewversionDao;
	}

}
