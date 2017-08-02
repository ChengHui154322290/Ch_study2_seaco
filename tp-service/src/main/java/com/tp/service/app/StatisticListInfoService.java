package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticListInfoDao;
import com.tp.model.app.StatisticListInfo;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticListInfoService;

@Service
public class StatisticListInfoService extends BaseService<StatisticListInfo> implements IStatisticListInfoService {

	@Autowired
	private StatisticListInfoDao statisticListInfoDao;
	
	@Override
	public BaseDao<StatisticListInfo> getDao() {
		return statisticListInfoDao;
	}

}
