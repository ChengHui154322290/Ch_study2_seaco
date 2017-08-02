package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.DynamicConfigurationDao;
import com.tp.model.app.DynamicConfiguration;
import com.tp.service.BaseService;
import com.tp.service.app.IDynamicConfigurationService;

@Service
public class DynamicConfigurationService extends BaseService<DynamicConfiguration> implements IDynamicConfigurationService {

	@Autowired
	private DynamicConfigurationDao dynamicConfigurationDao;
	
	@Override
	public BaseDao<DynamicConfiguration> getDao() {
		return dynamicConfigurationDao;
	}

	public DynamicConfiguration queryByVersion(DynamicConfiguration query){
		return  dynamicConfigurationDao.queryByVersion(query);
	}

}
