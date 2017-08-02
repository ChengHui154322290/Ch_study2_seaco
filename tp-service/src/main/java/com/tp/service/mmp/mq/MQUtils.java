/**
 * 
 */
package com.tp.service.mmp.mq;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;

/**
 *
 */
@Service(value = "mqUtils")
public class MQUtils {

	@Autowired
	private RabbitMqProducer rabbitMqProducer;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public boolean sendMqP2pMessage(String mqQueueName, Object obj) {

		if (obj == null) {
			return false;
		}
		try {
			logger.info("[sendMqP2pMessage]send mq message start.....");
			rabbitMqProducer.sendP2PMessage(mqQueueName, obj);
			logger.info("[sendMqP2pMessage]send mq message end.....");
			return true;
		} catch (MqClientException e) {
			logger.error("error",e);
			return false;
		}
	}
}
