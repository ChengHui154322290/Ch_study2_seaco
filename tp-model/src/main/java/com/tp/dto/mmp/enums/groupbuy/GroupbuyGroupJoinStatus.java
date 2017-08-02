package com.tp.dto.mmp.enums.groupbuy;

/**
 * Created by ldr on 2016/3/16.
 */
public enum GroupbuyGroupJoinStatus {

    /**
     * 可参团
     */
    CAN_JOIN(1),

    /**
     * 已参团
     */
    JOINED(2),

    /**
     * 仅限新人参团
     */
    NEW_ONLY_JOIN(3),

    /**
     * 不可参团
     */
    NO_JOIN(4);

    private int code;

    GroupbuyGroupJoinStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
