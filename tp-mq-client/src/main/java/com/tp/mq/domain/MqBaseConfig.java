package com.tp.mq.domain;

/**
 * @author szy
 *
 */
public class MqBaseConfig {

	private String hosts;

	/** username */
	private String username;

	/** password */
	private String password;

	/** port */
	private int port;

	/** Mirror Queue */
	private boolean allQueueMirror = false;

	/** Consumer Connection Count */
	private Integer consumerConnectionCount;

	/** Consumer Thread Count */
	private Integer consumerThreadCount;

	/** Product Connection Count */
	private Integer productConnectionCount;

	/** Product Thread Count */
	private Integer productThreadCount;

	/** connectionFactory Heartbeat unit：seconds */
	private Integer requestedHeartbeat;

	private boolean automaticRecoveryEnabled = false;

	private static volatile MqBaseConfig baseConfig;

	public static MqBaseConfig getBaseConfig() {
		return baseConfig;
	}

	public void init() {
		baseConfig = this;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAllQueueMirror() {
		return allQueueMirror;
	}

	public void setAllQueueMirror(boolean allQueueMirror) {
		this.allQueueMirror = allQueueMirror;
	}

	public Integer getConsumerConnectionCount() {
		return consumerConnectionCount;
	}

	public void setConsumerConnectionCount(Integer consumerConnectionCount) {
		this.consumerConnectionCount = consumerConnectionCount;
	}

	public Integer getConsumerThreadCount() {
		return consumerThreadCount;
	}

	public void setConsumerThreadCount(Integer consumerThreadCount) {
		this.consumerThreadCount = consumerThreadCount;
	}

	public Integer getProductConnectionCount() {
		return productConnectionCount;
	}

	public void setProductConnectionCount(Integer productConnectionCount) {
		this.productConnectionCount = productConnectionCount;
	}

	public Integer getProductThreadCount() {
		return productThreadCount;
	}

	public void setProductThreadCount(Integer productThreadCount) {
		this.productThreadCount = productThreadCount;
	}

	public Integer getRequestedHeartbeat() {
		if (null != requestedHeartbeat) {
			return requestedHeartbeat;
		}
		return 10;
	}

	public void setRequestedHeartbeat(Integer requestedHeartbeat) {
		this.requestedHeartbeat = requestedHeartbeat;
	}

	public boolean isAutomaticRecoveryEnabled() {
		return automaticRecoveryEnabled;
	}

	public void setAutomaticRecoveryEnabled(boolean automaticRecoveryEnabled) {
		this.automaticRecoveryEnabled = automaticRecoveryEnabled;
	}

}
