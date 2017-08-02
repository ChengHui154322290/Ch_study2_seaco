package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.AdvertLogDao;
import com.tp.model.bse.AdvertLog;
import com.tp.service.BaseService;
import com.tp.service.bse.IAdvertLogService;

@Service
public class AdvertLogService extends BaseService<AdvertLog> implements IAdvertLogService {

	@Autowired
	private AdvertLogDao advertLogDao;
	
	@Override
	public BaseDao<AdvertLog> getDao() {
		return advertLogDao;
	}

}
