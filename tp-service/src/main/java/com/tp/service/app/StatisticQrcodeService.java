package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticQrcodeDao;
import com.tp.model.app.StatisticQrcode;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticQrcodeService;

@Service
public class StatisticQrcodeService extends BaseService<StatisticQrcode> implements IStatisticQrcodeService {

	@Autowired
	private StatisticQrcodeDao statisticQrcodeDao;
	
	@Override
	public BaseDao<StatisticQrcode> getDao() {
		return statisticQrcodeDao;
	}

}
