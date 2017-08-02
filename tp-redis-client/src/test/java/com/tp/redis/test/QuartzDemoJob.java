package com.tp.redis.test;

import com.tp.redis.util.JedisCacheUtil;

public class QuartzDemoJob {

	private static final String RUN_WORK_KEY = "runWork";//每个定时任务一个key

	JedisCacheUtil jedisCacheUtil;

	public void runWork() {
		boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
		try {
			if (lock) {
				// 业务处理
			}
		} catch (Exception e) {

		} finally {
			if (lock) {
				jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			}
		}

	}

	public JedisCacheUtil getJedisCacheUtil() {
		return jedisCacheUtil;
	}

	public void setJedisCacheUtil(JedisCacheUtil jedisCacheUtil) {
		this.jedisCacheUtil = jedisCacheUtil;
	}

	public void close() {
		jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
	}

	// spring中bean配置 <bean id="quartzJob"
	// class="com.tp.redis.test.QuartzJob" destroy-method="close"></bean>
}
