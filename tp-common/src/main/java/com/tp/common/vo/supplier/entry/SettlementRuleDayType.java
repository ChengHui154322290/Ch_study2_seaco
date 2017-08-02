package com.tp.common.vo.supplier.entry;

/**
 * 结算规则天数类型
 *
 * @author szy
 */
public enum SettlementRuleDayType {

    naturnlday("自然日", "naturnlday"),

    workday("工作日", "workday");

    private String name;

    private String value;

    private SettlementRuleDayType(final String name, final String value) {
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
