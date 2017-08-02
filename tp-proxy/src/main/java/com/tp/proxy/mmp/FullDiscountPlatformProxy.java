package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.FullDiscountPlatform;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IFullDiscountPlatformService;
/**
 * 优惠政策适用平台信息代理层
 * @author szy
 *
 */
@Service
public class FullDiscountPlatformProxy extends BaseProxy<FullDiscountPlatform>{

	@Autowired
	private IFullDiscountPlatformService fullDiscountPlatformService;

	@Override
	public IBaseService<FullDiscountPlatform> getService() {
		return fullDiscountPlatformService;
	}
}
