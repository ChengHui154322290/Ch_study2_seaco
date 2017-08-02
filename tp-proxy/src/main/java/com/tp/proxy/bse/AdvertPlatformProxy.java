package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.AdvertPlatform;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IAdvertPlatformService;
/**
 * 广告统计代理层
 * @author szy
 *
 */
@Service
public class AdvertPlatformProxy extends BaseProxy<AdvertPlatform>{

	@Autowired
	private IAdvertPlatformService advertPlatformService;

	@Override
	public IBaseService<AdvertPlatform> getService() {
		return advertPlatformService;
	}
}
