package com.tp.common.vo.supplier.entry;

/**
 * <pre>
 * 审批人类型
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public enum AuditRoleType {

    SUBMIT("提交人", "submitUser"), EXAMING("审批人", "examineUser"), ENDING("结束人", "endUser");

    private String name;
    private String value;

    private AuditRoleType(final String name, final String value) {
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
