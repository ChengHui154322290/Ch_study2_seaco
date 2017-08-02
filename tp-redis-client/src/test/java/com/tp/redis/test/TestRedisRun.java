package com.tp.redis.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.redis.util.JedisCacheUtil;
import com.tp.redis.util.JedisDBUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring.xml" })
public class TestRedisRun {
	
	private static final Log logger = LogFactory.getLog(TestRedisRun.class);

	@Autowired
	JedisCacheUtil jedisCacheUtil;

	@Autowired
	JedisDBUtil jedisDBUtil;

	@Test
	public void testAll() throws InterruptedException {

		for (int i = 0; i < 10; i++) {
			jedisCacheUtil.setCacheString("cache_key" + i, "中国国加另中dfdf", 3);
		}
		Thread.sleep(1000);
		for (int i = 0; i < 10; i++) {
			logger.info(jedisCacheUtil.getCacheString("cache_key" + i));
		}

		
		logger.info("===================start 15000=>" + System.currentTimeMillis() / 1000);
		Thread.sleep(15000);
		logger.info("===================end =>" + System.currentTimeMillis() / 1000);
		for (int i = 0; i < 10; i++) {
			jedisCacheUtil.setCacheString("cache_key" + i, "中国国加另中dfdf", 3);
		}
		Thread.sleep(1000);
		for (int i = 0; i < 10; i++) {
			logger.info(jedisCacheUtil.getCacheString("cache_key" + i));
		}
		
		
		logger.info("===================start  20000=>" + System.currentTimeMillis() / 1000);
		Thread.sleep(20000);
		logger.info("===================end =>" + System.currentTimeMillis() / 1000);
		for (int i = 0; i < 10; i++) {
			jedisCacheUtil.setCacheString("cache_key" + i, "中国国加另中dfdf", 3);
		}
		Thread.sleep(1000);
		for (int i = 0; i < 10; i++) {
			logger.info(jedisCacheUtil.getCacheString("cache_key" + i));
		}

	}

}
