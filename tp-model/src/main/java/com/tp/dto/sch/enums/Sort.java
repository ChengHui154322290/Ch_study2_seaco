package com.tp.dto.sch.enums;

/**
 * Created by ldr on 2016/2/18.
 */
public enum Sort {

    PRICE_DESC(1, "topic_price", "-"),

    PRICE_ASC(2, "topic_price", "+"),

    SALES_COUNT_DESC(3, "sales_count", "-"),

    SALES_COUNT_ASC(4, "sales_count", "+"),

    ITEM_NEW(5,"item_id","-"),

    TOPIC_CREATE_TIME(6,"topic_start","-"),

    DEFAULT(7,"RANK","-");


    private int code;

    private String field;

    private String type;

    Sort(int code, String field, String type) {
        this.code = code;
        this.field = field;
        this.type = type;
    }

    public static Sort getByCode(Integer code) {
        if (code == null) return null;
        for (Sort sort : Sort.values()) {
            if (sort.code == code) {
                return sort;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    public String getType() {
        return type;
    }
}
