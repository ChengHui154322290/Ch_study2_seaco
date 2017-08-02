package com.tp.query.bse;

import java.io.Serializable;

public class DistrictInfoQuery implements Serializable{
	private static final long serialVersionUID = -3690415163264271888L;
	
	private Integer id;
	private String name;
	private Boolean isLeaf;
	/**
	 * 地区类别(行政级别)：0-世界，1-世界政区，2-国家，3-国家政区，4-省，5-市，6-县/区，9-直辖市
	 */
	private Short catType;
	private String group_;
	/** 是否是热门银行，0非，1是**/
	private Integer hotFlag;
	/** 主要是为银行显示图片坐标所用 **/
	private String position ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public Short getCatType() {
		return catType;
	}
	public void setCatType(Short catType) {
		this.catType = catType;
	}
	public String getGroup_() {
		return group_;
	}
	public void setGroup_(String group_) {
		this.group_ = group_;
	}
	public Integer getHotFlag() {
		return hotFlag;
	}
	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
