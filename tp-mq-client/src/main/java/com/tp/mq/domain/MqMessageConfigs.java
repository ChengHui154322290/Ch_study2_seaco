package com.tp.mq.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author szy
 *
 */
public class MqMessageConfigs {

	/** key 为queue name,value为 MqP2pConfig 对象 */
	private Map<String, MqP2pConfig> p2pConfigMap;

	/** key为exchangeName value为 MqTopicConfig对象 */
	private Map<String, MqTopicConfig> topicConfigsMap;

	public Map<String, MqP2pConfig> getP2pConfigMap() {
		return p2pConfigMap;
	}

	public void setP2pConfigMap(Map<String, MqP2pConfig> queueConfigMap) {
		this.p2pConfigMap = queueConfigMap;
	}

	public MqP2pConfig getQueueConfigDO(String queueName) {
		return this.getP2pConfigMap().get(queueName);
	}

	public MqTopicConfig getTopicConfigDO(String exchangeName) {
		return this.getTopicConfigsMap().get(exchangeName);
	}

	private boolean existsExchangeMulti() {
		if (null != this.p2pConfigMap && !this.p2pConfigMap.isEmpty()) {
			Set<String> queueExchangeSet = new HashSet<String>();
			for (Map.Entry<String, MqP2pConfig> entry : this.p2pConfigMap.entrySet()) {
				if (null != entry && null != entry.getValue()) {
					queueExchangeSet.add(entry.getValue().getExchangeName());
				}
			}
			if (null != this.topicConfigsMap && !this.topicConfigsMap.isEmpty()) {
				Set<String> topicExchangeNameSet = this.topicConfigsMap.keySet();
				for (String str : topicExchangeNameSet) {
					if (queueExchangeSet.contains(str)) {
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

	public boolean existsP2p() {
		if (null != this.p2pConfigMap && !this.p2pConfigMap.isEmpty()) {
			Set<String> queueExchangeSet = this.p2pConfigMap.keySet();
			if (queueExchangeSet.size() != this.p2pConfigMap.size()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean existsTopic() {
		if (null != this.topicConfigsMap && !this.topicConfigsMap.isEmpty()) {
			Set<String> topicExchangeNameSet = this.topicConfigsMap.keySet();
			if (topicExchangeNameSet.size() != this.topicConfigsMap.size()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public Map<String, MqTopicConfig> getTopicConfigsMap() {
		return topicConfigsMap;
	}

	public void setTopicConfigsMap(Map<String, MqTopicConfig> topicConfigsMap) {
		this.topicConfigsMap = topicConfigsMap;
	}

}
