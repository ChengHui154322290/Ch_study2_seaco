package com.tp.common.vo.supplier.entry;

/**
 * 结算规则选择状态
 * 
 * @author szy
 */
public enum SettlementRuleType {

    activityTime("按活动周期结算", "activityTime");

    private String name;
    private String value;

    private SettlementRuleType(final String name, final String value) {
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
