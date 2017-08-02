package com.tp.service.stg.mtdinvoke;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.redis.util.JedisCacheUtil;


@Aspect
//@Service
public class MethodInvokeReportService {

	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	@Pointcut("execution(* com.tp.service.stg.*.*(..))")
	public void recordMethodInvokeInfo(){
	}
	
	static String ip = "";
	static{
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip=addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
		}
	}
	
	
	
	public static final String STORAGEINVOKEKEY = "storage-method-invoke";
	
	@Before("recordMethodInvokeInfo()")
	public void doBeforeInvoke(JoinPoint joinPoint){
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		Map<String, Integer> map = (Map<String, Integer>)jedisCacheUtil.getCache(STORAGEINVOKEKEY);
		if(null==map){
			map = new HashMap<String, Integer>();
		}
		Integer info = map.get(ip+"-"+className+"-"+methodName);
		if(null==info){
			info = 0;
		}
		info++;
		map.put(ip+"-"+className+"-"+methodName, info);
		jedisCacheUtil.setCache(STORAGEINVOKEKEY, map,24*60*60);
	}
	
	private String appendWhiteSpace(int len,String key){
		int keyLen = key.length();
		for(int i=0;i<len-keyLen;i++){
			key=key+" ";
		}
		return key;
	}
}
