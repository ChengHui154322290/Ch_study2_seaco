/**
 * 
 */
package com.tp.world.controller.mtdinvoke;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.tp.model.sys.ApicallLog;
import com.tp.proxy.sys.ApicallLogProxy;
import com.tp.world.helper.RequestHelper;

/**
 * @author Administrator
 *
 */
@Aspect
@Component
public class RestfulApiInvokeLogAspect {
	
	private final static Logger logger = LoggerFactory.getLogger(RestfulApiInvokeLogAspect.class);
	
	@Autowired
	private ApicallLogProxy apicallLogProxy;
	
	//电子口岸回执
	@Pointcut("execution(String com.tp.world.controller.order.JKFCallbackController.callbackJKF(..))")
	public void jkfApiInvoke(){}
	
	//WMS仓库回执
	@Pointcut("execution(String com.tp.m.controller.wms.*.callback*(..))")
	public void wmsApiInvoke(){}
	
	//电子口岸回执
	@Around(value = "jkfApiInvoke()")
	public Object jkfApiInvokeLog(ProceedingJoinPoint joinPoint) throws Throwable{
		long startTime = System.currentTimeMillis();
		Date startDate = new Date();
		//方法名
		String methodName = joinPoint.getSignature().getName();
		String resultVal = (String)joinPoint.proceed();
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest)args[0];
		if (!(request instanceof HttpServletRequest)) {
			logger.error("[RESTFUL_API_INVOKE_LOG]jkfApiInvokeLog: 参数类型错误");
			return resultVal;
		}
		long endTime = System.currentTimeMillis();
		ApicallLog log = assembleApiCallLog(methodName, request, resultVal, endTime - startTime, startDate);
		apicallLogProxy.insert(log);
		return resultVal;
	}
	
	//仓库回执
	@Around(value = "wmsApiInvoke()")
	public Object wmsApiInvokeLog(ProceedingJoinPoint joinPoint) throws Throwable{
//		try {
//			Object[] args = joinPoint.getArgs();
//			if (args == null || args.length <= 0) { //没有参数
//				logger.error("[RESTFUL_API_INVOKE_LOG]wmsApiInvokeLog: 参数为空");
//				return;
//			}
//			//方法名
//			String methodName = joinPoint.getSignature().getName();
//			String resultVal = (String)retVal;
//			HttpServletRequest request = (HttpServletRequest)args[0];
//			if (!(request instanceof HttpServletRequest)) {
//				logger.error("[RESTFUL_API_INVOKE_LOG]wmsApiInvokeLog: 参数类型错误");
//				return;
//			}
//			ApicallLog log = assembleApiCallLog(methodName, request, resultVal);
//			apicallLogProxy.insert(log);
//		} catch (Exception e) {
//			logger.error("[RESTFUL_API_INVOKE_LOG]wmsApiInvokeLog: 异常", e);
//		}
		long startTime = System.currentTimeMillis();
		Date startDate = new Date();
		//方法名
		String methodName = joinPoint.getSignature().getName();
		String resultVal = (String)joinPoint.proceed();
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest)args[0];
		if (!(request instanceof HttpServletRequest)) {
			logger.error("[RESTFUL_API_INVOKE_LOG]wmsApiInvokeLog: 参数类型错误");
			return resultVal;
		}
		long endTime = System.currentTimeMillis();
		ApicallLog log = assembleApiCallLog(methodName, request, resultVal, endTime - startTime, startDate);
		apicallLogProxy.insert(log);
		return resultVal;
	}
	
	private ApicallLog assembleApiCallLog(String methodName, HttpServletRequest request, String result, long timelapse, Date startDate){
		ApicallLog log = new ApicallLog();
		log.setUri(request.getRequestURI());
		log.setIp(request.getRemoteAddr());
		log.setMethod(request.getMethod());
		log.setContentType(request.getContentType());
		log.setContentLen(Long.valueOf(request.getContentLength()));
		Map<String, String[]> params = request.getParameterMap(); 
		log.setParam(JSONObject.toJSONString(params));
		try {
			log.setContent(RequestHelper.getJsonStrByIO(request));
		} catch (Exception e) {
			logger.error("[ASSEMBLE_API_CALLLOG]getJsonStrByIO: 异常");
		}		
		Map<String, String> headers = getHeaderInfo(request);
		log.setHeader(JSONObject.toJSONString(headers));
		log.setMethodName(methodName);
		log.setResult(result);
		log.setCreateTime(new Date());
		log.setRequestTime(startDate);
		log.setReturnTime(new Date());
		log.setTimelapse(timelapse);
		return log;
	}
	
	
	private Map<String, String> getHeaderInfo(HttpServletRequest request){
		Map<String, String> headers = new HashMap<>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String)headerNames.nextElement();
			String value = request.getHeader(key);
			headers.put(key, value);
		}
		return headers;
	}
}
