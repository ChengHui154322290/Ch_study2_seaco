package com.tp.seller.constant;

/**
 * <pre>
 * 商品类型枚举
 * </pre>
 *
 * @author sqx
 * @version $Id: ItemTypesEnum.java, v 0.1 2014年12月30日 下午1:46:30 sqx Exp $
 */
public enum ItemTypesEnum {

    NORMAL("正常商品", 1),

    DUMMY("服务商品", 2),

    TWOHAND("二手商品", 3),

    ANNOUNCE("报废商品", 4);

    private String key;

    private Integer value;

    private ItemTypesEnum(final String key, final Integer value) {
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

    public static ItemTypesEnum getByValue(final Integer itemType) {
        if (null == itemType) {
            return null;
        }
        final ItemTypesEnum[] values = ItemTypesEnum.values();
        for (final ItemTypesEnum itemTypesEnum : values) {
            if (itemTypesEnum.getValue().intValue() == itemType.intValue()) {
                return itemTypesEnum;
            }
        }
        return null;
    }

}
