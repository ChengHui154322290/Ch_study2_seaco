package com.tp.world.helper.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.redis.util.JedisCacheUtil;

/**
 * 活动缓存工具类
 * @author zhuss
 * @2016年1月3日 下午5:54:51
 */
@Service
public class ActivityCacheHelper {

	public static final String ACTIVITY_ZL_QRCODE_KEY = "xg_zhonglai_offline_qrcode";
	public static final String ACTIVITY_ZL_CODE_KEY = "xg_zhonglai_offline_code";
	public static final int ACTIVITY_ZL_LIVE = 3600*7;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	/**
	 * 设置ZLCodeCache
	 */
	public void setZLCodeCache(int code){
		jedisCacheUtil.setCache(ACTIVITY_ZL_CODE_KEY, code, ACTIVITY_ZL_LIVE);
	}
	
	/**
	 * 获取QrcodeCache对象值
	 * @return
	 */
	public Integer getZLCodeCache(){
		Integer code = (Integer) jedisCacheUtil.getCache(ACTIVITY_ZL_CODE_KEY);
		if(null == code) code = 1234;
		code  = code + 3;
		setZLCodeCache(code);
		return code;
	}
}
