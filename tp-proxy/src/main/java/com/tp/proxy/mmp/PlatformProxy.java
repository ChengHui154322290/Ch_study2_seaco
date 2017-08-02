package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.Platform;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPlatformService;
/**
 * 促销适用平台代理层
 * @author szy
 *
 */
@Service
public class PlatformProxy extends BaseProxy<Platform>{

	@Autowired
	private IPlatformService platformService;

	@Override
	public IBaseService<Platform> getService() {
		return platformService;
	}
}
