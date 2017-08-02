package com.tp.query.mmp;

import java.io.Serializable;

import com.tp.dto.mmp.enums.CmsForcaseType;

public class CmsTopicSimpleQuery implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 6349589590946101946L;

	/**查询的参数数据*/
	private String data; 
	
	/***平台类型*/
	private Integer platformType;
	
	/***所在地区的id */
	private Integer areaId;
	
	/**专题类型*/
	private Integer topicType;
	
	private Integer pageSize;
	
	private Integer pageId  = 1;

	/** 返回结果方式*/
	private CmsForcaseType forcaseType;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return the forcaseType
	 */
	public CmsForcaseType getForcaseType() {
		return forcaseType;
	}

	/**
	 * @param forcaseType the forcaseType to set
	 */
	public void setForcaseType(CmsForcaseType forcaseType) {
		this.forcaseType = forcaseType;
	}

}
