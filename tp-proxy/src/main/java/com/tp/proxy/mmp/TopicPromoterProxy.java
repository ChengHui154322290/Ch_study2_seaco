package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicPromoter;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicPromoterService;
/**
 * 专题对应分销渠道代理层
 * @author szy
 *
 */
@Service
public class TopicPromoterProxy extends BaseProxy<TopicPromoter>{

	@Autowired
	private ITopicPromoterService topicPromoterService;

	@Override
	public IBaseService<TopicPromoter> getService() {
		return topicPromoterService;
	}
}
