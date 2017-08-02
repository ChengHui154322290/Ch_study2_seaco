package com.tp.dto.mmp;

import java.io.Serializable;

public class MyCouponBasicDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5653163107098806936L;

    /**
     * 优惠券总数量
     */
    int totalCount;

    /**
     * 满减券数量
     */
    Integer normalCount;

    /**
     * 现金券数量
     */
    Integer redPacketCount;
    /**
     * 红包的总价钱
     * @deprecated
     */
    Double totalMoney;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(Integer normalCount) {
        this.normalCount = normalCount;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getRedPacketCount() {
        return redPacketCount;
    }

    public void setRedPacketCount(Integer redPacketCount) {
        this.redPacketCount = redPacketCount;
    }


}
