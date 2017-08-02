package com.tp.service.ptm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ptm.PlatformSystemLogDao;
import com.tp.model.ptm.PlatformSystemLog;
import com.tp.service.BaseService;
import com.tp.service.ptm.IPlatformSystemLogService;

@Service
public class PlatformSystemLogService extends BaseService<PlatformSystemLog> implements IPlatformSystemLogService {

	@Autowired
	private PlatformSystemLogDao platformSystemLogDao;
	
	@Override
	public BaseDao<PlatformSystemLog> getDao() {
		return platformSystemLogDao;
	}

}
