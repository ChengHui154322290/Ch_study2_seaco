package com.tp.common.vo.supplier.entry;

/**
 * 合同主体（甲方）
 */
public enum ContractPartyA {

	HZSG("杭州西客商城有限公司", "HZSG");

    private String name;
    private String value;

    private ContractPartyA(final String name, final String value) {
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
