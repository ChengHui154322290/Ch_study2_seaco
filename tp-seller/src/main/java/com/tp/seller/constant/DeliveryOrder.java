package com.tp.seller.constant;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryOrder {

    ORDER_CODE("orderCode", "订单号"),

    ORDER_DATE("orderDate", "下单时间"),
    
    ORDER_STATUS("orderStatus", "订单状态"),

    PAY_TIME_STR("payTimeStr", "付款时间"),

    NICK_NAME("nickName", "买家账号"),

    PAY_TYPE("payType", "支付方式"),

    PAY_CODE("payCode", "支付单号"),

//    TOTAL_AMOUNT_STR("totalAmountStr", "实付金额"),
//
//    DIS_COUNT("discount", "优惠金额"),

    CUSTOM_CODE("customCode", "保税区备案号"),

    CONIGNEE_NAME("consigneeName", "收件人"),

    MOBILE("mobile", "手机"),

    POST_CODE("postCode", "邮编"),

    PROVINCE("province", "省"),

    CITY("city", "市"),

    COUNTY("county", "区"),

    ADDRESS("adress", "地址"),

    IDENTITY_CODE("identityCode", "身份证号"),

    REAL_NAME("realName", "真实姓名"),

    TITLE_STR("titleStr", "发票抬头"),

    SOURCE_STR("sourceStr", "订单来源"),

    REMARK("remark", "备注"),

    PRODUCT_NAME("productName", "商品名称"),

    PRODUCT_CODE("productCode", "SKU"),

    BAR_CODE("barCode", "商品条形码"),

    DELIVERY_WAY("deliveryWay", "海关货号"),

    UNIT("unit", "单位"),

    QUANTITY("quantity", "购买数量"),

    PRICE("price", "单价"),

    WEIGHT("weight", "重量"),

    BRAND_NAME("brand_name", "品牌名称"),

    EXPRESS_NAME("expressName", "快递名称"),

    EXPRESS_CODE("expressCode", "快递编号"),

    PACKAGE_NO("packageNo", "运单号");

    public static final Map<String, String> ORDER_FIELD_MAP = new HashMap<String, String>();
    // 成员变量
    private String key;
    private String keyName;

    static {
        /**
         * 字段和名称map
         */
        for (final DeliveryOrder orderField : DeliveryOrder.values()) {
            ORDER_FIELD_MAP.put(orderField.getKey(), orderField.getKeyName());
        }
    }

    private DeliveryOrder(final String key, final String keyName) {
        this.key = key;
        this.keyName = keyName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

}
