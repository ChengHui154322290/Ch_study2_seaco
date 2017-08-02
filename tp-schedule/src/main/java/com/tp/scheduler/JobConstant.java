package com.tp.scheduler;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.redis.util.JedisCacheUtil;

/**
 * 任务调度常量
 * @author szy
 *
 */
@Component
public class JobConstant {
	private static final Logger LOG = LoggerFactory.getLogger(JobConstant.class);
	
	@Autowired
	private Properties settings;
	@Autowired
	private  JedisCacheUtil jedisCacheUtil;
	
	public boolean isGobleRunnable(){
		return isRunnableByJobBasePre("goble");
	}
	
	public boolean isRunnableByJobPreName(String preFixed){
		return isGobleRunnable() && isRunnableByJobBasePre(preFixed) && isRunningSign(preFixed);
	}
	
	public boolean isRunnableByJobBasePre(String preFixed){
		String runnable = settings.getProperty(preFixed+".isrun");
		if("true".equalsIgnoreCase(runnable) || "yes".equalsIgnoreCase(runnable)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	public Integer getCancelUnpayMinute(){
		String minute = settings.getProperty("cancelorder.expiredminute");
		if(StringUtils.isNotBlank(minute) && NumberUtils.isNumber(minute)){
			return Integer.valueOf(minute);
		}
		return null;
	}
	
//用户下单15分钟内未付款
	public Integer getSendMobileMessageMinute(){
		String minute = settings.getProperty("sendmobilemessage.expiredminute");
		if(StringUtils.isNotBlank(minute) && NumberUtils.isNumber(minute)){
			return Integer.valueOf(minute);
		}
		return null;
	}
	
	public Integer getReceivedDays(){
		String minute = settings.getProperty("receivedgoods.receiveddays");
		if(StringUtils.isNotBlank(minute) && NumberUtils.isNumber(minute)){
			return Integer.valueOf(minute);
		}
		return null;
	}
	
	public Properties getProperties() {
		return settings;
	}
	
	public boolean isRunningSign(String preFixed){
		final String key = "job:salesorder:"+preFixed;
		synchronized (key) {
			if(jedisCacheUtil.lock(key)){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public void cleanSign(String preFixed){
		jedisCacheUtil.unLock("job:salesorder:"+preFixed);
	}
	
	public String[] getUnPutPayedOrder(){
		String unPutPayedOrders = getProperties().getProperty("putpayedunputsuborder.unputpayedorder");
		if(StringUtils.isNoneBlank(unPutPayedOrders)){
			return unPutPayedOrders.split(",");
		}
		return null;
	}
	
	public String[] getExpressCompanyCode(){
		String codes = getProperties().getProperty("seawaybillnocheck.companycodes");
		if (StringUtils.isNoneBlank(codes)) {
			return codes.split(",");
		}
		return null;
	}
}
