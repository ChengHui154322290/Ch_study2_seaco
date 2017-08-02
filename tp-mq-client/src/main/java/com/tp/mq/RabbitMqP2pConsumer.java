package com.tp.mq;

import java.io.IOException;

import com.rabbitmq.client.Channel;

public class RabbitMqP2pConsumer {

//	private static final Log logger = LogFactory.getLog(RabbitMqP2pConsumer.class);

//	private MqMessageConfigs messageConfigs;
//
//	public MqMessageConfigs getMessageConfigs() {
//		return messageConfigs;
//	}
//
//	public void setMessageConfigs(MqMessageConfigs messageConfigs) {
//		this.messageConfigs = messageConfigs;
//	}

	public Channel getP2pChannel(String queueName) throws IOException {
		// if (this.getMessageConfigs().existsP2p()) {
		// logger.error("sendP2PMessage 队列名配置重复");
		// }
		// Channel channel =
		// MqConsumerConnectionSingleton.connection().createChannel();
		//
		// MqP2pConfig config = messageConfigs.getQueueConfigDO(queueName);
		// if (null != config &&
		// !config.getSendMessageType().equals(MqConstants.SEND_MESSAGE_TYPE_P2P))
		// {
		// logger.error("queue =>" + queueName + " type is:" +
		// config.getSendMessageType() + "");
		// return null;
		// }
		//
		// String exchangeName = MqNameUtil.getExchangeName(queueName);
		// String newQueueName = MqNameUtil.getQueueName(queueName);
		// String routingKey = MqNameUtil.getRoutingKey(queueName);
		//
		// boolean durable = config.isQueueDurable();
		//
		// channel.exchangeDeclare(exchangeName,
		// MqConstants.EXCHANGE_TYPE_DIRECT, true);
		// channel.queueDeclare(newQueueName, durable, false,
		// false,MqQueueHaPolicySingleton.getInstance().getQueueArgs());
		// channel.queueBind(newQueueName, exchangeName, routingKey);
		//
		// return channel;

		return null;
	}

}
