/**
 * 
 */
package com.tp.service.cms.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dao.cms.SingleTepactivityDao;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.model.cms.SingleTepactivity;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.mmp.ITopicService;

/**
 * @author szy
 *
 */
@Service("promotionTopicChangeCallback")
public class CmsPromotionTopicChangeCallback implements MqMessageCallBack {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ITopicService topicService;
	
	@Autowired
	private SingleTepactivityDao singleTepactivityDao;
	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public boolean execute(Object o) {
		logger.info("start mq excute.......");
		Long topicId = (Long) o;
		logger.info("执行促销模块更新活动，更新cms数据:" + topicId);
		
		//首先查询该活动id是否存在cms的末班中，并且该模板是生效的，然后从促销那边查询数据去更新模板中的活动起止日期，用一条sql去执行
		TopicDetailDTO topicDo;
		try {
			topicDo = topicService.getTopicDetailById(null, topicId);
		} catch (Exception e1) {
			logger.info("执行查询促销模块的活动信息报错，活动id为:" + topicId);
			
			return true;
		}
		
		try {
			SingleTepactivity cmsSingleTepactivityDO = new SingleTepactivity();
			cmsSingleTepactivityDO.setActivityId(topicId);
			cmsSingleTepactivityDO.setStartdate(topicDo.getTopic().getStartTime());
			cmsSingleTepactivityDO.setEnddate(topicDo.getTopic().getEndTime());
			logger.info("执行更新cms数据，活动id为:" + topicId+
					";startdate:"+topicDo.getTopic().getStartTime()+";endtime:"+topicDo.getTopic().getEndTime());
			singleTepactivityDao.updateTopicChangeDynamic(cmsSingleTepactivityDO);
		} catch (Exception e) {
			logger.error("执行更新cms数据报错，活动id为:" + topicId+
					";startdate:"+topicDo.getTopic().getStartTime()+";endtime:"+topicDo.getTopic().getEndTime());
			return true;
		}
		return true;
	}

}
