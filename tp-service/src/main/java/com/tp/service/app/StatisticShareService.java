package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticShareDao;
import com.tp.model.app.StatisticShare;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticShareService;

@Service
public class StatisticShareService extends BaseService<StatisticShare> implements IStatisticShareService {

	@Autowired
	private StatisticShareDao statisticShareDao;
	
	@Override
	public BaseDao<StatisticShare> getDao() {
		return statisticShareDao;
	}

}
