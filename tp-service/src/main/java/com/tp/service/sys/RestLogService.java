package com.tp.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sys.RestLogDao;
import com.tp.model.sys.RestLog;
import com.tp.service.BaseService;
import com.tp.service.sys.IRestLogService;

@Service
public class RestLogService extends BaseService<RestLog> implements IRestLogService {

	@Autowired
	private RestLogDao restLogDao;
	
	@Override
	public BaseDao<RestLog> getDao() {
		return restLogDao;
	}

	
}
