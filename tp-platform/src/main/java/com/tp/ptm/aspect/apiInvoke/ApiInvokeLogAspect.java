package com.tp.ptm.aspect.apiInvoke;

import java.util.Date;
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
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.model.ptm.PlatformItemLog;
import com.tp.proxy.ptm.PlatformItemLogProxy;


/**
 * 
 * @author chenghui
 *
 */
@Aspect
@Component
public class ApiInvokeLogAspect {
	
	@Autowired
	private PlatformItemLogProxy platformItemLogProxy;
	
	private final static Logger logger = LoggerFactory.getLogger(ApiInvokeLogAspect.class);
	
	@Pointcut("execution(com.tp.dto.ptm.ReturnData com.tp.ptm.controller.*.*Controller.push*(..))")
	public void ptmApiInvoke(){}
	
	@Around(value = "ptmApiInvoke()")
	public com.tp.dto.ptm.ReturnData ptmApiInvokeLog(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("==============================================================================");
		//方法名
		String methodName = joinPoint.getSignature().getName();
		com.tp.dto.ptm.ReturnData resultVal = (com.tp.dto.ptm.ReturnData)joinPoint.proceed();
		Object[] args = joinPoint.getArgs();
		Object request = args[args.length-1];
//		if (!(request instanceof HttpServletRequest)) {
//			logger.error("[PLATFORM_API_INVOKE_LOG]jkfApiInvokeLog: 参数类型错误");
//			return new com.tp.dto.ptm.ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
//		}
		PlatformItemLog log = assemblePlatformItemLog(methodName, request, resultVal);
		platformItemLogProxy.insert(log);
		return resultVal;
	}
	/**
	 * 拼日志数据
	 * @param methodName
	 * @param request
	 * @param result
	 * @param timelapse
	 * @param startDate
	 * @return
	 */
	private PlatformItemLog assemblePlatformItemLog(String methodName, Object request, com.tp.dto.ptm.ReturnData result){
		PlatformItemLog log = new PlatformItemLog();
//		Map<String, String[]> params = request.getParameterMap(); 
		log.setContent(JSONObject.toJSONString(request));
//		Map<String, String> headers = getHeaderInfo(request);
		log.setType(methodName);
		log.setResponse(JSONObject.toJSONString(result));
		log.setCreateTime(new Date());
		log.setCreateUser("systemAuto");
		return log;
	}
}
