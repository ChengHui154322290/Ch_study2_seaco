package com.tp.dto.mmp.enums.groupbuy;

/**
 * Created by ldr on 2016/3/16.
 */
public enum GroupbuyType {

    NORMAL(1),

    NEW_ONLY(2);

    private int code;

    GroupbuyType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
