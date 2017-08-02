package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticPublicDao;
import com.tp.model.app.StatisticPublic;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticPublicService;

@Service
public class StatisticPublicService extends BaseService<StatisticPublic> implements IStatisticPublicService {

	@Autowired
	private StatisticPublicDao statisticPublicDao;
	
	@Override
	public BaseDao<StatisticPublic> getDao() {
		return statisticPublicDao;
	}

}
