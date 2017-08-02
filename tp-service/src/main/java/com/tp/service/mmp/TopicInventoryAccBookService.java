package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicInventoryAccBookDao;
import com.tp.model.mmp.TopicInventoryAccBook;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicInventoryAccBookService;

@Service
public class TopicInventoryAccBookService extends BaseService<TopicInventoryAccBook> implements ITopicInventoryAccBookService {

	@Autowired
	private TopicInventoryAccBookDao topicInventoryAccBookDao;

	@Override
	public BaseDao<TopicInventoryAccBook> getDao() {
		return topicInventoryAccBookDao;
	}



}
