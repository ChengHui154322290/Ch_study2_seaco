package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettlePushOrderLogDao;
import com.tp.model.fin.SettlePushOrderLog;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettlePushOrderLogService;

@Service
public class SettlePushOrderLogService extends BaseService<SettlePushOrderLog> implements ISettlePushOrderLogService {

	@Autowired
	private SettlePushOrderLogDao settlePushOrderLogDao;
	
	@Override
	public BaseDao<SettlePushOrderLog> getDao() {
		return settlePushOrderLogDao;
	}

}
