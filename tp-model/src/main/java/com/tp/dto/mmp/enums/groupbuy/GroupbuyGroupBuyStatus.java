package com.tp.dto.mmp.enums.groupbuy;

/**
 * Created by ldr on 2016/3/16.
 */
public enum GroupbuyGroupBuyStatus {

    /**
     * 可购买
     */
    CAN_BUY(1),

    /**
     * 已购买
     */
    BOUGHT(2),

    /**
     * 超时
     */
    TIMEOUT(3),

    /**
     * 不可购买
     */
    NO_BUY(4),

    /**
     * 达到限购
     */
    ACHIEVE_LIMIT(5);


    private int code;

    GroupbuyGroupBuyStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
