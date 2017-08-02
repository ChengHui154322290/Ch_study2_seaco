package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CustomsClearanceLogDao;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.service.BaseService;
import com.tp.service.ord.ICustomsClearanceLogService;

@Service
public class CustomsClearanceLogService extends BaseService<CustomsClearanceLog> implements ICustomsClearanceLogService {

	@Autowired
	private CustomsClearanceLogDao customsClearanceLogDao;
	
	@Override
	public BaseDao<CustomsClearanceLog> getDao() {
		return customsClearanceLogDao;
	}

}
