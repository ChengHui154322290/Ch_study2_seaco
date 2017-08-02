package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.List;

import com.tp.result.mmp.TopicItemInfoResult;

/**
 * 根据sku获得正在进行的专题和即将开始的专题
 * 
 * @author szy
 *
 */
public class SkuTopicDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1701599953847351203L;

	private List<TopicItemInfoResult> onSaleList;
	
	private List<TopicItemInfoResult> willSaleList;

	public List<TopicItemInfoResult> getOnSaleList() {
		return onSaleList;
	}

	public void setOnSaleList(List<TopicItemInfoResult> onSaleList) {
		this.onSaleList = onSaleList;
	}

	public List<TopicItemInfoResult> getWillSaleList() {
		return willSaleList;
	}

	public void setWillSaleList(List<TopicItemInfoResult> willSaleList) {
		this.willSaleList = willSaleList;
	}
	
	
	
}
