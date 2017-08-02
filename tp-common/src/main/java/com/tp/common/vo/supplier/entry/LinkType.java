package com.tp.common.vo.supplier.entry;

/**
 * {联系人类型} <br>
 * Create on : 2014年12月29日 下午6:35:56<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum LinkType {

    BUSINESS("业务联系人", "Business"),

    FINANCIAL("财务联系人", "Financial"),

    LOGISTICS("物流对接人", "Logistics"),

    AFTERSALE("售后联系人", "Aftersale");

    private String name;

    private String value;

    private LinkType(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
