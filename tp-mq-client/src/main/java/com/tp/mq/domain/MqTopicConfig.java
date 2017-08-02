package com.tp.mq.domain;


/**
 * @author szy topic message base config
 */
public class MqTopicConfig {

	/** Exchange Durable */
	private boolean exchangeDurable = true;
	
	/** Queue Name,exchangeName on the basis of this queue name*/
	private String queueName;

	/** Exchange Name */
	private String exchangeName;

	/** Exchange Type,this value must "fanout" */
	private String exchangeType = MqConstants.EXCHANGE_TYPE_FANOUT;

	/** Send Message Type,this value must pub_sub */
	private String sendMessageType = MqConstants.SEND_MESSAGE_TYPE_PUB_SUB;

	public boolean isExchangeDurable() {
		return exchangeDurable;
	}

	public void setExchangeDurable(boolean exchangeDurable) {
		this.exchangeDurable = exchangeDurable;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getSendMessageType() {
		return sendMessageType;
	}

	public void setSendMessageType(String sendMessageType) {
		this.sendMessageType = sendMessageType;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

}
