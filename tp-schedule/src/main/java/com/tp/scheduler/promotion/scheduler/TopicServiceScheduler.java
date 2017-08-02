package com.tp.scheduler.promotion.scheduler;

import com.alibaba.fastjson.JSON;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.service.mmp.ITopicService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public class TopicServiceScheduler {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ITopicService topicService;


	public ResultInfo scanTopicStatus() {
		try {
			logger.info("start status syn"
					+ ".chronize by scheduler........................");
			Date now = new Date();
			Date afterTime = new Date(now.getTime() + 240000);
			ResultInfo result = topicService.scanTopicStatus(afterTime);
			logger.info("TOPIC_SCAN_RESULT:"+ JSON.toJSONString(result));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e));
		}
	}

	public ResultInfo scanShortTopicStatus() {
		try {
			logger.info("start status synchronize by scheduler........................");
			Date now = new Date();
			Date afterTime = new Date(now.getTime() + 5000);
			ResultInfo result = topicService.scanTopicStatus(afterTime);
			logger.info("TOPIC_SCAN_RESULT:"+JSON.toJSONString(result));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e));
		}
	}
}
