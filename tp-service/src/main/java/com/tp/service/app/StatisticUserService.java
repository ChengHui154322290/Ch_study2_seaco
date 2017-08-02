package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticUserDao;
import com.tp.model.app.StatisticUser;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticUserService;

@Service
public class StatisticUserService extends BaseService<StatisticUser> implements IStatisticUserService {

	@Autowired
	private StatisticUserDao statisticUserDao;
	
	@Override
	public BaseDao<StatisticUser> getDao() {
		return statisticUserDao;
	}

}
