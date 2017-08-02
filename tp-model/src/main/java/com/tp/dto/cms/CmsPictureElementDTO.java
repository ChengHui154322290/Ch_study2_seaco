package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

/**
 * 图片元素
 * 
 * @author szy
 */

public class CmsPictureElementDTO extends BaseDO implements Serializable{

	private static final long serialVersionUID = -4829711039750374810L;

	/** 主键 */
	private Long id;

	/** 位置表主键 */
	private Long positionId;

	/** 图片名称 */
	private String name;

	/** 启用时间 */
	private Date startdate;

	/** 失效时间 */
	private Date enddate;

	/** 图片地址 */
	private String picSrc;
	
	/** 卷帘图片地址 */
	private String rollPicSrc;
	
	/** 图片地址：拼接好的前缀 */
	private String picSrcStr;
	
	/** 卷帘图片地址：拼接好的前缀 */
	private String rollPicSrcStr;

	/** 状态(0正常，1停用，2删除) */
	private Integer status;
	
	/** 图片属性 */
	private String attr;
	
	/** 活动名称 */
	private String actname;
	
	/** 跳转链接 */
	private String link;

	/** 创建人 */
	private Long creater;

	/** 创建时间 */
	private Date createTime;

	/** 修改人 */
	private Long modifier;

	/** 修改时间 */
	private Date modifyTime;
	
	/** sku */
	private String sku;
	
	/** 活动id */
	private Long activityid;
	
	/** 活动类型 */
	private String acttype;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 位置表主键
	 * 
	 * @param positionId
	 */
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	/**
	 * 设置 图片名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置 启用时间
	 * 
	 * @param startdate
	 */
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	/**
	 * 设置 失效时间
	 * 
	 * @param enddate
	 */
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	/**
	 * 设置 图片地址
	 * 
	 * @param picSrc
	 */
	public void setPicSrc(String picSrc) {
		this.picSrc = picSrc;
	}

	/**
	 * 设置 状态(0正常，1停用，2删除)
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置 创建人
	 * 
	 * @param creater
	 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/**
	 * 设置 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置 修改人
	 * 
	 * @param modifier
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	/**
	 * 设置 修改时间
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 位置表主键
	 * 
	 * @return positionId
	 */
	public Long getPositionId() {
		return positionId;
	}

	/**
	 * 获取 图片名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取 启用时间
	 * 
	 * @return startdate
	 */
	public Date getStartdate() {
		return startdate;
	}

	/**
	 * 获取 失效时间
	 * 
	 * @return enddate
	 */
	public Date getEnddate() {
		return enddate;
	}

	/**
	 * 获取 图片地址
	 * 
	 * @return picSrc
	 */
	public String getPicSrc() {
		return picSrc;
	}

	/**
	 * 获取 状态(0正常，1停用，2删除)
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取 创建人
	 * 
	 * @return creater
	 */
	public Long getCreater() {
		return creater;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取 修改人
	 * 
	 * @return modifier
	 */
	public Long getModifier() {
		return modifier;
	}

	/**
	 * 获取 修改时间
	 * 
	 * @return modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRollPicSrc() {
		return rollPicSrc;
	}

	public void setRollPicSrc(String rollPicSrc) {
		this.rollPicSrc = rollPicSrc;
	}

	public String getPicSrcStr() {
		return picSrcStr;
	}

	public void setPicSrcStr(String picSrcStr) {
		this.picSrcStr = picSrcStr;
	}

	public String getRollPicSrcStr() {
		return rollPicSrcStr;
	}

	public void setRollPicSrcStr(String rollPicSrcStr) {
		this.rollPicSrcStr = rollPicSrcStr;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getActivityid() {
		return activityid;
	}

	public void setActivityid(Long activityid) {
		this.activityid = activityid;
	}

	public String getActtype() {
		return acttype;
	}

	public void setActtype(String actType) {
		this.acttype = actType;
	}

}