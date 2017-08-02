package com.tp.query.mmp;

import java.io.Serializable;
import java.util.List;

/**
 * 商品查询促销专题信息接口条件
 * @author szy
 *
 */
public class TopicItemInfoQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6759485063924953842L;

	private long topicId; //专题id
	
	private List<String> skuList; //sku list
	
	private String areaStr;// 所在地区
	
	private int amount;//购买数量
	
	private int platform;//所用平台
	

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}
	
	
	
	
}
