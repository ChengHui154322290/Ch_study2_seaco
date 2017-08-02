package com.tp.m.controller.wx;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.wx.CoreAO;
import com.tp.m.helper.AuthHelper;

/**
 * 核心管理器 
 * @author zhuss
 * @2016年1月16日 下午2:23:00
 */
@Controller
@RequestMapping("/core")
public class CoreController {

	private static final Logger log = LoggerFactory.getLogger(CoreController.class);
	
	@Autowired
	private CoreAO coreAO;
	
	/**
	 * 微信开发平台的服务器URL配置  确认请求来自微信服务器:用来校验
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	protected void coreGet(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		try{
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			String echostr = request.getParameter("echostr");// 随机字符串
			out = response.getWriter();
			// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
			if (AuthHelper.checkSignature(signature, timestamp, nonce)) {
				out.print(echostr);
			}
		}catch(Exception ex){
			log.error("[调用微信API的核心入口    GET  Excepiton] = {}",ex);
		}finally{
			out.close();
		}
	}

	/**
	 * 微信开发平台的服务器URL配置  确认请求来自微信服务器：用来返回
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	protected void corePost(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String respXml = coreAO.processRequest(request);
			// 响应消息
			out = response.getWriter();
			out.print(respXml);
		} catch (Exception ex) {
			log.error("[调用微信API的核心入口    POST  Excepiton] = {}", ex);
		} finally {
			out.close();
		}
	}
}
