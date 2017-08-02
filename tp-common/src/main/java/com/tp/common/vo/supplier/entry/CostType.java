package com.tp.common.vo.supplier.entry;

/**
 * {费用类型} <br>
 * Create on : 2014年12月29日 下午7:06:06<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum CostType {

    //DEPOSIT("押金", "Deposit"),

    //ROYALTIES("平台使用费", "Royalties");
	REBATE("返利", "rebate");

    private String name;

    private String value;

    private CostType(final String name, final String value) {
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
