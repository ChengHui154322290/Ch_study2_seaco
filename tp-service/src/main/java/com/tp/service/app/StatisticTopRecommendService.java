package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticTopRecommendDao;
import com.tp.model.app.StatisticTopRecommend;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticTopRecommendService;

@Service
public class StatisticTopRecommendService extends BaseService<StatisticTopRecommend> implements IStatisticTopRecommendService {

	@Autowired
	private StatisticTopRecommendDao statisticTopRecommendDao;
	
	@Override
	public BaseDao<StatisticTopRecommend> getDao() {
		return statisticTopRecommendDao;
	}

}
