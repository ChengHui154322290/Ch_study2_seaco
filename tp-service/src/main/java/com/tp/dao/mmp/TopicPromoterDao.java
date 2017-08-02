package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicPromoter;

public interface TopicPromoterDao extends BaseDao<TopicPromoter> {

	void batchInsert(List<TopicPromoter> topicPromoterList);
	
	List<TopicPromoter> queryListByTopicId(Long topicId);
}
