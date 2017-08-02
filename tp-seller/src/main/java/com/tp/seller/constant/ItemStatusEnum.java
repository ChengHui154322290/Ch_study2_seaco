package com.tp.seller.constant;

/**
 * <pre>
 * 商品状态枚举类
 * </pre>
 *
 * @author sqx
 * @version $Id: ItemStatusEnum.java, v 0.1 2014年12月30日 下午1:43:05 Administrator Exp $
 */
public enum ItemStatusEnum {

    OFFLINE("未上架", 0),

    ONLINE("已上架", 1),

    CANCEL("作废", 2);

    private String key;

    private Integer value;

    private ItemStatusEnum(final String key, final Integer value) {
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

    public static ItemStatusEnum getByValue(final Integer value) {
        if (null == value) {
            return null;
        }
        final ItemStatusEnum[] values = ItemStatusEnum.values();
        for (final ItemStatusEnum item : values) {
            if (item.getValue().intValue() == value.intValue()) {
                return item;
            }
        }
        return null;
    }
}
