package com.tp.shop.controller.payment;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.m.enums.MResultInfo;
import com.tp.service.pay.IPaymentService;
import com.tp.shop.ao.payment.PayCallbackAO;

/**
 * 与支付瓶套的http交互入口 包括跳转到支付系统以及接受支付平台的支付、退款的回调请求
 * @author zhuss
 * @2016年1月13日 下午5:53:38
 */
@Controller
public class PayCallbackController {
	
	private Logger log = LoggerFactory.getLogger(PayCallbackController.class);
	
	@Autowired
	private PayCallbackAO payCallbackAO;
	
	/**
	 * 支付回调入口
	 * @param gateway url参数，支付方式代码(gateway_code: alipayDirect|union|weixin|kq|alipayInternational)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paycb/{gateway}")
	@ResponseBody
	public String callback(@PathVariable(value = "gateway") final String gateway, HttpServletRequest request) {
		// 检查是回调url是否为我们所设置,根据不同的网关回调不同的处理,处理成功后发送信息
		log.info("异步回调测试接口成功后发送消息通知业务系统");
		Map<String, String> requestMap = getRequestMap(request);
		log.info("回调参数：{}", requestMap);
		CallbackResultDto result = payCallbackAO.callback(gateway, requestMap,true);
		if (result == null || !result.isSuccess()) {
			log.info("异步回调返回结果:" + result);
			return MResultInfo.FAILED.message;
		} 
		log.info("异步回调返回结果:msg={}", result.getMessge());
		return result.getMessge();
	}
	
	/**
	 * 获取request里的参数列表并封装到Map中返回
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getRequestMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			Enumeration<String> enumration = request.getParameterNames();
			while (enumration.hasMoreElements()) {
				String key = enumration.nextElement();
				if ("get".equalsIgnoreCase(request.getMethod())) {
					params.put(key, new String(request.getParameter(key).getBytes("ISO-8859-1"), "utf-8"));
					log.info(key + ":" + new String(request.getParameter(key).getBytes("ISO-8859-1"), "utf-8"));
				} else {
					params.put(key, request.getParameter(key));
					log.info(key + ":" + request.getParameter(key));
				}
			}
			log.info("request params={}", params);
			if (request.getContentLength() > 0) {
				String requestContent = getRequestContent(request);
				log.info("request content={}", requestContent);
				params.put(IPaymentService.REQUEST_CONTENT_NAME, requestContent);
			}
			return params;
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return params;
	}

	private String getRequestContent(HttpServletRequest request) {
		try (BufferedReader reader = request.getReader();) {
			StringBuilder content = new StringBuilder();
			String line = null;
			do {
				line = reader.readLine();
				content.append(line);
			} while (line != null);

			return content.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
