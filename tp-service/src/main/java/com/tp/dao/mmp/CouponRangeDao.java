package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.mmp.CouponRangeDto;
import com.tp.model.mmp.CouponRange;

public interface CouponRangeDao extends BaseDao<CouponRange> {

    List<CouponRangeDto> selectCouponRangeByCouponId(Long couponId);

    Integer deleteByCouponId(Long couponId);
}
