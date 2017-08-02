package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.PointPackage;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointPackageService;
/**
 * 会员积分打包记录代理层
 * @author szy
 *
 */
@Service
public class PointPackageProxy extends BaseProxy<PointPackage>{

	@Autowired
	private IPointPackageService pointPackageService;

	@Override
	public IBaseService<PointPackage> getService() {
		return pointPackageService;
	}
}
