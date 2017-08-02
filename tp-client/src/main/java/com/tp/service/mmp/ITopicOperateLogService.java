package com.tp.service.mmp;

import com.tp.model.mmp.*;
import com.tp.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
  * @author szy 
  * 接口
  */
public interface ITopicOperateLogService extends IBaseService<TopicOperateLog>{

    void saveTopicOperateDetailLog(Map<TopicItem, TopicItemChange> meta, Topic topicNEW, Topic topicOLD, PolicyInfo policyNEW, PolicyInfo policyORD, Long userId, String userName);

    void insertBatch(List<TopicOperateLog> topicOperateLogList);

}
