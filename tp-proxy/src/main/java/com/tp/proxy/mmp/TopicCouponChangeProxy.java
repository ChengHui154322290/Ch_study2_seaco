package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicCouponChange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicCouponChangeService;
/**
 * 活动指定优惠券变更单代理层
 * @author szy
 *
 */
@Service
public class TopicCouponChangeProxy extends BaseProxy<TopicCouponChange>{

	@Autowired
	private ITopicCouponChangeService topicCouponChangeService;

	@Override
	public IBaseService<TopicCouponChange> getService() {
		return topicCouponChangeService;
	}
}
