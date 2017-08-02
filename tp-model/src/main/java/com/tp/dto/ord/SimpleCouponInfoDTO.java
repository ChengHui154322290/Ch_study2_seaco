package com.tp.dto.ord;

import java.io.Serializable;

/**
 * 简单优惠券信息对象DTO
 *
 * @author szy
 */
public class SimpleCouponInfoDTO implements Serializable {

    private static final long serialVersionUID = -8412441594270643963L;

    /** 用户优惠券Id */
    private Long couponUserId;

    /** 面值 */
    private Integer faceValue;

    /** 优惠券类型 0 : 满减券 1：现金券 */
    private Integer couponType;

    /** 发券主体 **/
    private Integer sourceType;

    public Long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(final Long couponUserId) {
        this.couponUserId = couponUserId;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(final Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(final Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(final Integer sourceType) {
        this.sourceType = sourceType;
    }

}
