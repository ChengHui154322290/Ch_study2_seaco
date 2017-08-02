package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponSend;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponSendService;
/**
 * 优惠券发放信息表代理层
 * @author szy
 *
 */
@Service
public class CouponSendProxy extends BaseProxy<CouponSend>{

	@Autowired
	private ICouponSendService couponSendService;

	@Override
	public IBaseService<CouponSend> getService() {
		return couponSendService;
	}
}
