package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.BaseDO;

public class CmsStaticActivityInfoDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = -1307199090476518398L;

	/** 主键 */
	private Long id;

	/** 活动类型 */
//	private Integer activityType;

	/** 活动名称 */
	private String activityName;

	/** 活动上线时间 */
	private Date startdate;

	/** 活动下线时间 */
	private Date enddate;

	/** 是否预热(0代表否，1代表是) */
	private Integer ifPreheat;

	/** 预热开始时间 */
	private Date preheatStartdate;

	/** 预热结束时间 */
	private Date preheatEnddate;
	
	/** 预热页面id */
	private Long preheatId;

	/** 活动平台 */
	private Integer platform;

	/** title词 */
	private String title;

	/** meta-keyword */
	private String metaKeyword;

	/** meta-description */
	private String metaDescription;

	/** 页面背景颜色 */
	private String activityBackend;

	/** 页面图片路径 */
	private String activityPicSrc;
	
	/** 页面图片路径(已拼接http头的路径) */
	private String spelActivityPicSrc;

	/** 备注 */
	private String remark;

	/** 状态(0正常，1删除) */
	private Integer status;

	/** 创建人 */
	private Integer creater;

	/** 创建时间 */
	private Date createTime;

	/** 修改人 */
	private Integer modifier;

	/** 修改时间 */
	private Date modifyTime;
	
	/** 模板的集合 */
	private List<CmsStaticActivityTempletDTO> list = null;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 活动名称
	 * 
	 * @param activityName
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * 设置 活动上线时间
	 * 
	 * @param startdate
	 */
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	/**
	 * 设置 活动下线时间
	 * 
	 * @param enddate
	 */
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	/**
	 * 设置 是否预热(0代表否，1代表是)
	 * 
	 * @param ifPreheat
	 */
	public void setIfPreheat(Integer ifPreheat) {
		this.ifPreheat = ifPreheat;
	}

	/**
	 * 设置 预热开始时间
	 * 
	 * @param preheatStartdate
	 */
	public void setPreheatStartdate(Date preheatStartdate) {
		this.preheatStartdate = preheatStartdate;
	}

	/**
	 * 设置 预热结束时间
	 * 
	 * @param preheatEnddate
	 */
	public void setPreheatEnddate(Date preheatEnddate) {
		this.preheatEnddate = preheatEnddate;
	}

	/**
	 * 设置 活动平台
	 * 
	 * @param platform
	 */
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	/**
	 * 设置 title词
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 设置 meta-keyword
	 * 
	 * @param metaKeyword
	 */
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	/**
	 * 设置 meta-description
	 * 
	 * @param metaDescription
	 */
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	/**
	 * 设置 页面背景颜色
	 * 
	 * @param activityBackend
	 */
	public void setActivityBackend(String activityBackend) {
		this.activityBackend = activityBackend;
	}

	/**
	 * 设置 页面图片路径
	 * 
	 * @param activityPicSrc
	 */
	public void setActivityPicSrc(String activityPicSrc) {
		this.activityPicSrc = activityPicSrc;
	}

	/**
	 * 设置 备注
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 设置 状态(0正常，1删除)
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
	public void setCreater(Integer creater) {
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
	public void setModifier(Integer modifier) {
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
	 * 获取 活动名称
	 * 
	 * @return activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * 获取 活动上线时间
	 * 
	 * @return startdate
	 */
	public Date getStartdate() {
		return startdate;
	}

	/**
	 * 获取 活动下线时间
	 * 
	 * @return enddate
	 */
	public Date getEnddate() {
		return enddate;
	}

	/**
	 * 获取 是否预热(0代表否，1代表是)
	 * 
	 * @return ifPreheat
	 */
	public Integer getIfPreheat() {
		return ifPreheat;
	}

	/**
	 * 获取 预热开始时间
	 * 
	 * @return preheatStartdate
	 */
	public Date getPreheatStartdate() {
		return preheatStartdate;
	}

	/**
	 * 获取 预热结束时间
	 * 
	 * @return preheatEnddate
	 */
	public Date getPreheatEnddate() {
		return preheatEnddate;
	}

	/**
	 * 获取 活动平台
	 * 
	 * @return platform
	 */
	public Integer getPlatform() {
		return platform;
	}

	/**
	 * 获取 title词
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 获取 meta-keyword
	 * 
	 * @return metaKeyword
	 */
	public String getMetaKeyword() {
		return metaKeyword;
	}

	/**
	 * 获取 meta-description
	 * 
	 * @return metaDescription
	 */
	public String getMetaDescription() {
		return metaDescription;
	}

	/**
	 * 获取 页面背景颜色
	 * 
	 * @return activityBackend
	 */
	public String getActivityBackend() {
		return activityBackend;
	}

	/**
	 * 获取 页面图片路径
	 * 
	 * @return activityPicSrc
	 */
	public String getActivityPicSrc() {
		return activityPicSrc;
	}

	/**
	 * 获取 备注
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 获取 状态(0正常，1删除)
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
	public Integer getCreater() {
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
	public Integer getModifier() {
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

	public List<CmsStaticActivityTempletDTO> getList() {
		return list;
	}

	public void setList(List<CmsStaticActivityTempletDTO> list) {
		this.list = list;
	}

	public String getSpelActivityPicSrc() {
		return spelActivityPicSrc;
	}

	public void setSpelActivityPicSrc(String spelActivityPicSrc) {
		this.spelActivityPicSrc = spelActivityPicSrc;
	}

	public Long getPreheatId() {
		return preheatId;
	}

	public void setPreheatId(Long preheatId) {
		this.preheatId = preheatId;
	}

}