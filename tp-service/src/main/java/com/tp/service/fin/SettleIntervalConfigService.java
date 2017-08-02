package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleIntervalConfigDao;
import com.tp.model.fin.SettleIntervalConfig;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleIntervalConfigService;

@Service
public class SettleIntervalConfigService extends BaseService<SettleIntervalConfig> implements ISettleIntervalConfigService {

	@Autowired
	private SettleIntervalConfigDao settleIntervalConfigDao;
	
	@Override
	public BaseDao<SettleIntervalConfig> getDao() {
		return settleIntervalConfigDao;
	}

}
