package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicPromoterChangeDao;
import com.tp.model.mmp.TopicPromoterChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicPromoterChangeService;

@Service
public class TopicPromoterChangeService extends BaseService<TopicPromoterChange> implements ITopicPromoterChangeService {

	@Autowired
	private TopicPromoterChangeDao topicPromoterChangeDao;
	
	@Override
	public BaseDao<TopicPromoterChange> getDao() {
		return topicPromoterChangeDao;
	}

}
