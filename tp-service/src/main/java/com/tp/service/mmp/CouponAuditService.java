package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponAuditDao;
import com.tp.model.mmp.CouponAudit;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponAuditService;

@Service
public class CouponAuditService extends BaseService<CouponAudit> implements ICouponAuditService {

	@Autowired
	private CouponAuditDao couponAuditDao;
	
	@Override
	public BaseDao<CouponAudit> getDao() {
		return couponAuditDao;
	}

}
