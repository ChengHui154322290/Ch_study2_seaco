package com.tp.scheduler.cms.scheduler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.service.cms.redis.ICmsRedisService;

/**
 * @author szy
 * @desc 此类是cms的redis全量更新操作，只能调其他接口，不能被别人所调用
 */
@Component
public class CmsRedisScheduler {

	@Autowired
	private ICmsRedisService cmsRedisService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 服务刚启动以及每天会执行一次全量的更新
	 */
	public void initAdvanceRedis() {
		try {
			logger.info("start cms redis synchronize by scheduler........................");
			cmsRedisService.initRedis();
			logger.info("end cms redis synchronize by scheduler........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			closeAdvanceRedis();
		}
	}

	public void closeAdvanceRedis() {
		try {
			cmsRedisService.closeRedis();
			logger.info("release cms redis lock........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 每5分钟去检查，判断缓存中是否有值，如果没有则全量更新，可能缓存服务器重启或者缓存数据失效;有值则不需要。
	 */
	public void checkAdvanceRedis() {
		try {
			logger.info("start cms(check,5分钟) redis synchronize by scheduler........................");
			cmsRedisService.checkAdvanceRedis();
			logger.info("end cms(check,5分钟) redis synchronize by scheduler........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			closeAdvanceRedis();
		}
	}

	public void closeCheckAdvanceRedis() {
		try {
			cmsRedisService.closeCheckAdvanceRedis();
			logger.info("release cms(check,5分钟) redis lock........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
