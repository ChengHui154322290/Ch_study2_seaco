package com.tp.dto.app.enums;

/**
 * Created by ldr on 2016/12/15.
 */
public enum SwitchEnum {

    SEAGOOR_PAY("sgp", "西客币支付"),;

    private String code;

    private String desc;

    SwitchEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
