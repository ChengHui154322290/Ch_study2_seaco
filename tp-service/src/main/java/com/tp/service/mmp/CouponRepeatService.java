package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponRepeatDao;
import com.tp.model.mmp.CouponRepeat;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponRepeatService;

@Service
public class CouponRepeatService extends BaseService<CouponRepeat> implements ICouponRepeatService {

	@Autowired
	private CouponRepeatDao couponRepeatDao;
	
	@Override
	public BaseDao<CouponRepeat> getDao() {
		return couponRepeatDao;
	}

}
