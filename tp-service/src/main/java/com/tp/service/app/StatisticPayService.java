package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticPayDao;
import com.tp.model.app.StatisticPay;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticPayService;

@Service
public class StatisticPayService extends BaseService<StatisticPay> implements IStatisticPayService {

	@Autowired
	private StatisticPayDao statisticPayDao;
	
	@Override
	public BaseDao<StatisticPay> getDao() {
		return statisticPayDao;
	}

}
