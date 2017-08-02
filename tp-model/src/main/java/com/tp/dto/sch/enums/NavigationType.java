package com.tp.dto.sch.enums;

/**
 * Created by ldr on 2016/2/26.
 */
public enum NavigationType {
    CATEGORY(1,"分类"),

    BRAND(2,"品牌");

    private int code;

    private String desc;

    NavigationType(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
