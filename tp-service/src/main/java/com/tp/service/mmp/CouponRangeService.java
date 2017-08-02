package com.tp.service.mmp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.CouponRangeDao;
import com.tp.dto.mmp.CouponRangeDto;
import com.tp.model.mmp.CouponRange;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponRangeService;

@Service
public class CouponRangeService extends BaseService<CouponRange> implements ICouponRangeService {

	@Autowired
	private CouponRangeDao couponRangeDao;
	
	@Override
	public BaseDao<CouponRange> getDao() {
		return couponRangeDao;
	}

	@Override
	public List<CouponRangeDto> selectCouponRangeByCouponId(Long couponId) {

		return couponRangeDao.selectCouponRangeByCouponId(couponId);

	}

	@Override
	public Integer deleteByCouponId(Long couponId) {
		return couponRangeDao.deleteByCouponId(couponId);
	}
}
