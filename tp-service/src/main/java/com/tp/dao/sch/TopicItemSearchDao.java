package com.tp.dao.sch;

import java.util.List;
import java.util.Map;

import com.tp.model.sch.TopicItemSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface TopicItemSearchDao {

    Integer getAllAvailableTopicItemsCount(List<Long> topicIds);

    List<TopicItemSearch> getAllAvailableTopicItem(Map<String,Object> param);

}
