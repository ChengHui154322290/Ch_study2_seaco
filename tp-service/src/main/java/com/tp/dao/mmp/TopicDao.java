package com.tp.dao.mmp;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.mmp.TopicQueryDTO;
import com.tp.model.mmp.Topic;

public interface TopicDao extends BaseDao<Topic> {

    Integer getMaxTopicSortIndex();

    List<Long> getAvailablePCAndWACTopicId();

    List<Topic> queryTopicList(Map<String, Object> jp);

    Long countTopicList(Map<String, Object> jp);

    List<Long> getTopicIdList(Map<String, Object> param);

    List<Topic> queryTopicInfoList(List<Long> ids);

    List<Topic> selectDynamicPageQueryWithLike(TopicQueryDTO topic);

    Long getTopicsWithoutSpecialIdCount(Map<String, Object> param);

    List<Topic> getTopicsWithoutSpecialId(Map<String, Object> param);

    Long selectCountWithLike(TopicQueryDTO topic);

    List<Topic> queryTopicListCms(Map<String, Object> param);

    Integer countTopicListCms(Map<String, Object> param);

    List<Topic> queryTopicInList(List<Long> ids);

    List<Topic> getTomorrowForecast(Map<String, Object> param);

    Integer countTomorrowForecast(Map<String, Object> param);

    List<Topic> getTodayTopic(Map<String, Object> param);

    Integer countTodayTopic(Map<String, Object> param);

    List<Topic> getTodaySingleTopic(Map<String, Object> param);

    Integer countTodaySingleTopic(Map<String, Object> param);

    List<Topic> getTodayAllTopic(Map<String, Object> param);

    Integer countTodayAllTopic(Map<String, Object> param);

    List<Topic> getTodayLastRash(Map<String, Object> param);

    Integer countTodayLastRash(Map<String, Object> param);

    List<Topic> queryLastestSingleTopic(Integer size);

    List<Long> getScanTopicIds(Map<String, Object> param);

   Long getSingleAvailableTopicIdByIds(Map<String, Object> param);

    List<Topic> getBusAvailableTopicList();

    List<Topic> queryTopicForGroupbuy(Map<String,Object> param);

    Integer queryTopicForGroupbuyCount(Map<String,Object> param);

    List<Topic> queryTopicForGroupbuyAPP(Map<String,Object> param);

    Integer queryTopicForGroupbuyAPPCount(Map<String,Object> param);

    List<Long> queryTopicIdsForGroupbuyAPP (Map<String,Object> param);

}
