package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.ModifyMobileLogDao;
import com.tp.model.mem.ModifyMobileLog;
import com.tp.service.BaseService;
import com.tp.service.mem.IModifyMobileLogService;

@Service
public class ModifyMobileLogService extends BaseService<ModifyMobileLog> implements IModifyMobileLogService {

	@Autowired
	private ModifyMobileLogDao modifyMobileLogDao;
	
	@Override
	public BaseDao<ModifyMobileLog> getDao() {
		return modifyMobileLogDao;
	}

}
