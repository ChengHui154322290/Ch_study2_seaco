package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleInfoLogDao;
import com.tp.model.fin.SettleInfoLog;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleInfoLogService;

@Service
public class SettleInfoLogService extends BaseService<SettleInfoLog> implements ISettleInfoLogService {

	@Autowired
	private SettleInfoLogDao settleInfoLogDao;
	
	@Override
	public BaseDao<SettleInfoLog> getDao() {
		return settleInfoLogDao;
	}

}
