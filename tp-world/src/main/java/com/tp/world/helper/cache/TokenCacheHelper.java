package com.tp.world.helper.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.redis.util.JedisCacheUtil;

/**
 * 缓存工具类
 * @author zhuss
 * @2016年1月3日 下午5:54:51
 */
@Service
public class TokenCacheHelper {

	private static final Logger log = LoggerFactory.getLogger(TokenCacheHelper.class);
	
	public static final int TOKEN_LIVE = 365*86400;//TOKEN存活时间 单位为秒

	
	public static final int TOKEN_LIVE_2 = 120;//TOKEN存活时间 单位为秒

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	/**
	 * 设置Token
	 * @param token
	 */
	public void setTokenCacheAppSingleAllInfo(String key , List<AppSingleAllInfoDTO> dtolist){
		boolean result =  jedisCacheUtil.setCache(key, dtolist, TOKEN_LIVE_2);
		if(!result){
			log.error("[缓存工具-设置Token 失败] = {}",MResultInfo.CACHE_SET_FAILED.message);
			throw new MobileException(MResultInfo.CACHE_SET_FAILED);
		}
	}
	
	public List<AppSingleAllInfoDTO> getTokenCacheAppSingleAllInfo(String key){
		return (List<AppSingleAllInfoDTO>) jedisCacheUtil.getCache(key);
	}


	
	/**
	 * 设置Token
	 * @param token
	 */
	public void setTokenCache(String key , TokenCacheTO token){
		boolean result =  jedisCacheUtil.setCache(key, token, TOKEN_LIVE);
		if(!result){
			log.error("[缓存工具-设置Token 失败] = {}",MResultInfo.CACHE_SET_FAILED.message);
			throw new MobileException(MResultInfo.CACHE_SET_FAILED);
		}
	}

	
	/**
	 * 获取token对象值
	 * @param key
	 * @return
	 */
	public TokenCacheTO getTokenCache(String key){
		return (TokenCacheTO) jedisCacheUtil.getCache(key);
	}
	
	/**
	 * 删除token
	 * @param key
	 */
	public void delToken(String key){
		long result =  jedisCacheUtil.deleteCacheKey(key);
		if(result < 1){
			log.error("[缓存工具-删除Token 失败] = {}",MResultInfo.CACHE_DEL_FAILED.message);
			throw new MobileException(MResultInfo.SYSTEM_ERROR);
		}
	}
}
