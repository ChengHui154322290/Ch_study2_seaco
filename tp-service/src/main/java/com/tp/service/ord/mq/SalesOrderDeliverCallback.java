/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.model.ord.OrderDelivery;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

/**
 * <pre>
 * 发货后的订单操作MQ启动
 * </pre>
 * 
 * @author szy
 * @time 2016-01-02 下午7:42:02
 */
@Service
public class SalesOrderDeliverCallback implements MqMessageCallBack {
	private static final Logger logger = LoggerFactory.getLogger(SalesOrderDeliverCallback.class);

	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;

	@Override
	public boolean execute(Object orderDeliver) {
		if (orderDeliver != null && orderDeliver instanceof OrderDelivery) {
			if (logger.isDebugEnabled()) {
				logger.debug("从MQ中获取的发货实体数据为：{}", JSONObject.toJSON(orderDeliver));
			}
			salesOrderRemoteService.operateOrderForDeliver((OrderDelivery)orderDeliver);
		} else {
			logger.error("发货后订单操作时获取的请求实体为空或者不为com.tp.model.ord.OrderDelivery类型");
		}
		return Boolean.TRUE;
	}

}
