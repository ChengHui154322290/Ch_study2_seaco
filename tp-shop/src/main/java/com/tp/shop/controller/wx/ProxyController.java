/**
 * 
 */
package com.tp.shop.controller.wx;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author Administrator
 *
 */
@Controller
public class ProxyController {
	private static Logger log = Logger.getLogger(ProxyController.class);
	private static int visitNum = 0;
	private static String url = "http://1eb8eb19.ngrok.io";
	@RequestMapping("proxy/**/*")
	public void getSignature(HttpServletResponse response, HttpServletRequest request){
		System.out.println(request.getServletPath().substring(7));
		String path = request.getServletPath().substring(7);
		try {
	        PrintWriter out = response.getWriter();  
	        out.write(get(request, url+"/"+path+"?1=1")); 
	        out.flush();  
		} catch (Exception e) {
			e.printStackTrace();
		}     
	}
	@RequestMapping("proxy/seturl")
	public void setUrl(HttpServletResponse response, HttpServletRequest request){
		if(request.getParameter("myUrl") != null) {
			url = request.getParameter("myUrl");
		}
		System.out.println(url);
	}
	
	public String get(HttpServletRequest request, String url) {
		HttpMethod method = null;
		PostMethod post = null;
		Map<String, String[]> map = request.getParameterMap();
		System.out.println(map);
		HttpClient client = new HttpClient();
		try {
			if("get".equalsIgnoreCase(request.getMethod())){
				String params = "";
				for(String s : map.keySet()) {
					params += ("&"+ s+"="+map.get(s)[0]);
				}
				method = new GetMethod(url+params);
				client.executeMethod(method);
				return new String(method.getResponseBody(), "utf-8");
			}
			else{
				post = new PostMethod(url);
				post.setRequestBody(request.getInputStream());
				client.executeMethod(post);
				return new String(post.getResponseBody(), "utf-8");
			}
			
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(method != null)
				method.releaseConnection();
			if(post != null)
				post.releaseConnection();
		}
		return "";
	}
	
}
