package com.tp.dto.cms.query;

import java.io.Serializable;

public class ParamSingleBusTemQuery implements Serializable{
	
	private static final long serialVersionUID = 8493189916013956652L;

	/** 用户id */
	private Long userId;
	
	/***平台类型*/
	private Integer platformType;
	
	/** 活动id */
	private Long topicId;
	
	/** 地区id */
	private Integer areaId;
	
	/** 起始页 */
	private Integer pagestart;
	
	/** 页大小 */
	private Integer pagesize; 
	
	/** 第几天的天数，用于明日预告的后面天数 */
	private Integer dateCounts; 
	
	/** 用户电话号码  */
	private String tele;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Integer getPagestart() {
		return pagestart;
	}

	public void setPagestart(Integer pagestart) {
		this.pagestart = pagestart;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public Integer getDateCounts() {
		return dateCounts;
	}

	public void setDateCounts(Integer dateCounts) {
		this.dateCounts = dateCounts;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
	

}
