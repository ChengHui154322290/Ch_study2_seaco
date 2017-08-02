package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.PointPackageDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointPackageDetailService;
/**
 * 积分包出入详情表代理层
 * @author szy
 *
 */
@Service
public class PointPackageDetailProxy extends BaseProxy<PointPackageDetail>{

	@Autowired
	private IPointPackageDetailService pointPackageDetailService;

	@Override
	public IBaseService<PointPackageDetail> getService() {
		return pointPackageDetailService;
	}
}
