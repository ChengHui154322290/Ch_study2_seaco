package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicPromoterChange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicPromoterChangeService;
/**
 * 专题对应分销渠道代理层
 * @author szy
 *
 */
@Service
public class TopicPromoterChangeProxy extends BaseProxy<TopicPromoterChange>{

	@Autowired
	private ITopicPromoterChangeService topicPromoterChangeService;

	@Override
	public IBaseService<TopicPromoterChange> getService() {
		return topicPromoterChangeService;
	}
}
