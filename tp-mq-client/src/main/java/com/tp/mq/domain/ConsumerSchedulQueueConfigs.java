package com.tp.mq.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author szy
 *
 */
public class ConsumerSchedulQueueConfigs {

	private List<MqConsumerP2pConfig> p2pConfigListeners = new ArrayList<MqConsumerP2pConfig>();

	private List<MqConsumerTopicConfig> topicConfigListeners = new ArrayList<MqConsumerTopicConfig>();

	public List<MqConsumerP2pConfig> getP2pConfigListeners() {
		return p2pConfigListeners;
	}

	public void setP2pConfigListeners(List<MqConsumerP2pConfig> p2pConfigListeners) {
		this.p2pConfigListeners = p2pConfigListeners;
	}

	public List<MqConsumerTopicConfig> getTopicConfigListeners() {
		return topicConfigListeners;
	}

	public void setTopicConfigListeners(List<MqConsumerTopicConfig> topicConfigListeners) {
		this.topicConfigListeners = topicConfigListeners;
	}

}
