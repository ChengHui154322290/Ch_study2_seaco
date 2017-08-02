package com.tp.shop.interceptor;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.shop.helper.AuthHelper;

/**
 * 拦截器，用来验证数据有效性，包括公用的URL属性和Token
 * @author zhuss
 */
public class AuthInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

	/**不需要签名*/
	String[] notFilter = new String[] {"/file/","/wx/","/core.htm"};
	
	String[] specialFilter = new String[] {"/user/getcaptcha"};
	
	/**
	 * 业务处理器处理请求之前被调用
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//过滤拦截
		if(!getValidateSign(request)){
			return Boolean.TRUE;
		}
		//验证签名
		try{
			AuthHelper.authSign(request);
		}catch(MobileException me){
			writeContent(response,JSON.toJSONString(new MResultVO<>(me.getMessage())));
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	

	/**
	 * 业务处理器处理完请求后，但是DispatcherServlet向客户端返回请求前被调用，在该方法中对用户请求request进行处理。
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//log.info("method = {}","postHandle");
	}

	/**
	 * 这个方法在DispatcherServlet完全处理完请求后被调用，可以在该方法中进行一些资源清理的操作。
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//log.info("method = {}","afterCompletion");
	}
	
	/**
	 * 是否需要签名
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private boolean getValidateSign(HttpServletRequest request) throws IOException{
		boolean result=true;
		
		if(notFilter==null || notFilter.length==0) return result;
		
		String url=request.getRequestURL().toString();
		
		 for (String s : notFilter) {
             if (url.indexOf(s) != -1) {
            	 result = false;
                 break;
             }
         }
		 String channelCode = request.getHeader("subdomain");
		 if(StringUtil.isBlank(channelCode)){
			 log.info("平台接入方不存在 url[{}]",request.getRequestURL());
			 result = false;
		 }
		 return result;
	}
	
	/**
	 * 返回信息
	 * @param response
	 * @param str
	 */
	private void writeContent(HttpServletResponse response,String str){
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			ServletOutputStream out= null;
			out = response.getOutputStream();
			out.write(str.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
