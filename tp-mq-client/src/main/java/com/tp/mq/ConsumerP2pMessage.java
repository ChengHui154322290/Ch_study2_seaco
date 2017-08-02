package com.tp.mq;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.tp.mq.domain.MqConstants;
import com.tp.mq.domain.MqMessageConfigs;
import com.tp.mq.domain.MqP2pConfig;
import com.tp.mq.exception.MqClientException;
import com.tp.mq.util.MqConsumerConnectionSingleton;
import com.tp.mq.util.MqNameUtil;
import com.tp.mq.util.MqQueueHaPolicySingleton;
import com.tp.mq.util.MqSerializeUtil;

/**
 * P2P点对点消息 接收对象
 * 
 * @author szy
 *
 */
public class ConsumerP2pMessage implements Runnable {

	private static Log logger;

	private volatile boolean stopRequested = false;

	private volatile MqMessageCallBack messageCallBack;

	private volatile String queueName;

	private volatile MqMessageConfigs messageConfigs;

	private volatile boolean autoAck = false;

	private volatile int recheckCount = 0;//客户端业务重试次数，最大值为2

	private volatile int recheckIntervalTime = 200;//毫秒

	public void setQueueName(String queueName) {
		this.queueName = queueName;
		logger = LogFactory.getLog(ConsumerP2pMessage.class + "queueName=>" + queueName);
	}

	public void run() {
		boolean autoAck = this.getAutoAck();
		Channel channel = null;
		while (!stopRequested) {
			boolean exceptionflag = false;
			try {
				channel = getP2pChannel(queueName);
				String newQueueName = MqNameUtil.getQueueName(queueName);
				if (null != channel) {
					channel.basicQos(1);
					QueueingConsumer consumer = new QueueingConsumer(channel);
					channel.basicConsume(newQueueName, autoAck, consumer);

					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					boolean reciveOk = false;
					if (null != delivery) {
						byte[] bytes = delivery.getBody();
						if (null != bytes) {
							Object message = MqSerializeUtil.unserialize(bytes);
							reciveOk = messageCallBack.execute(message);

							if (!reciveOk) {
								for (int i = 0; i < recheckCount; i++) {
									try {
										reciveOk = messageCallBack.execute(message);
										if (reciveOk) {
											break;
										}
									} catch (Exception e) {
										logger.info(e.toString(),e);
									}
								}
							}
						}
						if (!autoAck && reciveOk) {
							channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
						}
					}
				}
			} catch (Exception ex) {
				exceptionflag = true;
				logger.error("run() exception for=>" + ex.toString(), ex);
			} finally {
				try {
					if (channel != null) {
						channel.close();
					}
				} catch (Exception e) {
					logger.error("close channel exception for=>" + e.toString(), e);
				}
				if (exceptionflag) {
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (Exception e) {
						logger.error("exceptionflag exception for=>" + e.toString(), e);
					}
				}
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

	public MqMessageConfigs getMessageConfigs() {
		return messageConfigs;
	}

	public void setMessageConfigs(MqMessageConfigs messageConfigs) {
		this.messageConfigs = messageConfigs;
	}

	public String getQueueName() {
		return queueName;
	}

	public Channel getP2pChannel(String queueName) throws MqClientException, IOException {
		if (null == queueName || "".equals(queueName.trim())) {
			String message = "sendP2PMessage queue name is null ";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (this.getMessageConfigs().existsP2p()) {
			String message = "sendP2PMessage queue name is multi ";
			logger.error(message);
			throw new MqClientException(message);
		}

		MqP2pConfig config = messageConfigs.getQueueConfigDO(queueName);
		if (null == config) {
			String message = "queueName =>" + queueName + " config  is null ";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (null != config && !config.getSendMessageType().equals(MqConstants.SEND_MESSAGE_TYPE_P2P)) {
			String message = "queue =>" + queueName + " type is:" + config.getSendMessageType() + "";
			logger.error(message);
			throw new MqClientException(message);
		}
		Channel channel = MqConsumerConnectionSingleton.connection().createChannel();

		String exchangeName = MqNameUtil.getExchangeName(queueName);
		String newQueueName = MqNameUtil.getQueueName(queueName);
		String routingKey = MqNameUtil.getRoutingKey(queueName);

		boolean durable = config.isQueueDurable();

		channel.exchangeDeclare(exchangeName, MqConstants.EXCHANGE_TYPE_DIRECT, true);
		channel.queueDeclare(newQueueName, durable, false, false, MqQueueHaPolicySingleton.getInstance().getQueueArgs());
		channel.queueBind(newQueueName, exchangeName, routingKey);

		return channel;
	}

	private boolean getAutoAck() {
		return false;
		// MqP2pConfig config = messageConfigs.getQueueConfigDO(queueName);
		// return null != config ? config.isAutoAck() : this.autoAck;
	}

	public int getRecheckCount() {
		return recheckCount;
	}

	public void setRecheckCount(int recheckCount) {
		this.recheckCount = recheckCount;
	}

	public int getRecheckIntervalTime() {
		return recheckIntervalTime;
	}

	public void setRecheckIntervalTime(int recheckIntervalTime) {
		this.recheckIntervalTime = recheckIntervalTime;
	}
	
}
