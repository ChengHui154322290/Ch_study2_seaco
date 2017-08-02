package com.tp.backend.controller.supplier.ao;

import org.springframework.stereotype.Service;

import com.tp.proxy.usr.UserHandler;

/**
 * 请不要将此类依赖于任何AO
 *
 * @author yfxie
 */
@Service
public class SupplierUtilAO {

    /**
     * <pre>
     * 获取当前用户的userId
     * </pre>
     *
     * @param request
     * @return
     */
    public static Long currentUserId() {
        return UserHandler.getUser().getId();
    }

    public static String getCurrentUserName() {
        return UserHandler.getUser().getLoginName();
    }

    /**
     * 获取角色名称
     *
     * @param request
     * @return
     */
    public static String getRoleName() {
        return UserHandler.getUser().getRoleName();
    }

    /**
     * <pre>
     * 获取当前用户的roleId
     * </pre>
     *
     * @param request
     * @return
     */
    public static Long currentRoleId() {
        // TODO
        return -1L;
    }

    /**
     * 获取当前用户名
     *
     * @param request
     * @return
     */
    public static String currentUserName() {
        return UserHandler.getUser().getUserName();
    }

}
