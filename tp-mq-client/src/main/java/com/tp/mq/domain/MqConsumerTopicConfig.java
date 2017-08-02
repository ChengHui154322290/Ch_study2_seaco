package com.tp.mq.domain;

import com.tp.mq.MqMessageCallBack;

/**
 * @author szy
 *
 */
public class MqConsumerTopicConfig {
	
	private MqTopicConfig topicConfig;
	
	/** recive message bean implements for MqMessageCallBack */
	private MqMessageCallBack messageCallBack;
	
	public MqMessageCallBack getMessageCallBack() {
		return messageCallBack;
	}

	public void setMessageCallBack(MqMessageCallBack messageCallBack) {
		this.messageCallBack = messageCallBack;
	}

	public MqTopicConfig getTopicConfig() {
		return topicConfig;
	}

	public void setTopicConfig(MqTopicConfig topicConfig) {
		this.topicConfig = topicConfig;
	}
	
}
