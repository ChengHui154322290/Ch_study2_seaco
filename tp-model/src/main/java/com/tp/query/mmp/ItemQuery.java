package com.tp.query.mmp;

import java.io.Serializable;

/**
 * @author szy
 * @version 0.0.1
 */
public class ItemQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6896217805487855001L;

	private String sku;
	
	private int amount;
	
	private long  topicId;

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

}
