package com.tp.service.ord.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.model.ord.SubOrder;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

/**
 * 海淘订单推送仓库 
 */
@Service
public class SeaorderStockoutConsumer implements MqMessageCallBack{

	private static final Logger logger = LoggerFactory.getLogger(SeaorderStockoutConsumer.class);

	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;

	@Override
	public boolean execute(Object subOrder) {
		if (subOrder != null && subOrder instanceof SubOrder) {
			logger.debug("从MQ中获取的发货实体数据为：{}", JSONObject.toJSON(subOrder));
			salesOrderRemoteService.putWareHouseShippingBySeaSubOrder((SubOrder)subOrder);
		} else {
			logger.error("清关通过后订单推送仓库操作时获取的请求实体为空或者不为com.tp.model.ord.SubOrder类型");
		}
		return Boolean.TRUE;
	}
}
