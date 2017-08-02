package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticShowDao;
import com.tp.model.app.StatisticShow;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticShowService;

@Service
public class StatisticShowService extends BaseService<StatisticShow> implements IStatisticShowService {

	@Autowired
	private StatisticShowDao statisticShowDao;
	
	@Override
	public BaseDao<StatisticShow> getDao() {
		return statisticShowDao;
	}

}
