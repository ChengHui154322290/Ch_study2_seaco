package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicItemChange;

public interface TopicItemChangeDao extends BaseDao<TopicItemChange> {

   List<TopicItemChange> getTopicItemChangeByIds(List<Long>ids);

}
