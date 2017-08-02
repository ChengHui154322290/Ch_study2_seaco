package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponRepeat;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponRepeatService;
/**
 * 补券规则信息表代理层
 * @author szy
 *
 */
@Service
public class CouponRepeatProxy extends BaseProxy<CouponRepeat>{

	@Autowired
	private ICouponRepeatService couponRepeatService;

	@Override
	public IBaseService<CouponRepeat> getService() {
		return couponRepeatService;
	}
}
