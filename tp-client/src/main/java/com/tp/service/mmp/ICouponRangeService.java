package com.tp.service.mmp;

import com.tp.dto.mmp.CouponRangeDto;
import com.tp.model.mmp.CouponRange;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy
  * 优惠券适用范围表接口
  */
public interface ICouponRangeService extends IBaseService<CouponRange> {

    List<CouponRangeDto> selectCouponRangeByCouponId(Long couponId);

    Integer deleteByCouponId(Long couponId);

}
