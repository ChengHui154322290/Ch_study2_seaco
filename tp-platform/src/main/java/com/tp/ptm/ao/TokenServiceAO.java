package com.tp.ptm.ao;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.service.ptm.IPlatformAccountService;

/**
 * <pre>
 * token 服务AO
 * </pre>
 * 
 * @author ZhuFuhua
 */
@Service
public class TokenServiceAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceAO.class);

    private static final long DEFULT_EFFECTIVE = 1000 * 60 * 60 * 24L;

    /** token数据map key：appkey value：token */
    private static Map<String, String> tokenMap = new HashMap<String, String>();

    /** token数据录入时间map key：appkey value：录入时间(毫秒级) System.currentTimeMillis() */
    private static Map<String, Long> tokenSetTime = new HashMap<String, Long>();

    @Autowired
    private IPlatformAccountService platformAccountService;

    private static ConcurrentHashMap<String, String> concurrentTokenMap = new ConcurrentHashMap<>();  
    public String getTokenByAppKey(String appKey){
    	String token = concurrentTokenMap.get(appKey);
    	if(StringUtils.isNotEmpty(token)) return token;
    	token = platformAccountService.selectTokenByAppkey(appKey);
    	if(StringUtils.isNotEmpty(token)){
    		concurrentTokenMap.put(appKey, token);
    	}
    	return token;
    }
    
    /**
     * <pre>
     * 获取token
     * </pre>
     * 
     * @param appKey
     * @return token
     */
    public String getToken(String appKey) {
        if (StringUtils.isNotBlank(appKey)) {
            Long tokenQueryTime = tokenSetTime.get(appKey);
            long now = System.currentTimeMillis();
            if (tokenQueryTime == null || (now > tokenQueryTime.longValue() + DEFULT_EFFECTIVE)) {
                String token = null;
                try {
                    token = platformAccountService.selectTokenByAppkey(appKey);
                } catch (Exception e) {
                    LOGGER.error("查询appkey对应token异常", e);
                    return null;
                }

                tokenMap.put(appKey, token);
                tokenSetTime.put(appKey, new Long(now));
            }
            return tokenMap.get(appKey);
        }
        return null;
    }

}
