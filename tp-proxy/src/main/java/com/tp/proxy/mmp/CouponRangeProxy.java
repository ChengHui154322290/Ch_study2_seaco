package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponRange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponRangeService;
/**
 * 优惠券适用范围表代理层
 * @author szy
 *
 */
@Service
public class CouponRangeProxy extends BaseProxy<CouponRange>{

	@Autowired
	private ICouponRangeService couponRangeService;

	@Override
	public IBaseService<CouponRange> getService() {
		return couponRangeService;
	}
}
