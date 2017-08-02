package com.tp.service.app;

import com.tp.model.app.VersionInfo;
import com.tp.service.IBaseService;
/**
  * @author zhuss
  * 接口
  */
public interface IVersionInfoService extends IBaseService<VersionInfo>{

	/**
	 * 根据平台修改所有版本为非最新
	 * @param platform
	 * @return
	 */
	public Integer updateIsNewByPlatform(Integer platform);
}
