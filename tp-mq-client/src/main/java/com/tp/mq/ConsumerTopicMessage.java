package com.tp.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.tp.mq.domain.MqConstants;
import com.tp.mq.domain.MqMessageConfigs;
import com.tp.mq.util.MqConsumerConnectionSingleton;
import com.tp.mq.util.MqNameUtil;
import com.tp.mq.util.MqSerializeUtil;

/**
 * (pub_sub)主题订阅消息 接收对象
 * 
 * @author szy
 *
 */
public class ConsumerTopicMessage implements Runnable {

	private static Log logger;

	private volatile MqMessageCallBack messageCallBack;

	private volatile String queueName;

	private volatile MqMessageConfigs messageConfigs;

	private volatile boolean stopRequested = false;

	public void run() {
		boolean autoAck = false;
		Channel channel = null;
		try {
			while (!stopRequested) {
				channel = MqConsumerConnectionSingleton.connection().createChannel();
				String exchangeName = MqNameUtil.getExchangeName(getQueueName());
				channel.exchangeDeclare(exchangeName, MqConstants.EXCHANGE_TYPE_FANOUT, true);
				
				String queueName = channel.queueDeclare().getQueue();
				channel.queueBind(queueName, exchangeName, "");
				channel.basicQos(1);
				
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(queueName, autoAck, consumer);
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				if (null != delivery) {
					byte[] bytes = delivery.getBody();
					Object o = MqSerializeUtil.unserialize(bytes);
					messageCallBack.execute(o);
					if (!autoAck) {
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					}
				}
			}
		} catch (Exception e) {
			logger.error("run() exception for " + e.getMessage(),e);
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (Exception ex) {
				logger.error("channel close exception for " + ex.getMessage(),ex);
			}
		}
	}

	public void stopRequest() {
		stopRequested = true;
	}

	public MqMessageCallBack getMessageCallBack() {
		return messageCallBack;
	}

	public void setMessageCallBack(MqMessageCallBack messageCallBack) {
		this.messageCallBack = messageCallBack;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
		logger = LogFactory.getLog(ConsumerTopicMessage.class + "queueName=>" + queueName);

	}

	public MqMessageConfigs getMessageConfigs() {
		return messageConfigs;
	}

	public void setMessageConfigs(MqMessageConfigs messageConfigs) {
		this.messageConfigs = messageConfigs;
	}

}
