package com.tp.test.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.m.helper.cache.SMSCacheHelper;
import com.tp.m.helper.cache.TokenCacheHelper;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.test.base.BaseTest;

public class CacheTest extends BaseTest{
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private SMSCacheHelper sMSCacheHelper;
	
	@Autowired
	private TokenCacheHelper tokenCacheHelper;
	
	@Test
	public void getCache(){
		System.out.println(jedisCacheUtil.getCache("15800917996:app_receive_coupon_sms_code"));
	}
	
	@Test
	public void getSMSCache(){
		System.out.println(sMSCacheHelper.getSMSCache("15800917996:app_receive_coupon_sms_code"));
	}
	
	@Test
	public void setSMSCache(){
		sMSCacheHelper.setSMSCache("15800917991:app_receive_coupon_sms_code","123456");
	}
}
