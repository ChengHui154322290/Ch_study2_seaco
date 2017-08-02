package com.tp.common.vo.supplier.entry;

/**
 * {预约单状态} <br>
 * Create on : 2015年1月5日 下午6:22:00<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum OrderStatus {

    EDITING("等待提交", "Editing", 0),

    SUCCESS("预约成功", "Success", 1),

    CANCEL("已取消", "Cancel", 2),

    DONE("已完成", "Done", 3);

    private String name;

    private String value;

    private Integer status;

    private OrderStatus(final String name, final String value, final Integer status) {
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
