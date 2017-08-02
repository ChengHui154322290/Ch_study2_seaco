package com.tp.dto.mmp;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/1/26.
 */
public class CouponReceiveDTO extends BaseDO {

    private static final long serialVersionUID = 2109215027301325535L;

    /**
     * 用户Id
     */
    private Long memberId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 优惠券code
     */
    private String code;


    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
