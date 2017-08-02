package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicChange;

public interface TopicChangeDao extends BaseDao<TopicChange> {

    Integer getUnprocessingChangeOrderCount(Long id);

    List<TopicChange> selectDynamicPageQueryWithLike(TopicChange topicChange);

    Long selectCountWithLike(TopicChange topicChange);

}
