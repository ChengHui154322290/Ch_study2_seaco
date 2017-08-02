package com.tp.common.vo.supplier.entry;

/**
 * 商家平台用户状态 {class_description} <br>
 * Create on : 2015年1月15日 下午6:07:59<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum SupplierUserStatus {
	
	DISABLE("无效", "Disabled", 0),

    TAKEEFFECT("生效", "TakeEffect", 1),

    CANCEL("作废", "Cancel", 2),

    CONGEAL("冻结", "Congeal", 3);

    private String name;

    private String value;

    private Integer status;

    private SupplierUserStatus(final String name, final String value, final Integer status) {
        this.name = name;
        this.value = value;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getStatus() {
        return status;
    }
}
