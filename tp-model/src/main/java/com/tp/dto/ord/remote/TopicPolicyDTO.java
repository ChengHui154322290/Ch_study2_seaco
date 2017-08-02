/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.remote;

import java.io.Serializable;

/**
 * <pre>
 * 促销限购信息
 * </pre>
 * 
 * @author szy
 * @time 2015-2-11 下午6:12:13
 */
public class TopicPolicyDTO implements Serializable {

	private static final long serialVersionUID = 6607728370115073611L;
	
	/** 促销ID */
	private Long topicId;
	/** 商品skucode **/
	private String skuCode;
	/** 用户ID */
	private  Long userId;
	/**个人限购商品总数 */
	private Integer topicSum;
	/**用户购买数量 */
	private Integer quantity;
	/** 用户下单IP */
	private String ip;
	/** 收货人手机 */
	private String mobile;
	
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getTopicSum() {
		return topicSum;
	}
	public void setTopicSum(Integer topicSum) {
		this.topicSum = topicSum;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
