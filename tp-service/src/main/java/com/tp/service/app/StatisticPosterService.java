package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticPosterDao;
import com.tp.model.app.StatisticPoster;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticPosterService;

@Service
public class StatisticPosterService extends BaseService<StatisticPoster> implements IStatisticPosterService {

	@Autowired
	private StatisticPosterDao statisticPosterDao;
	
	@Override
	public BaseDao<StatisticPoster> getDao() {
		return statisticPosterDao;
	}

}
