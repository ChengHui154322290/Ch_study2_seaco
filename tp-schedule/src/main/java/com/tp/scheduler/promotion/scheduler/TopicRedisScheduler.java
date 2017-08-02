/**
 * 
 */
package com.tp.scheduler.promotion.scheduler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tp.service.mmp.ITopicRedisService;

@Component
public class TopicRedisScheduler {

	@Autowired
	private ITopicRedisService topicRedisService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void initRedis() {
		try {
			logger.info("start redis synchronize by scheduler........................");
			topicRedisService.initRedis();
			logger.info("end redis synchronize by scheduler........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			closeRedis();
		}
	}



	public void closeRedis() {
		try {
			topicRedisService.closeRedis();
			logger.info("release redis lock........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void closeLastRashRedis() {
		try {
			topicRedisService.closeLastRashRedis();
			logger.info("release last redis lock........................");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
