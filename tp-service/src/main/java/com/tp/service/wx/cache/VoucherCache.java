package com.tp.service.wx.cache;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.wx.manager.VoucherManager;
import com.tp.util.StringUtil;
/**
 * 凭证缓存管理器
 * @author zhuss
 * @2016年4月27日 下午5:01:37
 */
@Service
public class VoucherCache {
	private static final Logger log = LoggerFactory.getLogger(VoucherCache.class);
	
	public static final String ACCESS_TOKEN_KEY = "xg_access_token";
	public static final int ACCESS_TOKEN_LIVE = 3600;//ACCESS_TOKEN存活时间 单位为秒
	
	public static final String JSAPI_TICKET_KEY = "xg_jsapi_ticket";
	public static final int JSAPI_TICKET_LIVE = 3600;//JSAPI_TICKET存活时间 单位为秒
	
	/*public static final String ACCESS_TOKEN_TEST_KEY = "xg_test_access_token";
	public static final int ACCESS_TOKEN_TEST_LIVE = 3600;//ACCESS_TOKEN存活时间 单位为秒
*/	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	Properties settings;
	
	/**
	 * 设置AccessToken
	 * @param token
	 */
	public void setAccessTokenCache(String accessToken){
		jedisCacheUtil.setCache(ACCESS_TOKEN_KEY, accessToken, ACCESS_TOKEN_LIVE);
	}
	
	/**
	 * 获取AccessToken对象值
	 * @param key
	 * @return
	 */
	public String getAccessTokenCache(){
		String accessToken = (String) jedisCacheUtil.getCache(ACCESS_TOKEN_KEY);
		if(StringUtil.isBlank(accessToken)){
			accessToken = VoucherManager.getAccessToken(settings.getProperty("WX_APPID"),settings.getProperty("WX_APPSECRET"));
			setAccessTokenCache(accessToken);
		}
		log.info("[获取微信凭证ACCESSTOKEN = {}]",accessToken);
		return accessToken;
	}
//	public String getAccessTokenCacheNew(int choise){
////		String accessToken = (String) jedisCacheUtil.getCache(ACCESS_TOKEN_KEY);
////		if(StringUtil.isBlank(accessToken)){
//		String accessToken = null;
//			if(1==choise){
//				accessToken = VoucherManager.getAccessToken(settings.getProperty("WX_APPID_XGM"),settings.getProperty("WX_APPSECRET_XGM"));
//			}else{
//				accessToken = VoucherManager.getAccessToken(settings.getProperty("WX_APPID"),settings.getProperty("WX_APPSECRET"));
//			}
////			setAccessTokenCache(accessToken);
////		}
//		log.info("[获取微信凭证ACCESSTOKEN = {}]",accessToken);
//		return accessToken;
//	}
	
	/**
	 * 设置Ticket
	 * @param token
	 */
	public void setTicketCache(String ticket){
		jedisCacheUtil.setCache(JSAPI_TICKET_KEY, ticket, JSAPI_TICKET_LIVE);
	}
	
	/**
	 * 获取Ticket对象值
	 * @param key
	 * @return
	 */
	public String getTicketCache(){
		String ticket = (String) jedisCacheUtil.getCache(JSAPI_TICKET_KEY);
		if(StringUtil.isBlank(ticket)){
			ticket = VoucherManager.getTicket(getAccessTokenCache());
			setTicketCache(ticket);
		}
		return ticket;
	}
//	public String getTicketCacheNew(Integer choise){
//		String ticket = (String) jedisCacheUtil.getCache(JSAPI_TICKET_KEY);
//		if(StringUtil.isBlank(ticket)){
//			ticket = VoucherManager.getTicket(getAccessTokenCacheNew(choise));
//			setTicketCache(ticket);
//		}
//		return ticket;
//	}
}
