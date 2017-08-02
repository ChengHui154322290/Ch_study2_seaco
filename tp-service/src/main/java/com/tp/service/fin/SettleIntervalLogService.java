package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleIntervalLogDao;
import com.tp.model.fin.SettleIntervalLog;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleIntervalLogService;

@Service
public class SettleIntervalLogService extends BaseService<SettleIntervalLog> implements ISettleIntervalLogService {

	@Autowired
	private SettleIntervalLogDao settleIntervalLogDao;
	
	@Override
	public BaseDao<SettleIntervalLog> getDao() {
		return settleIntervalLogDao;
	}

}
