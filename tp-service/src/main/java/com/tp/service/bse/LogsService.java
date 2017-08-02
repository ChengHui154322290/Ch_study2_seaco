package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.LogsDao;
import com.tp.model.bse.Logs;
import com.tp.service.BaseService;
import com.tp.service.bse.ILogsService;

@Service
public class LogsService extends BaseService<Logs> implements ILogsService {

	@Autowired
	private LogsDao logsDao;
	
	@Override
	public BaseDao<Logs> getDao() {
		return logsDao;
	}

}
