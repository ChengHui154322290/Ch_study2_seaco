package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicCoupon;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicCouponService;
/**
 * 活动指定优惠券代理层
 * @author szy
 *
 */
@Service
public class TopicCouponProxy extends BaseProxy<TopicCoupon>{

	@Autowired
	private ITopicCouponService topicCouponService;

	@Override
	public IBaseService<TopicCoupon> getService() {
		return topicCouponService;
	}
}
