package com.tp.common.util;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.tp.dto.common.FailInfo;

/**
 * 异常工具
 * @author szy
 *
 */
//@Component
public final class ExceptionUtils{
	//@Autowired
	//private static IHintInfoService hintInfoService=(IHintInfoService)SpringBeanProxy.getBean("hintInfoService");
	/**
	 * 打印日志
	 * @param error
	 * @param logger
	 * @return
	 */
	public static FailInfo println(FailInfo error,Logger logger){
		return println(error,logger,null,false,false);
	}
	
	/**
	 * 打印日志并且抛出异常
	 * @param error
	 * @param logger
	 * @return
	 */
	public static void printlnAndThrow(FailInfo error,Logger logger) throws Throwable{
		println(error,logger,4,null,false,true);
	}
	/**
	 * 打印日志、堆栈
	 * @param error
	 * @param logger
	 * @return
	 */
	public static FailInfo print(FailInfo error,Logger logger){
		return println(error,logger,null,true,false);
	}
	/**
	 * 打印日志、堆栈并且抛出异常
	 * @param error
	 * @param logger
	 * @return
	 */
	public static void printAndThrow(FailInfo error,Logger logger) throws Throwable{
		println(error,logger,4,null,true,true);
	}
	/**
	 * 打印日志
	 * @param error
	 * @param logger
	 * @param params 要打印的参数
	 * @return
	 */
	public static FailInfo println(FailInfo error,Logger logger,Object... params){
		return println(error,logger,params,false,false);
	}
	/**
	 * 打印日志、抛出异常
	 * @param error
	 * @param logger 
	 * @param params 要打印的参数
	 * @throws Throwable
	 */
	public static void printlnAndThrow(FailInfo error,Logger logger,Object... params) throws Throwable{
		println(error,logger,4,params,false,true);
	}
	
	/**
	 * 打印日志、堆栈
	 * @param error
	 * @param logger
	 * @param params 要打印的参数
	 * @return
	 */
	public static FailInfo print(FailInfo error,Logger logger,Object... params){
		return println(error,logger,params,true,false);
	}
	/**
	 * 打印日志、堆栈、抛出异常
	 * @param error
	 * @param logger
	 * @param params 要打印的参数
	 * @throws Throwable
	 */
	public static void printAndThrow(FailInfo error,Logger logger,Object... params) throws Throwable{
		println(error,logger,4,params,true,true);
	}
	
	private static FailInfo println(FailInfo error,Logger logger,Object[] params,Boolean isPrintStackTrace,Boolean isThrows){
		try{
			return println(error,logger,5,params,isPrintStackTrace,isThrows);
		}catch(Throwable e){
		}
		return error;
	}
	
	private static FailInfo println(FailInfo error,Logger logger,Integer currentNum,Object[] params,Boolean isPrintStackTrace,Boolean isThrows) throws Throwable{
		String stackString=getInfo(currentNum);
		Throwable exception = error.getException();
		if(exception!=null){
			StackTraceElement[] sa = exception.getStackTrace();
			if(sa !=null && sa.length>0){
				stackString = exception.getStackTrace()[0].toString();
				logger.error(stackString);
			}
		}
		if(error.getCode()!=null){
			/**HintInfo hintInfo = hintInfoService.queryHintInfoByModelAndCode(error.getModelType(), error.getCode());
			if(hintInfo!=null && StringUtils.isNoneBlank(hintInfo.getMessage())){
				error.setMessage(String.format(hintInfo.getMessage(), error.params));
			}*/
		}
		if(StringUtils.isBlank(error.getMessage()) && exception!=null){
			Throwable curThrowable = exception.fillInStackTrace();
			if(curThrowable!=null){
				error.setMessage(curThrowable.getMessage());
			}
		}
		logger.error(stackString
					+"\r\n\t\t\t\t\t\terror info\t"+error.toString()
					+"\r\n\t\t\t\t\t\tparems >>>>\t"+JSONArray.fromObject(params)+"\r\n");
		if(error.getException()!=null && isPrintStackTrace){
			error.getException().printStackTrace();
		}
		if(error.getException()!=null && isThrows){
			throw error.getException();
		}
		return error;
	}
	
	public static String getInfo() {
		return getInfo(3);
	}
	
	public static String getInfo(Integer currentSign) {
		StackTraceElement stack = Thread.currentThread().getStackTrace()[currentSign];
		return stack.getClassName() + "." + stack.getMethodName() + "()"+":" + stack.getLineNumber();
	}

}
