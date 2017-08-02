package com.tp.common.vo.supplier.entry;

/**
 * {西客商城对接人类型} <br>
 * Create on : 2014年12月23日 下午1:26:42<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum DockingType {

    /**
     * <code>BUSINESS</code> - {业务}.
     */
    BUSINESS("业务对接人", "Business"),

    /**
     * <code>FINANCIAL</code> - {财务}.
     */
    FINANCIAL("财务对接人", "Financial"),

    /**
     * <code>WAREHOUSE</code> - {仓库}.
     */
    WAREHOUSE("仓库对接人", "Warehouse");

    private String name;

    private String value;

    private DockingType(final String name, final String value) {
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
