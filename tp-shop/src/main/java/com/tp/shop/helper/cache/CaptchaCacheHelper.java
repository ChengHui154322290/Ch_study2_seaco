package com.tp.shop.helper.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.verify.CaptchaVO;
import com.tp.redis.util.JedisCacheUtil;

/**
 * 验证码缓存工具类
 * @author zhuss
 */
@Service
public class CaptchaCacheHelper {

	public static final int KAPTCHA_LIVE = 60;//图形验证码存活时间 单位为秒
	
	public static final String GEETEST_KEY = "gt_server_status";//极验验证API服务状态Key
	public static final int GEETEST_LIVE = 60;//极验验证码存活时间 单位为秒

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	/**
	 * 用于存储用户图形验证码 - 持久化
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean setKaptchaCache(String key,CaptchaVO obj){
		return jedisCacheUtil.setCache(key, obj, KAPTCHA_LIVE);
	}
	
	public CaptchaVO getKaptchaCache(String key){
		return (CaptchaVO) jedisCacheUtil.getCache(key);
	}
	
	/**
	 * 用于获取用户图形验证码 - 持久化
	 * @param key
	 * @return
	 */
	public CaptchaVO getKaptchaCache(String key,String value){
		CaptchaVO vo = (CaptchaVO) jedisCacheUtil.getCache(key);
		if(null == vo){
			vo = new CaptchaVO(value);
			setKaptchaCache(key,vo);
		}else{
			if(!StringUtil.equals(vo.getVerifycode(), value)){
				vo.setVerifycode(value);
				setKaptchaCache(key,vo);
			}
		}
		return vo;
	}
	
	public boolean auth(String name , String tel ,String value){
		if(StringUtil.isBlank(name) || StringUtil.isBlank(tel) || StringUtil.isBlank(value))throw new MobileException(MResultInfo.PARAM_ERROR);
		String key = new StringBuffer(name).append(tel).toString();
		CaptchaVO vo = getKaptchaCache(key);
		if(null != vo){
			if(StringUtil.equals(vo.getVerifycode(), value)) return true;
		}
		throw new MobileException("图形验证码输入不正确");
	}
}
