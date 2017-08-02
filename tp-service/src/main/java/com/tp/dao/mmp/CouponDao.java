package com.tp.dao.mmp;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.mmp.CouponDto;
import com.tp.dto.mmp.CouponUserInfoDTO;
import com.tp.model.mmp.Coupon;

public interface CouponDao extends BaseDao<Coupon> {

    CouponDto getCouponInfosById(Long id);

    List<CouponUserInfoDTO> queryCouponForBackend(Map<String, Object> param);

    Integer countCouponForBackend(Map<String, Object> param);

    Long queryByObjectWithLikeCount(Coupon coupon);

    List<Coupon> queryByObjectWithLike(Coupon coupon);

}
