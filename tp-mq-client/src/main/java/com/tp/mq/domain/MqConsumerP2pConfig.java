package com.tp.mq.domain;

import com.tp.mq.MqMessageCallBack;

/**
 * @author szy
 *
 */
public class MqConsumerP2pConfig {

	private MqP2pConfig p2pConfig;

	/** recive message bean implements for MqMessageCallBack */
	private MqMessageCallBack messageCallBack;

	private int recheckCount = 0;

	private int recheckIntervalTime = 200;

	public MqMessageCallBack getMessageCallBack() {
		return messageCallBack;
	}

	public void setMessageCallBack(MqMessageCallBack messageCallBack) {
		this.messageCallBack = messageCallBack;
	}

	public MqP2pConfig getP2pConfig() {
		return p2pConfig;
	}

	public void setP2pConfig(MqP2pConfig p2pConfig) {
		this.p2pConfig = p2pConfig;
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
