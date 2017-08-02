package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.List;

import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;

public class TopicItemBrandCategoryDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6677178823859042424L;

	private List<Long> brandIdList;
	
	private List<Long> categoryIdList;
	
	private Topic topic;


	public List<Long> getBrandIdList() {
		return brandIdList;
	}

	public void setBrandIdList(List<Long> brandIdList) {
		this.brandIdList = brandIdList;
	}

	public List<Long> getCategoryIdList() {
		return categoryIdList;
	}

	public void setCategoryIdList(List<Long> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
}
