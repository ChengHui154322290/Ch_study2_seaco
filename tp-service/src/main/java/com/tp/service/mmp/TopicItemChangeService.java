package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicItemChangeDao;
import com.tp.model.mmp.TopicItemChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicItemChangeService;

@Service
public class TopicItemChangeService extends BaseService<TopicItemChange> implements ITopicItemChangeService {

	@Autowired
	private TopicItemChangeDao topicItemChangeDao;

	@Override
	public BaseDao<TopicItemChange> getDao() {
		return topicItemChangeDao;
	}


}
