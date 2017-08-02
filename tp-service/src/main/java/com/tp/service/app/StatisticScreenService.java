package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticScreenDao;
import com.tp.model.app.StatisticScreen;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticScreenService;

@Service
public class StatisticScreenService extends BaseService<StatisticScreen> implements IStatisticScreenService {

	@Autowired
	private StatisticScreenDao statisticScreenDao;
	
	@Override
	public BaseDao<StatisticScreen> getDao() {
		return statisticScreenDao;
	}

}
