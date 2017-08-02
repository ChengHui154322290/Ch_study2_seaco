package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicPromoterChange;

public interface TopicPromoterChangeDao extends BaseDao<TopicPromoterChange> {

	void batchInsert(List<TopicPromoterChange> topicPromoterList);
	
	List<TopicPromoterChange> queryListByTopicChangeId(Long topicChangeId);
}
