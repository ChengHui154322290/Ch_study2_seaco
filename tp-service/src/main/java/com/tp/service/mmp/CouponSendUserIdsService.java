package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponSendUserIdsDao;
import com.tp.model.mmp.CouponSendUserIds;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponSendUserIdsService;

@Service
public class CouponSendUserIdsService extends BaseService<CouponSendUserIds> implements ICouponSendUserIdsService {

	@Autowired
	private CouponSendUserIdsDao couponSendUserIdsDao;
	
	@Override
	public BaseDao<CouponSendUserIds> getDao() {
		return couponSendUserIdsDao;
	}

}
