package com.tp.m.query.dss;

import java.util.Map;

import com.tp.m.base.BaseQuery;

public class QueryPromoterTopic extends BaseQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113587159253560795L;

	
	/** 推广员id **/
	private Long promoterid;
	/** 推广员类型 **/
	private String type;
	/** 推广员级别 **/
	private String level;

	/** 主题ID **/
	private Long topicid;
	
	/** 商品sku**/
	private String sku;
		
	/** 上下架情况 0:下架 1:上架**/
	private Integer onshelf;

	/** 上下架类型 0:专题 1:专题商品**/
	private Integer shelftype;
	
	/**专题类型**/	
	private Integer topictype;

	
			
	public Integer getTopictype() {
		return topictype;
	}

	public void setTopictype(Integer topictype) {
		this.topictype = topictype;
	}

	public Integer getShelftype() {
		return shelftype;
	}

	public void setShelftype(Integer shelftype) {
		this.shelftype = shelftype;
	}

	public Integer getOnshelf() {
		return onshelf;
	}

	public void setOnshelf(Integer onshelf) {
		this.onshelf = onshelf;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getTopicid() {
		return topicid;
	}

	public void setTopicid(Long topicid) {
		this.topicid = topicid;
	}

	public Long getPromoterid() {
		return promoterid;
	}

	public void setPromoterid(Long promoterid) {
		this.promoterid = promoterid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
