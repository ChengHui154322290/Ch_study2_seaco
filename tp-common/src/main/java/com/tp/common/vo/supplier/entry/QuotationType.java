package com.tp.common.vo.supplier.entry;

/**
 * 报价单类型
 *
 * @author szy
 */
public enum QuotationType {

    CONTRACT_TYPE("合同报价单", "contract_quotation"), COMMON_TYPE("普通报价单", "common_quotation");

    private String name;

    private String value;

    private QuotationType(final String name, final String value) {
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
