package com.tp.seller.constant;

import org.apache.commons.lang.StringUtils;

/***
 * 海淘商品 配送方式枚举类
 * 
 * @author caihui
 */
public enum ItemSendTypeEnum {

    DT("DT", "海外直购"), GN("GN", "国内直发"), BA("BA", "保税区直发");
    private String code;

    private String desc;

    ItemSendTypeEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        final ItemSendTypeEnum[] values = ItemSendTypeEnum.values();
        for (final ItemSendTypeEnum item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }

}
