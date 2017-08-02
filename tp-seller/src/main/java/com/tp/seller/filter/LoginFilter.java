package com.tp.seller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tp.seller.constant.SellerConstant;
import com.tp.seller.util.SessionUtils;

/**
 * 登陆filter
 *
 * @author yfxie
 */
public class LoginFilter implements Filter {

    @SuppressWarnings("unused")
    private FilterConfig filterConfig;
    private FilterChain chain;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public void destroy() {
        this.chain = null;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain filterChain) throws IOException, ServletException {
        request = (HttpServletRequest) req;
        response = (HttpServletResponse) res;
        final Object obj = SessionUtils.getSession(SellerConstant.USER_ID_KEY, request);
        this.chain = filterChain;
        if (null == obj) {
            response.sendRedirect("/tologin");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

}
