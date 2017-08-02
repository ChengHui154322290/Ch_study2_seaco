package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicPromoterDao;
import com.tp.model.mmp.TopicPromoter;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicPromoterService;

@Service
public class TopicPromoterService extends BaseService<TopicPromoter> implements ITopicPromoterService {

	@Autowired
	private TopicPromoterDao topicPromoterDao;
	
	@Override
	public BaseDao<TopicPromoter> getDao() {
		return topicPromoterDao;
	}

}
