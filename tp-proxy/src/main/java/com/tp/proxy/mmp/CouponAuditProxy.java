package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponAudit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponAuditService;
/**
 * 审核日志记录代理层
 * @author szy
 *
 */
@Service
public class CouponAuditProxy extends BaseProxy<CouponAudit>{

	@Autowired
	private ICouponAuditService couponAuditService;

	@Override
	public IBaseService<CouponAudit> getService() {
		return couponAuditService;
	}
}
