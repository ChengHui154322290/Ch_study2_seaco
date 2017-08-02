package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CancelLogDao;
import com.tp.model.ord.CancelLog;
import com.tp.service.BaseService;
import com.tp.service.ord.ICancelLogService;

@Service
public class CancelLogService extends BaseService<CancelLog> implements ICancelLogService {

	@Autowired
	private CancelLogDao cancelLogDao;
	
	@Override
	public BaseDao<CancelLog> getDao() {
		return cancelLogDao;
	}

}
