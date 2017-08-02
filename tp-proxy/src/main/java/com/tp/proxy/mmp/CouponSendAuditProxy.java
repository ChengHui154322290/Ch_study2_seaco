package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.CouponSendAudit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponSendAuditService;
/**
 * 审核日志记录代理层
 * @author szy
 *
 */
@Service
public class CouponSendAuditProxy extends BaseProxy<CouponSendAudit>{

	@Autowired
	private ICouponSendAuditService couponSendAuditService;

	@Override
	public IBaseService<CouponSendAudit> getService() {
		return couponSendAuditService;
	}
}
