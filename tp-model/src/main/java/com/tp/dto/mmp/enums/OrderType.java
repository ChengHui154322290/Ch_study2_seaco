package com.tp.dto.mmp.enums;

/**
 * {class_description} <br>
 * Create on : 2014年12月21日 下午2:11:18<br>
 * 
 * @author szy
 * @version 0.0.1
 */
public enum OrderType {

    /**
     * <code>PURCHASE</code> - {采购}.
     */
    PURCHASE("Purchase", "采购"),

    /**
     * <code>CONSIGNMENT</code> - {代销}.
     */
    CONSIGNMENT("Consignment", "代销");

    private String key;

    private String value;

    private OrderType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getValue(String key) {
        for (OrderType c : OrderType.values()) {
            if (c.getKey() == key) {
                return c.key;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
