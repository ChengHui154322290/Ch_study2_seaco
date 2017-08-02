package com.tp.common.vo.supplier.entry;

/**
 * {供应商业务类型,分为自营与非自营} <br>
 * Create on : 2015年3月22日 上午11:44:57<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum SupplierBusinessType {

    SEAGOOR("西客商城", "SEAGOOR"),

    /**
     * <code>SELLER</code> - {非自营}.
     */
    SELLER("商家", "SELLER");

    private String name;

    private String value;

    private SupplierBusinessType(final String name, final String value) {
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
