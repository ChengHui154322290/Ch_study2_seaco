package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TopicItemPriceTiered;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicItemPriceTieredService;
/**
 * 商品阶梯价信息代理层
 * @author szy
 *
 */
@Service
public class TopicItemPriceTieredProxy extends BaseProxy<TopicItemPriceTiered>{

	@Autowired
	private ITopicItemPriceTieredService topicItemPriceTieredService;

	@Override
	public IBaseService<TopicItemPriceTiered> getService() {
		return topicItemPriceTieredService;
	}
}
