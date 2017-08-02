package com.tp.dto.ord.kuaidi100;

import java.util.HashMap;

public class PushExpressInfoRequest {
	/** 订阅的快递公司的编码，一律用小写字母 */
	private String company;
	/** 订阅的快递单号 */
	private String number;
	/** 出发地城市 */
	private String from;
	/** 目的地城市，到达目的地后会加大监控频率 */
	private String to;
	/** 授权码 */
	private String key;

	private String src;

	/**
	 * 具体数据
	 *  { "callbackurl":"回调地址" "salt":"签名用随机字符串（可选）" "resultv2":"1:表示开通行政区域解析功能（仅对开通签收状态服务用户有效）" }
	 */
	private HashMap<String, String> parameters = new HashMap<String, String>();

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

}
