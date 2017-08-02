package com.tp.dto.mmp.enums.groupbuy;

/**
 * Created by ldr on 2016/3/16.
 */
public enum GroupbuyGroupLaunchStatus {

    /**
     * 可发起团购
     */
    CAN_LAUNCH(1),

    /**
     * 已发起规定数量的团购
     */
    LAUNCHED(2);

    private int code;

    GroupbuyGroupLaunchStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
