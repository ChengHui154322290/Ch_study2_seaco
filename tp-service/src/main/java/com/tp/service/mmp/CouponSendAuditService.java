package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponSendAuditDao;
import com.tp.model.mmp.CouponSendAudit;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponSendAuditService;

@Service
public class CouponSendAuditService extends BaseService<CouponSendAudit> implements ICouponSendAuditService {

	@Autowired
	private CouponSendAuditDao couponSendAuditDao;
	
	@Override
	public BaseDao<CouponSendAudit> getDao() {
		return couponSendAuditDao;
	}

}
