/**
 * 
 */
package com.tp.service.mmp.mq;

import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.mmp.ITopicItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "topicItemUpdateListenerCallback")
public class TopicItemUpdateListenerCallback implements MqMessageCallBack {

	@Autowired
	private ITopicItemService topicItemService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Object o) {
		try {
			logger.info("start mq excute.......");
			List<PromotionItemMqDto> itemMqDtos = (List<PromotionItemMqDto>) o;
			topicItemService.updateTopicItemByUpdatedItemInfo(itemMqDtos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

}
