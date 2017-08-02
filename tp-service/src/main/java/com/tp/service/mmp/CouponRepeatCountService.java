package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponRepeatCountDao;
import com.tp.model.mmp.CouponRepeatCount;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponRepeatCountService;

@Service
public class CouponRepeatCountService extends BaseService<CouponRepeatCount> implements ICouponRepeatCountService {

	@Autowired
	private CouponRepeatCountDao couponRepeatCountDao;
	
	@Override
	public BaseDao<CouponRepeatCount> getDao() {
		return couponRepeatCountDao;
	}

}
