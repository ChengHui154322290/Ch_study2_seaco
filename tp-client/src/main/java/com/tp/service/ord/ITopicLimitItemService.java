package com.tp.service.ord;

import com.tp.model.ord.TopicLimitItem;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 活动限购订单商品统计表接口
  */
public interface ITopicLimitItemService extends IBaseService<TopicLimitItem>{

	Integer updateTopicLimitItemQuantity(TopicLimitItem topicLimitItem);

}
