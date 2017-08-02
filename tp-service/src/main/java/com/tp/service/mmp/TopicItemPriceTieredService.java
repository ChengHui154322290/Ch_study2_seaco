package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicItemPriceTieredDao;
import com.tp.model.mmp.TopicItemPriceTiered;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicItemPriceTieredService;

@Service
public class TopicItemPriceTieredService extends BaseService<TopicItemPriceTiered> implements ITopicItemPriceTieredService {

	@Autowired
	private TopicItemPriceTieredDao topicItemPriceTieredDao;
	
	@Override
	public BaseDao<TopicItemPriceTiered> getDao() {
		return topicItemPriceTieredDao;
	}

}
