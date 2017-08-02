package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicInventoryAccBook;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicInventoryAccBookService;
/**
 * 库存交互流水账代理层
 * @author szy
 *
 */
@Service
public class TopicInventoryAccBookProxy extends BaseProxy<TopicInventoryAccBook>{

	@Autowired
	private ITopicInventoryAccBookService topicInventoryAccBookService;

	@Override
	public IBaseService<TopicInventoryAccBook> getService() {
		return topicInventoryAccBookService;
	}
}
