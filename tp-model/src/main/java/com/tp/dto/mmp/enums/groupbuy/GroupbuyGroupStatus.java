package com.tp.dto.mmp.enums.groupbuy;

/**
 * 团状态
 * Created by ldr on 2016/3/16.
 */
public enum GroupbuyGroupStatus {

    /**
     * 进行中
     */
    PROCESSING(1),

    /**
     * 成功
     */
    SUCCESS(2),

    /**
     * 失败
     */
    FAILED(3);

    private int code;

    GroupbuyGroupStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
