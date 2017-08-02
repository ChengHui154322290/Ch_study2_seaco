package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.VersionInfoDao;
import com.tp.model.app.VersionInfo;
import com.tp.service.BaseService;
import com.tp.service.app.IVersionInfoService;

@Service
public class VersionInfoService extends BaseService<VersionInfo> implements IVersionInfoService {

	@Autowired
	private VersionInfoDao versionDao;
	
	@Override
	public BaseDao<VersionInfo> getDao() {
		return versionDao;
	}

	@Override
	public Integer updateIsNewByPlatform(Integer platform) {
		return versionDao.updateIsNewByPlatform(platform);
	}

}
