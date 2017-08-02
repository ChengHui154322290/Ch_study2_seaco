package com.tp.m.helper;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.BaseQuery;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.cache.TokenCacheHelper;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.util.MD5Util;
import com.tp.util.Sha1Util;

/**
 * 认证工具类
 * @author zhuss
 * @2016年1月3日 下午6:02:53
 */
@Service
public class AuthHelper {
	
	private static final Logger log = LoggerFactory.getLogger(AuthHelper.class);
	
	public static final String APP_SIGN = "XG_APP";
	
	public static final String WX_TOKEN = "xgtoken";
	
	public static final Map<Long,Integer> MEMBER_ACCESS_MAP = new CacheMap<Long,Integer>(1000*60*30);
	@Autowired
	private TokenCacheHelper cacheHelper;
	@Autowired
	private MemberInfoProxy memberInfoProxy;

	/**
	 * 验证Token
	 * @return
	 */
	public TokenCacheTO authToken(String tokenKey){
		if(StringUtils.isBlank(tokenKey)) {
			log.error("[验证Token error] = {}",MResultInfo.TOKEN_NULL.message);
			throw new MobileException(MResultInfo.ACCOUNT_TIMEOUT);	
		}
		TokenCacheTO token = cacheHelper.getTokenCache(tokenKey);
		if(null == token){
			log.error("[验证Token error] = {}",MResultInfo.TOKEN_NO_EXIST.message);
			throw new MobileException(MResultInfo.ACCOUNT_TIMEOUT);	
		}
		if(null == token.getUid()){
			log.error("[验证Token error] = {}",MResultInfo.TOKEN_NO_USER.message);
			throw new MobileException(MResultInfo.ACCOUNT_TIMEOUT);	
		}
		Integer count = MEMBER_ACCESS_MAP.get(token.getUid());
		if(count==null){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("id", token.getUid());
			param.put("state", Boolean.TRUE);
			count = memberInfoProxy.queryByParamCount(param).getData();
			MEMBER_ACCESS_MAP.put(token.getUid(), count);
		}
		if(count==0){
			log.error("[验证Token error] = {}",MResultInfo.USER_AUTH_NULL.message);
			throw new MobileException(MResultInfo.USER_AUTH_NULL);	
		}
		return token;
	}
	
	/**
	 * 验证签名
	 * @param base
	 */
	public static void authSignature(BaseQuery base){
		if(StringUtil.isBlank(base.getSignature()))throw new MobileException(MResultInfo.SIGNATURE_ERROR);
		//APP签名规则：apptype+curtime+固定字符串(XG_APP) [MD5加密,不包括+]
		if(RequestHelper.isAPP(base.getApptype())){
			String appSign = base.getApptype()+base.getCurtime()+APP_SIGN;
			if(!StringUtil.equals(MD5Util.encrypt(appSign), base.getSignature()))throw new MobileException(MResultInfo.SIGNATURE_ERROR);
		}else{ //WAP签名规则：apptype+appversion [MD5加密,不包括+]
			String wapSign = base.getApptype() + base.getAppversion();
			if(!StringUtil.equals(MD5Util.encrypt(wapSign), base.getSignature()))throw new MobileException(MResultInfo.SIGNATURE_ERROR);
		}
	}
	
	/**
	 * 拦截器验证签名
	 * @param request
	 * @return
	 */
	public static void authSign(HttpServletRequest request){
		String method = request.getMethod();
		String appType = "";
		String signature  = "";
		String curtime = "";
		String appVersion = "";
		if(StringUtil.equals(method, "get")){
			appType = request.getParameter("apptype");
			signature  = request.getParameter("signature");
			curtime  = request.getParameter("curtime");
			appVersion  = request.getParameter("appversion");
		}else{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			request.setAttribute(RequestHelper.POST_PAREMENTS_JSON_KEY, jsonStr);
			appType = (String) JsonUtil.getJsonObjectByKey("apptype", jsonStr);
			signature  = (String) JsonUtil.getJsonObjectByKey("signature", jsonStr);
			curtime  = (String) JsonUtil.getJsonObjectByKey("curtime", jsonStr);
			appVersion  = (String) JsonUtil.getJsonObjectByKey("appversion", jsonStr);
		}
		String appSign = appType+curtime+"XG_APP";
		if (StringUtil.equals(appType, PlatformEnum.IOS.name()) || StringUtil.equals(appType, PlatformEnum.ANDROID.name())){
			checkSignature(appSign,signature,MResultInfo.SIGNATURE_ERROR);
		}
	}
	
	public static void checkSignature(String appSign,String signature,MResultInfo errInfo){
		if(!StringUtil.equals(MD5Util.encrypt(appSign), signature))throw new MobileException(errInfo);
	}
	
	/**
	 * 验证微信入口签名
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return StringUtil.equals(signature,signature(timestamp,nonce));
	}
	
	/**
	 * 调用微信API凭证签名:sha1加密
	 * @return
	 */
	public static String signature(String timestamp, String nonce){
		String[] arr = new String[] {WX_TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		return Sha1Util.sha1LowerData(content.toString());
	}

	public static void main(String[] args) {
		System.out.println("xbscd".indexOf("x"));
	}
	
	
   static class CacheMap<K, V> extends AbstractMap<K, V> {
	   private static final long DEFAULT_TIMEOUT = 30000;
	   private static CacheMap<Object, Object> defaultInstance;

	   public static synchronized final CacheMap<Object, Object> getDefault() {
	       if (defaultInstance == null) {
	           defaultInstance = new CacheMap<Object, Object>(DEFAULT_TIMEOUT);
	       }
	       return defaultInstance;
	   }
	   private class CacheEntry implements Entry<K, V> {
	       long time;
	       V value;
	       K key;
	       CacheEntry(K key, V value) {
	           super();
	           this.value = value;
	           this.key = key;
	           this.time = System.currentTimeMillis();
	       }
	       @Override
	       public K getKey() {
	           return key;
	       }
	       @Override
	       public V getValue() {
	           return value;
	       }
	       @Override
	       public V setValue(V value) {
	           return this.value = value;
	       }
	   }
	   private class ClearThread extends Thread {
	       ClearThread() {
	           setName("clear cache thread");
	       }
	       public void run() {
	           while (true) {
	               try {
	                   long now = System.currentTimeMillis();
	                   Object[] keys = map.keySet().toArray();
	                   for (Object key : keys) {
	                       CacheEntry entry = map.get(key);
	                       if (now - entry.time >= cacheTimeout) {
	                           synchronized (map) {
	                               map.remove(key);
	                               log.error("缓存KEY:"+key+"已移除！");
	                           }
	                       }
	                   }
	                   Thread.sleep(10000);
	               } catch (Exception e) {
	                   e.printStackTrace();
	               }
	           }
	       }
	   }
	   
	   private long cacheTimeout;
	   private Map<K, CacheEntry> map = new HashMap<K, CacheEntry>();
	   public CacheMap(long timeout) {
	       this.cacheTimeout = timeout;
	       new ClearThread().start();
	   }
	   @Override
	   public synchronized Set<Entry<K, V>> entrySet() {
	       Set<Entry<K, V>> entrySet = new HashSet<Map.Entry<K, V>>();
	       Set<Entry<K, CacheEntry>> wrapEntrySet = map.entrySet();
	       for (Entry<K, CacheEntry> entry : wrapEntrySet) {
	           entrySet.add(entry.getValue());
	       }
	       return entrySet;
	   }
	   @Override
	   public synchronized V get(Object key) {
	       CacheEntry entry = map.get(key);
	       return entry == null ? null : entry.value;
	   }
	   @Override
	   public synchronized V put(K key, V value) {
	       CacheEntry entry = new CacheEntry(key, value);
	       synchronized (map) {
	           map.put(key, entry);
	           log.error("KEY:"+key+"value:"+value+"----------创建完成");
	       }
	       return value;
	   }
	   @Override
	   public synchronized V remove(Object key) {
		   CacheEntry entry = map.get(key);
		   return (V) map.remove(key);
	    }
	}
}
			 