package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponRepeatInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponRepeatInfoService;
/**
 * 优惠券补发规则表代理层
 * @author szy
 *
 */
@Service
public class CouponRepeatInfoProxy extends BaseProxy<CouponRepeatInfo>{

	@Autowired
	private ICouponRepeatInfoService couponRepeatInfoService;

	@Override
	public IBaseService<CouponRepeatInfo> getService() {
		return couponRepeatInfoService;
	}
}
