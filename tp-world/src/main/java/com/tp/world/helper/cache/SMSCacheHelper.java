package com.tp.world.helper.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.mem.SessionKey;
import com.tp.m.enums.CaptchaType;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.redis.util.JedisCacheUtil;

/**
 * 短信验证码相关缓存
 * @author zhuss
 * @2016年1月27日 下午2:53:23
 */
@Service
public class SMSCacheHelper {
	private static final Logger log = LoggerFactory.getLogger(SMSCacheHelper.class);
	
	public static final int SMS_CODE_EXPIRE_TIME = 60 * 15; //手机验证码存活时间
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	/**
	 * 设置手机号验证码
	 * @param token
	 */
	public void setSMSCache(String key , String random){
		boolean result =  jedisCacheUtil.setCache(key, random, SMS_CODE_EXPIRE_TIME);
		if(!result){
			log.error("[缓存工具 -设置手机验证码 失败] = {}",MResultInfo.CACHE_SET_FAILED.message);
			throw new MobileException(MResultInfo.CACHE_SET_FAILED);
		}
	}
	
	/**
	 * 获取手机号验证码
	 * @param key
	 * @return
	 */
	public String getSMSCache(String key){
		return StringUtil.getStrByObj((Integer) jedisCacheUtil.getCache(key));
	}
	
	/**
	 * 设置要存的手机验证码
	 * @param key：缓存的KEY
	 * @param type：需要存入缓存的类型
	 * @return
	 */
	public void setCacheCode(String type,String tel,String code){
		StringBuffer smsCodeKey = new StringBuffer();
		if(StringUtil.equals(type, CaptchaType.RECEIVE_COUPON)){
			smsCodeKey.append(tel).append(":").append(SessionKey.RECEIVE_COUPON.value);
		}
		setSMSCache(smsCodeKey.toString(),code);
	}
	
	/**
	 * 比较传入的验证码和缓存里的验证码
	 * @param key：缓存的KEY
	 * @param code：被比较的验证码
	 * @return
	 */
	public void compareCode(String tel,SessionKey key,String code){
		StringBuffer smsCodeKey = new StringBuffer();
		smsCodeKey.append(tel).append(":").append(key.value);
		String cacheCode = getSMSCache(smsCodeKey.toString());
		if(!StringUtil.equals(cacheCode, code))throw new MobileException(MResultInfo.CAPTCHA_ERROR);
	}
}
