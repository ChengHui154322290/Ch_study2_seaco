package com.tp.proxy.dss;



import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.dss.PromoterTopic;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IPromoterTopicService;


/**
 * 分销主题商品上下架代理层
 * @author zhs
 *
 */
@Service
public class PromoterTopicProxy extends BaseProxy<PromoterTopic>{

	@Autowired
	private IPromoterTopicService promoterTopicService;
	
	@Override
	public IBaseService<PromoterTopic> getService() {
		return promoterTopicService;
	}
	
	

}
