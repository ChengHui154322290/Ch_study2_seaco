package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.TopicLimitItemDao;
import com.tp.model.ord.TopicLimitItem;
import com.tp.service.BaseService;
import com.tp.service.ord.ITopicLimitItemService;

@Service
public class TopicLimitItemService extends BaseService<TopicLimitItem> implements ITopicLimitItemService {

	@Autowired
	private TopicLimitItemDao topicLimitItemDao;
	
	@Override
	public BaseDao<TopicLimitItem> getDao() {
		return topicLimitItemDao;
	}
	/**
	 * 
	 * <pre>
	 * 更新促销限购商品统计数量
	 * </pre>
	 * 
	 * @param TopicLimitItem
	 * @return
	 */
	@Override
	public Integer updateTopicLimitItemQuantity(TopicLimitItem topicLimitItem){
		return  topicLimitItemDao.updateTopicLimitItemQuantity(topicLimitItem);
	}
}
