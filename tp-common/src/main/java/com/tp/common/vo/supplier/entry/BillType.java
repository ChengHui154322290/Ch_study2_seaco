package com.tp.common.vo.supplier.entry;

/**
 * {审批流设置-单据类型} <br>
 * Create on : 2014年12月29日 下午6:03:31<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum BillType {

    SPLIST("供应商", "SPList"),

    CONSTRACT("合同", "Constract"),

    PRICE("报价单", "Price"),

    PURCHARSE("采购订单", "Purcharse"),

    PURCHARSE_RETURN("采购退货单", "PurcharseReturn"),

    SELL("代销订单", "sell"),

    SELL_RETURN("代销退货单", "sellReturn"),

    ORDER("预约单", "Order");

    private String name;

    private String value;

    private BillType(final String name, final String value) {
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
