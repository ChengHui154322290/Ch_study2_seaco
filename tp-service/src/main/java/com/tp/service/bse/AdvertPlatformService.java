package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.AdvertPlatformDao;
import com.tp.model.bse.AdvertPlatform;
import com.tp.service.BaseService;
import com.tp.service.bse.IAdvertPlatformService;

@Service
public class AdvertPlatformService extends BaseService<AdvertPlatform> implements IAdvertPlatformService {

	@Autowired
	private AdvertPlatformDao advertPlatformDao;
	
	@Override
	public BaseDao<AdvertPlatform> getDao() {
		return advertPlatformDao;
	}

}
