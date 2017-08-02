package com.tp.dto.prd.enums;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * 商品类型枚举
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public enum SellerItemBindLevelEnum {

    SKU("SKU", "SKU层级"), PRD("PRD", "PRD层级"), SPU("SPU", "SPU层级");

    private String code;

    private String desc;

    SellerItemBindLevelEnum(final String code, final String desc) {
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
