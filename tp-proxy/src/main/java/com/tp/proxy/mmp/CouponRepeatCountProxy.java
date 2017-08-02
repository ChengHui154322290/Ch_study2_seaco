package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponRepeatCount;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponRepeatCountService;
/**
 * 优惠券补发统计表代理层
 * @author szy
 *
 */
@Service
public class CouponRepeatCountProxy extends BaseProxy<CouponRepeatCount>{

	@Autowired
	private ICouponRepeatCountService couponRepeatCountService;

	@Override
	public IBaseService<CouponRepeatCount> getService() {
		return couponRepeatCountService;
	}
}
