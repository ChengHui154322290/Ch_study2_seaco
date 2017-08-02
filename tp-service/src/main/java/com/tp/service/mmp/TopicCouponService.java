package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicCouponDao;
import com.tp.model.mmp.TopicCoupon;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicCouponService;

@Service
public class TopicCouponService extends BaseService<TopicCoupon> implements ITopicCouponService {

	@Autowired
	private TopicCouponDao topicCouponDao;

	@Override
	public BaseDao<TopicCoupon> getDao() {
		return topicCouponDao;
	}

}
