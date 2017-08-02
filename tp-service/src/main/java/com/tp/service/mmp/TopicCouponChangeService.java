package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicCouponChangeDao;
import com.tp.model.mmp.TopicCouponChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicCouponChangeService;

@Service
public class TopicCouponChangeService extends BaseService<TopicCouponChange> implements ITopicCouponChangeService {

	@Autowired
	private TopicCouponChangeDao topicCouponChangeDao;
	
	@Override
	public BaseDao<TopicCouponChange> getDao() {
		return topicCouponChangeDao;
	}


}
