package com.tp.seller.constant;

/**
 * <pre>
 * 商品销售类型枚举
 * </pre>
 *
 * @author sqx
 * @version $Id: ItemTypesEnum.java, v 0.1 2014年12月30日 下午1:46:30 sqx Exp $
 */
public enum ItemSaleTypeEnum {

    SEAGOOR("西客商城", 0), SELLER("商家", 1);

    private String key;

    private Integer value;

    private ItemSaleTypeEnum(final String key, final Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static ItemSaleTypeEnum getByValue(final Integer value) {
        if (null == value) {
            return null;
        }
        final ItemSaleTypeEnum[] values = ItemSaleTypeEnum.values();
        for (final ItemSaleTypeEnum item : values) {
            if (item.getValue().intValue() == value.intValue()) {
                return item;
            }
        }
        return null;
    }

}
