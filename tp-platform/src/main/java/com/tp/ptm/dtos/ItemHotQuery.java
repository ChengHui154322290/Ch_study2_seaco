package com.tp.ptm.dtos;

import java.io.Serializable;

public class ItemHotQuery implements Serializable{

	private static final long serialVersionUID = 868600969391954861L;
	private String sku;
	private Long topicId;
	
	ItemHotQuery(){}
	
	ItemHotQuery(String sku){
		this.sku=sku;
	}
	ItemHotQuery(String sku,Long topicId){
		this.sku=sku;
		this.topicId=topicId;
	}
	ItemHotQuery(Long topicId){
		this.topicId=topicId;
	}
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
}
