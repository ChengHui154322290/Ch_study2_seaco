package com.tp.common.vo.supplier.entry;

/**
 * 升序或者降序枚举
 *
 * @author szy
 */
public enum SqlOrder {

    ORDER_DESC("升序", "desc"), ORDER_ASC("降序", "asc");

    private String name;
    private String value;

    private SqlOrder(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
