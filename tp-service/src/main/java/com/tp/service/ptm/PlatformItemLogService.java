package com.tp.service.ptm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ptm.PlatformItemLogDao;
import com.tp.model.ptm.PlatformItemLog;
import com.tp.service.BaseService;

@Service
public class PlatformItemLogService extends BaseService<PlatformItemLog> implements IPlatformItemLogService {

	@Autowired
	private PlatformItemLogDao itemLogDao;
	
	@Override
	public BaseDao<PlatformItemLog> getDao() {
		return itemLogDao;
	}

}
