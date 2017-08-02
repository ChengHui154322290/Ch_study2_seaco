package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.TopicLimitItem;

public interface TopicLimitItemDao extends BaseDao<TopicLimitItem> {

	Integer updateTopicLimitItemQuantity(TopicLimitItem topicLimitItem);
	
	Long saveLimit(TopicLimitItem topicLimitItem);

	void batchInsert(@Param("topicLimitItemList") List<TopicLimitItem> topicLimitItemList);

}
