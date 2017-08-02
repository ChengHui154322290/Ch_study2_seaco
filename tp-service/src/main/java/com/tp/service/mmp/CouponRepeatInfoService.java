package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponRepeatInfoDao;
import com.tp.model.mmp.CouponRepeatInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponRepeatInfoService;

@Service
public class CouponRepeatInfoService extends BaseService<CouponRepeatInfo> implements ICouponRepeatInfoService {

	@Autowired
	private CouponRepeatInfoDao couponRepeatInfoDao;
	
	@Override
	public BaseDao<CouponRepeatInfo> getDao() {
		return couponRepeatInfoDao;
	}

}
