package com.tp.common.vo.supplier.entry;

/**
 * {预约单类型} <br>
 * Create on : 2015年1月5日 下午6:20:16<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum PurcharseType {

    PURCHARSE("采购订单", "Purcharse"),

    PURCHARSE_RETURN("采购退货单", "PurcharseReturn"),

    SELL("代销订单", "sell"),

    SELL_RETURN("代销退货单", "sellReturn");

    private String name;

    private String value;

    private PurcharseType(final String name, final String value) {
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
