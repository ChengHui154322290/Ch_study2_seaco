package com.tp.common.vo.supplier.entry;

/**
 * {币别} <br>
 * Create on : 2014年12月23日 下午1:07:07<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum CurrencyType {

    /**
     * <code>YUAN</code> - {人民币}.
     */
    CNY("人民币", "CNY"),

    /**
     * <code>DOLLAR</code> - {美元}.
     */
    USD("美元", "USD"),

    /**
     * <code>EUR</code> - {欧元}.
     */
    EUR("欧元", "EUR"),

    /**
     * <code>GBP</code> - {英镑}.
     */
    GBP("英镑", "GBP"),

    /**
     * <code>HKD</code> - {港币}.
     */
    HKD("港币", "HKD"),

    /**
     * <code>JPY</code> - {日币}.
     */
    JPY("日币", "JPY"),

    /**
     * <code>ker</code> - {韩币}.
     */
    KER("韩币", "KER");

    private String name;

    private String value;

    private CurrencyType(final String name, final String value) {
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
