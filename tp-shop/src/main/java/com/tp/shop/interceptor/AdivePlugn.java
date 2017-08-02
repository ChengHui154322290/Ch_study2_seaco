package com.tp.shop.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.m.util.JsonUtil;

//@Aspect
//@Component
public class AdivePlugn {
	Logger logger = LoggerFactory.getLogger(AdivePlugn.class);
	@Pointcut("execution (* com.tp.shop.controller.home.IndexController.*(..))")
	private void anyMethod() {
		logger.info("进入adive");
	}// 声明一个切入点

	@Before("anyMethod()")
	public void doAccessCheck(JoinPoint jp) {
		logger.info(jp.getSignature().toString()+" 入参：" +JsonUtil.convertObjToStr(jp.getArgs()));
	}


}
