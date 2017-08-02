package com.tp.m.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tp.common.vo.Constant.IMAGE_URL_TYPE;
import com.tp.m.base.BaseQuery;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.query.order.QueryOrder;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;

@Aspect
@Component
public class AdivePlugn {
	Logger logger = LoggerFactory.getLogger(AdivePlugn.class);
	@Pointcut("execution (* com.tp.m.controller.*.*Controller.*(..))")
	private void anyMethod() {
	}// 声明一个切入点

	@Around(value = "anyMethod()")
	public Object doAccessCheck(ProceedingJoinPoint joinPoint) {
		if(joinPoint.getArgs()==null || joinPoint.getArgs().length==0){
			try {
				return joinPoint.proceed();
			} catch (Throwable e) {
				return null;
			}
		}
		Object obj = joinPoint.getArgs()[0];
		String signature = joinPoint.getSignature().toString();
		String scheme = null;
		if(obj instanceof BaseQuery){
			scheme = ((BaseQuery)obj).getScheme();
		}else if(obj instanceof HttpServletRequest){
			HttpServletRequest request = (HttpServletRequest)obj;
			scheme = request.getHeader("X-Scheme");
			if(null==scheme){
				scheme = RequestHelper.getJsonStrByIO(request);
				if(StringUtil.isNotBlank(scheme)){
					try{
					BaseQuery baseQuery = (BaseQuery) JsonUtil.getObjectByJsonStr(scheme, BaseQuery.class);
					scheme = baseQuery.getScheme();
					}catch(Exception e){
						
					}
				}
			}
		}
		if(!"https".equalsIgnoreCase(scheme)){
			try {
				return joinPoint.proceed();
			} catch (Throwable e) {
			}
		}
		String result=null;
		String[] cmsReset = {"http://7xpsec.com1.z0.glb.clouddn.com/","http://7xpsec.com2.z0.glb.clouddn.com/","http://7xpsec.com2.z0.glb.qiniucdn.com/"};
		String[] itemReset = {"http://7xpcxl.com1.z0.glb.clouddn.com/","http://7xpcxl.com2.z0.glb.clouddn.com/","http://7xpcxl.com2.z0.glb.qiniucdn.com/"};		 
		try {
			result = (String) joinPoint.proceed();
			if(StringUtils.isNoneBlank(result)){
				result = result.replaceAll(IMAGE_URL_TYPE.item.url, IMAGE_URL_TYPE.item.sslUrl);
				result = result.replaceAll(IMAGE_URL_TYPE.cmsimg.url, IMAGE_URL_TYPE.cmsimg.sslUrl);
				result = result.replaceAll(IMAGE_URL_TYPE.basedata.url, IMAGE_URL_TYPE.basedata.sslUrl);
				result = result.replaceAll(IMAGE_URL_TYPE.supplier.url, IMAGE_URL_TYPE.supplier.sslUrl);
				result = result.replaceAll(cmsReset[0], IMAGE_URL_TYPE.cmsimg.sslUrl);
				result = result.replaceAll(cmsReset[1], IMAGE_URL_TYPE.cmsimg.sslUrl);
				result = result.replaceAll(cmsReset[2], IMAGE_URL_TYPE.cmsimg.sslUrl);
				result = result.replaceAll(itemReset[0], IMAGE_URL_TYPE.item.sslUrl);
				result = result.replaceAll(itemReset[1], IMAGE_URL_TYPE.item.sslUrl);
				result = result.replaceAll(itemReset[2], IMAGE_URL_TYPE.item.sslUrl);
				if(!signature.matches("^.*PayController.paymentOrder.*$") 
				&& !signature.matches("^.*OrderController.mergeSubmit.*$")
				&& !signature.matches("^.*OrderController.submit.*$"))
				result = result.replaceAll("http://m.51seaco.com", "https://m.51seaco.com");
			}
		} catch (Throwable e) {
		}
		return result;
	}


}
