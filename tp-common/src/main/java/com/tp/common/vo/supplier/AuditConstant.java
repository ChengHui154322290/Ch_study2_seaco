package com.tp.common.vo.supplier;

import java.util.HashMap;
import java.util.Map;

import com.tp.common.vo.supplier.entry.AuditRoleType;
import com.tp.common.vo.supplier.entry.BillType;

/**
 * <pre>
 * 审核流程常量
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public final class AuditConstant {

    /** 角色map */
    public static final Map<String, String> AUDIT_ROLE_MAP = new HashMap<String, String>();

    /** 单据类型 */
    public static final Map<String, String> BILL_TYPE_MAP = new HashMap<String, String>();

    private AuditConstant() {

    }

    static {
        final BillType[] tList = BillType.values();
        for (final BillType t : tList) {
            BILL_TYPE_MAP.put(t.getValue(), t.getName());
        }

        final AuditRoleType[] auditRoles = AuditRoleType.values();
        for (final AuditRoleType ar : auditRoles) {
            AUDIT_ROLE_MAP.put(ar.getValue(), ar.getName());
        }
    }

}
