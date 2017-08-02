package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.ExceptionBusinessLogDao;
import com.tp.model.ord.ExceptionBusinessLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IExceptionBusinessLogService;

@Service
public class ExceptionBusinessLogService extends BaseService<ExceptionBusinessLog> implements IExceptionBusinessLogService {

	@Autowired
	private ExceptionBusinessLogDao exceptionBusinessLogDao;
	
	@Override
	public BaseDao<ExceptionBusinessLog> getDao() {
		return exceptionBusinessLogDao;
	}

}
