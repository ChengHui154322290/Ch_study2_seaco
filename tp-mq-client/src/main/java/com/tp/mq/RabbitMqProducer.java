package com.tp.mq;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.tp.mq.domain.MqConstants;
import com.tp.mq.domain.MqMessageConfigs;
import com.tp.mq.domain.MqP2pConfig;
import com.tp.mq.domain.MqTopicConfig;
import com.tp.mq.exception.MqClientException;
import com.tp.mq.util.MqNameUtil;
import com.tp.mq.util.MqProductConnectionSingleton;
import com.tp.mq.util.MqQueueHaPolicySingleton;
import com.tp.mq.util.MqSerializeUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ReturnListener;

/**
 * @author szy
 *
 */
public class RabbitMqProducer {

	private static final Log logger = LogFactory.getLog(RabbitMqProducer.class);

	private MqMessageConfigs messageConfigs;

	private SendMessageFailCallBack sendMessageFailCallBack;

	public MqMessageConfigs getMessageConfigs() {
		return messageConfigs;
	}

	public void setMessageConfigs(MqMessageConfigs messageConfigs) {
		this.messageConfigs = messageConfigs;
	}

	private boolean mandatory = false;

	/**
	 * 异步发送点对点消息
	 * 
	 * @param oldQueueName
	 *            队列名
	 * @param obj
	 *            要发送的消息对象
	 * @throws IOException
	 */
	public void sendP2PMessage(String oldQueueName, Object obj) throws MqClientException {
		if (null == obj) {
			String message = oldQueueName + " send message must not null ";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (!(obj instanceof Serializable)) {
			String message = oldQueueName + " send message must instanceof Serializable interface ";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (this.getMessageConfigs().existsP2p()) {
			String message = oldQueueName + "sendP2PMessage 队列名配置重复";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (null != oldQueueName && !"".equals(oldQueueName)) {
			Channel channel = null;
			boolean messageBurable = true;
			BasicProperties baseProp = MessageProperties.PERSISTENT_TEXT_PLAIN;
			MqP2pConfig queueConfigDO = messageConfigs.getQueueConfigDO(oldQueueName);
			if (!queueConfigDO.getSendMessageType().equalsIgnoreCase(MqConstants.SEND_MESSAGE_TYPE_P2P)) {
				String message = "oldQueueName=>" + oldQueueName + "send message type must " + MqConstants.SEND_MESSAGE_TYPE_P2P;
				logger.error(message);
				throw new MqClientException(message);
			}
			String exchangeName = MqNameUtil.getExchangeName(oldQueueName);
			String newQueueName = MqNameUtil.getQueueName(oldQueueName);
			String routingKey = MqNameUtil.getRoutingKey(oldQueueName);
			try {
				channel = MqProductConnectionSingleton.connection().createChannel();

				channel.addReturnListener(new ReturnListener() {// 处理发送失败的消息
					public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, BasicProperties properties, byte[] body) throws IOException {
						StringBuffer sb = new StringBuffer();
						sb.append("-----------get returnListener reply message---------");
						sb.append("replyCode=> " + replyCode);
						sb.append("replyText=> " + replyText);
						sb.append("exchange=> " + exchange);
						sb.append("routingKey=> " + routingKey);
						sb.append("ContentEncoding=> " + properties.getContentEncoding());
						sb.append("content type=> " + properties.getContentType());
						sb.append("Expiration=> " + properties.getExpiration());
						sb.append("type=> " + properties.getType());
						sb.append("reply to=> " + properties.getReplyTo());
						sb.append("body after unserialize=> " + MqSerializeUtil.unserialize(body));
						if (logger.isInfoEnabled()) {
							logger.info(sb.toString());
						}
						if (null != sendMessageFailCallBack) {
							try {
								sendMessageFailCallBack.execute(replyCode, replyText, exchange, routingKey, properties, body);
							} catch (Exception ex) {
								logger.error(ex.getMessage(), ex);
							}
						}
					}
				});

				if (!queueConfigDO.isQueueDurable()) {
					messageBurable = false;
					baseProp = null;
				}
				channel.exchangeDeclare(exchangeName, MqConstants.EXCHANGE_TYPE_DIRECT, true);
				channel.queueDeclare(newQueueName, messageBurable, false, false, MqQueueHaPolicySingleton.getInstance().getQueueArgs());
				channel.queueBind(newQueueName, exchangeName, routingKey);
				byte[] bytes = MqSerializeUtil.serialize(obj);

				if (isMandatory()) {
					channel.basicPublish(exchangeName, routingKey, true, baseProp, bytes);
				} else {
					channel.basicPublish(exchangeName, routingKey, baseProp, bytes);
				}

			} catch (Exception ex) {
				String message = "oldQueueName=>" + oldQueueName + "send message exception for:" + ex.getMessage();
				logger.error(message, ex);
				throw new MqClientException(message, ex);

			} finally {
				try {
					if (channel != null) {
						channel.close();
					}
				} catch (Exception ex) {
					String message = "queueName=" + oldQueueName + "channel close exception for " + ex.getMessage();
					logger.error(message);
					throw new MqClientException(message, ex);
				}
			}
		}
	}

	/**
	 * 同步发送点对点消息,确保消息无丢失，但性能较低
	 * 
	 * @param oldQueueName
	 *            队列名
	 * @param obj
	 *            要发送的消息对象
	 * @return true 消息发送成功，false消息发送失败
	 */
	public boolean sendSyncP2PMessage(String oldQueueName, final Object obj) {
		if (null == obj) {
			String message = oldQueueName + " send message must not null ";
			logger.error(message);
			return false;
		}
		if (!(obj instanceof Serializable)) {
			String message = oldQueueName + " send message must instanceof Serializable interface ";
			logger.error(message);
			return false;
		}
		if (this.getMessageConfigs().existsP2p()) {
			String message = oldQueueName + "sendP2PMessage 队列名配置重复";
			logger.error(message);
			return false;
		}
		boolean isSuccess = false;
		if (null != oldQueueName && !"".equals(oldQueueName)) {
			Channel channel = null;
			boolean messageBurable = true;
			BasicProperties baseProp = MessageProperties.PERSISTENT_TEXT_PLAIN;
			MqP2pConfig queueConfigDO = messageConfigs.getQueueConfigDO(oldQueueName);
			if (!queueConfigDO.getSendMessageType().equalsIgnoreCase(MqConstants.SEND_MESSAGE_TYPE_P2P)) {
				String message = "oldQueueName=>" + oldQueueName + "send message type must " + MqConstants.SEND_MESSAGE_TYPE_P2P;
				logger.error(message);
				return false;
			}
			String exchangeName = MqNameUtil.getExchangeName(oldQueueName);
			String newQueueName = MqNameUtil.getQueueName(oldQueueName);
			String routingKey = MqNameUtil.getRoutingKey(oldQueueName);
			try {
				channel = MqProductConnectionSingleton.connection().createChannel();
				if (null != channel) {
					channel.txSelect();
					if (!queueConfigDO.isQueueDurable()) {
						messageBurable = false;
						baseProp = null;
					}
					channel.exchangeDeclare(exchangeName, MqConstants.EXCHANGE_TYPE_DIRECT, true);
					channel.queueDeclare(newQueueName, messageBurable, false, false, MqQueueHaPolicySingleton.getInstance().getQueueArgs());
					channel.queueBind(newQueueName, exchangeName, routingKey);
					byte[] bytes = MqSerializeUtil.serialize(obj);

					if (isMandatory()) {
						channel.basicPublish(exchangeName, routingKey, true, baseProp, bytes);
					} else {
						channel.basicPublish(exchangeName, routingKey, baseProp, bytes);
					}
					channel.txCommit();
					isSuccess = true;
				}
			} catch (Exception ex) {
				String message = "oldQueueName=>" + oldQueueName + "send message exception for:" + ex.getMessage();
				logger.error(message, ex);
				if (null != channel) {
					try {
						channel.txRollback();
					} catch (IOException e) {
						logger.error(message, e);
					}
				}
				isSuccess = false;
			} finally {
				try {
					if (channel != null) {
						channel.close();
					}
				} catch (Exception ex) {
					String message = "queueName=" + oldQueueName + "channel close exception for " + ex.getMessage();
					logger.error(message);
				}
			}
			return isSuccess;
		}
		return isSuccess;
	}

	/**
	 * 发送主题订阅消息 不支持持久化
	 * 
	 * @param queueName
	 *            队列名
	 * @param t
	 *            要发送的消息
	 * @throws IOException
	 */
	public void sendTopicMessage(String queueName, Object t) throws MqClientException {
		if (null == t) {
			String message = queueName + " send message must not null ";
			logger.error(message);
			throw new MqClientException(message);
		}
		if (null != queueName && !"".equals(queueName)) {
			if (!(t instanceof Serializable)) {
				String message = queueName + " send message must instanceof Serializable interface ";
				logger.error(message);
				throw new MqClientException(message);
			}
			if (this.getMessageConfigs().existsTopic()) {
				logger.error("sendTopicMessage 队列名配置重复");
				throw new MqClientException("sendTopicMessage 队列名配置重复");
			}

			MqTopicConfig topicConfigDO = messageConfigs.getTopicConfigDO(queueName);
			if (null != topicConfigDO && !topicConfigDO.getSendMessageType().equalsIgnoreCase(MqConstants.SEND_MESSAGE_TYPE_PUB_SUB)) {
				String message = "oldQueueName=>" + queueName + "send message type must " + MqConstants.SEND_MESSAGE_TYPE_PUB_SUB;
				logger.error(message);
				throw new MqClientException(message);
			}

			String exchangeName = MqNameUtil.getExchangeName(queueName);
			Channel channel = null;
			try {
				channel = MqProductConnectionSingleton.connection().createChannel();
				channel.exchangeDeclare(exchangeName, MqConstants.EXCHANGE_TYPE_FANOUT, true);

				byte[] bytes = MqSerializeUtil.serialize(t);
				channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, bytes);
			} catch (Exception ex) {
				String message = "sendTopicMessage method queueName = " + queueName + " ;exchangeName=" + exchangeName + ",exception for " + ex.getMessage();
				logger.error(message, ex);
				throw new MqClientException(message, ex);
			} finally {
				try {
					if (channel != null) {
						channel.close();
					}
				} catch (Exception ex) {
					String message = "sendTopicMessage method  queueName = " + queueName + ",channel close exception for " + ex.getMessage();
					logger.error(message, ex);
					throw new MqClientException(message, ex);
				}
			}
		}

	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public SendMessageFailCallBack getSendMessageFailCallBack() {
		return sendMessageFailCallBack;
	}

	public void setSendMessageFailCallBack(SendMessageFailCallBack sendMessageFailCallBack) {
		this.sendMessageFailCallBack = sendMessageFailCallBack;
	}

}
