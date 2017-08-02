package com.tp.seller.util; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tp.model.usr.UserInfo;
import com.tp.seller.constant.SellerConstant;

/**
 * 设置session
 *
 * @author yfxie
 */
public final class SessionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

    /**
     * 设置session的值
     *
     * @param key
     * @param val
     * @param request
     */
    public static void setSession(final String key, final Object val, HttpServletRequest request) {
        final HttpSession session = getSession(request);
        if (null == session || null == key) {
            return;
        }
        session.setAttribute(key, val);
    }

    /**
     * 获取session
     * @param request
     */
    public static Object getSession(final String key, HttpServletRequest request) {
        final HttpSession session = getSession(request);
        if (null == session || null == key) {
            return null;
        }
        return session.getAttribute(key);
    }

    public String getJSessionId(){
		String jtxSessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		return jtxSessionId;
	}
    
    public static Long getSupplierId(){
		Object obj = RequestContextHolder.getRequestAttributes().getAttribute(SellerConstant.SUPPLIER_ID_KEY, RequestAttributes.SCOPE_SESSION);
		return (Long)obj;
	}
    
    public static String getUserName(){
		Object obj = RequestContextHolder.getRequestAttributes().getAttribute(SellerConstant.USER_NAME_KEY, RequestAttributes.SCOPE_SESSION);
		return (String)obj;
	}
    /**
     * 获取用户名
     * @param request 
     * @param session
     *
     * @return
     */
    public static String getUserName(HttpServletRequest request) {
        final HttpSession session = getSession(request);
        if (null == session) {
            return null;
        }
        return (String) session.getAttribute(SellerConstant.USER_NAME_KEY);
    }

    /**
     * 获取供应商id
     * @param request 
     *
     * @return
     */
    public static Long getSupplierId(HttpServletRequest request) {
        final HttpSession session = getSession(request);
        if (null == session) {
            return null;
        }
        return (Long) session.getAttribute(SellerConstant.SUPPLIER_ID_KEY);
    }

    /**
     * 获取用户id
     * @param request
     * @param session
     *
     * @return
     */
    public static Long getUserId(HttpServletRequest request) {
        final HttpSession session = getSession(request);
        if (null == session) {
            return null;
        }
        return (Long) session.getAttribute(SellerConstant.USER_ID_KEY);
    }

    /**
     * 获取session
     *
     * @return
     */
    private static HttpSession getSession(HttpServletRequest request) {
        if (request != null) {
            return request.getSession();
        }

        return null;
    }

    /**
     * 清空session
     * @param request 
     */
    public static void clearSession(HttpServletRequest request) {
        // 清空Cookie和session
        if (request == null || request.getSession() == null) {
            return;
        }
        try {
            request.getSession().invalidate();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 验证是否是海淘供应商
     * 
     * @param request
     */
    public static boolean checkIsHaitao(HttpServletRequest request){
    	final HttpSession session = getSession(request);
        if (null == session) {
        	LOGGER.error("Session is null.");
            return false;
        }
        if(null != session.getAttribute(SellerConstant.IS_HAITAO_KEY)){
        	return (Boolean)session.getAttribute(SellerConstant.IS_HAITAO_KEY);
        } else {
        	return false;
        }
    }

}
