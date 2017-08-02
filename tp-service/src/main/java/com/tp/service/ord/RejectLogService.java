package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.RejectLogDao;
import com.tp.model.ord.RejectLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IRejectLogService;

@Service
public class RejectLogService extends BaseService<RejectLog> implements IRejectLogService {

	@Autowired
	private RejectLogDao rejectLogDao;
	
	@Override
	public BaseDao<RejectLog> getDao() {
		return rejectLogDao;
	}

}
