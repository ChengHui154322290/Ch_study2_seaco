package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.TopicLimitItem;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ITopicLimitItemService;
/**
 * 活动限购订单商品统计表代理层
 * @author szy
 *
 */
@Service
public class TopicLimitItemProxy extends BaseProxy<TopicLimitItem>{

	@Autowired
	private ITopicLimitItemService topicLimitItemService;

	@Override
	public IBaseService<TopicLimitItem> getService() {
		return topicLimitItemService;
	}
}
