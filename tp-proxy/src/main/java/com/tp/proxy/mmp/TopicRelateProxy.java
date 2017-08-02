/**
 * 
 */
package com.tp.proxy.mmp;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.model.mmp.Topic;
import com.tp.service.mmp.ITopicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 0.0.1
 */
@Service
public class TopicRelateProxy {

	@Autowired
	private ITopicService topicService;

	public PageInfo<Topic> getTopicInfoWithoutSpecialId(Long specialTopicId,String number, String name, Integer startSize, Integer pageSize) {

		return topicService.getTopicsWithoutSpecialId(specialTopicId, number,
				name, startSize, pageSize);
	}

	public Topic getTopicById(Long topicId) {
		return topicService.queryById(topicId);
	}

	public Topic getAuditedTopicById(Long topicId) {
		Topic Topic = topicService.queryById(topicId);
		if (null != Topic
				&& TopicConstant.TOPIC_STATUS_AUDITED == Topic.getStatus()
				&& DeletionStatus.NORMAL.ordinal() == Topic.getDeletion()) {
			return Topic;
		}
		return null;
	}
}
