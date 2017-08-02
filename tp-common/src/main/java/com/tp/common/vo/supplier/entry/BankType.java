package com.tp.common.vo.supplier.entry;

/**
 * {银行帐号类型} <br>
 * Create on : 2014年12月23日 下午1:21:16<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum BankType {

    /**
     * <code>COLLECTION</code> - {收款银行}.
     */
    COLLECTION("收款银行", "Collection"),

    /**
     * <code>payment</code> - {付款银行}.
     */
    PAYMENT("付款银行", "Payment");

    private String name;

    private String value;

    private BankType(final String name, final String value) {
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
