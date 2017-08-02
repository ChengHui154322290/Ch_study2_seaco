package com.tp.dao.app;

import com.tp.common.dao.BaseDao;
import com.tp.model.app.VersionInfo;

public interface VersionInfoDao extends BaseDao<VersionInfo> {

	/**
	 * 根据平台修改所有版本为非最新
	 * @param platform
	 * @return
	 */
	public Integer updateIsNewByPlatform(Integer platform);
}
