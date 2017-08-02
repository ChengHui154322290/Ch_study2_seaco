package com.tp.backend.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author yuwenjie
 * @version $Id: PerFilter.java, v 0.1 2015年1月29日 下午6:22:11 yuwenjie Exp $
 */
public class PasswordSafeFilter extends PathMatchingFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {

		HttpServletRequest req = (HttpServletRequest) request;
		
		Boolean passIsSafe = (Boolean) req.getSession().getAttribute("passIsSafe");
		
		if(!passIsSafe){//密码不安全
			if("/permission/user/toResetPassword".equals(req.getServletPath())
		|| "/permission/user/updatePassword.htm".equals(req.getServletPath())) return true;
			
			logger.info(passIsSafe.toString());
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendRedirect("/permission/user/toResetPassword");
		}
		return true;
	}
}
