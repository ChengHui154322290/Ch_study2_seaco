package com.tp.backend.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Administrator<br>
 * @version 0.0.1-SNAPSHOT v0.0.1
 */
public final class WebUtil {

    /**
     * Constructors.
     */
    private WebUtil() {
    }

    /**
     * 获取访问路径的上下文.
     * 
     * @param request HttpServletRequest
     * @return String(http://serverName:serverPort/context)
     */
    public static String getCtx(final HttpServletRequest request) {
        final StringBuffer ctx = new StringBuffer();
        ctx.append(getServerPath(request));
        ctx.append(request.getContextPath());
        return ctx.toString();
    }

    /**
     * 获取访问的http url.
     * 
     * @param request HttpServletRequest
     * @return String(http://serverName:serverport)
     */
    public static String getServerPath(final HttpServletRequest request) {
        final StringBuffer serverPath = new StringBuffer();
        serverPath.append(request.getScheme()).append("://");
        serverPath.append(request.getServerName()).append(":");
        serverPath.append(request.getServerPort());
        return serverPath.toString();
    }

    /**
     * {获取当前用户}.
     * 
     * @param request HttpServletRequest
     * @return String
     */
    public static String getCurrentUser(final HttpServletRequest request) {
        Object currentUser = request.getSession().getAttribute("currentUser");
        if (currentUser != null) {
            return currentUser.toString();
        }
        return null;
    }


}
