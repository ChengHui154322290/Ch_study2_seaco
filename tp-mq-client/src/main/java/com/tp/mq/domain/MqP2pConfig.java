package com.tp.mq.domain;


/**
 * @author szy message base config
 */
public class MqP2pConfig {

	/** Exchange Durable */
	private boolean exchangeDurable = true;

	/** Queue Durable */
	private boolean queueDurable = true;

	/** Message Durable */
	private boolean messageDurable = true;

	/** Exchange Name */
	private String exchangeName;

	/** Queue Name */
	private String queueName;

	/** Mirror Queue*/
	private boolean mirrorQueue;

	/** Exchange Type */
	private String exchangeType;

	/** RoutingKey */
	private String routingKey;

	/** SendMessageType p2p(点对点) this value must p2p */
	private String sendMessageType;

	/** 默认为 不自动应答消息(自动应答后，接收到消息会自动删除掉，不是通过程序删除的) */
	private boolean autoAck = false;

	/** client Consumer Thread Count */
	private int consumerThreadCount = 1;
	
	public boolean isExchangeDurable() {
		return exchangeDurable;
	}

	public void setExchangeDurable(boolean exchangeDurable) {
		this.exchangeDurable = exchangeDurable;
	}

	public boolean isQueueDurable() {
		return queueDurable;
	}

	public void setQueueDurable(boolean queueDurable) {
		this.queueDurable = queueDurable;
	}

	public boolean isMessageDurable() {
		return messageDurable;
	}

	public void setMessageDurable(boolean messageDurable) {
		this.messageDurable = messageDurable;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isMirrorQueue() {
		return mirrorQueue;
	}

	public void setMirrorQueue(boolean mirrorQueue) {
		this.mirrorQueue = mirrorQueue;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getSendMessageType() {
		return sendMessageType;
	}

	public void setSendMessageType(String sendMessageType) {
		this.sendMessageType = sendMessageType;
	}

	public boolean isAutoAck() {
		return autoAck;
	}

	public void setAutoAck(boolean autoAck) {
		this.autoAck = autoAck;
	}

	public int getConsumerThreadCount() {
		if (consumerThreadCount > 1) {
			return 2;
		}
		return consumerThreadCount;
	}

	public void setConsumerThreadCount(int consumerThreadCount) {
		this.consumerThreadCount = consumerThreadCount;
	}

}
