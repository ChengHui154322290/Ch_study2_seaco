package com.tp.query.mmp;

import java.io.Serializable;

import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/1/11.
 */
public class MyCouponQuery extends BaseDO implements Serializable {

    private static final long serialVersionUID = 570602036718100362L;

    /**
     * 用户Id 非空
     */
    private Long memberId;
    /**
     * 优惠券类型
     *@see  CouponType
     */
    private CouponType couponType;
    /**
     * 状态  非空
     *@see  CouponUserStatus
     */
    private CouponUserStatus couponUserStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public CouponUserStatus getCouponUserStatus() {
        return couponUserStatus;
    }

    public void setCouponUserStatus(CouponUserStatus couponUserStatus) {
        this.couponUserStatus = couponUserStatus;
    }

}
