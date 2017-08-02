package com.tp.dao.sch;


import java.util.List;
import java.util.Map;

import com.tp.model.sch.TopicSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface TopicSearchDao {

    List<TopicSearch> getAllAvailableTopics(Map<String,Object> param);
}
