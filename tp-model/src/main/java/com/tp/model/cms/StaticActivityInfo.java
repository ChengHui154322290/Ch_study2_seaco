package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动页面表
  */
public class StaticActivityInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**活动名称 数据类型varchar(50)*/
	private String activityName;
	
	/**活动上线时间 数据类型datetime*/
	private Date startdate;
	
	/**活动下线时间 数据类型datetime*/
	private Date enddate;
	
	/**是否预热(0代表否，1代表是) 数据类型int(1)*/
	private Integer ifPreheat;
	
	/**预热开始时间 数据类型datetime*/
	private Date preheatStartdate;
	
	/**预热结束时间 数据类型datetime*/
	private Date preheatEnddate;
	
	/**活动平台 数据类型int(2)*/
	private Integer platform;
	
	/**title词 数据类型varchar(128)*/
	private String title;
	
	/**meta-keyword 数据类型varchar(128)*/
	private String metaKeyword;
	
	/**meta-description 数据类型varchar(128)*/
	private String metaDescription;
	
	/**页面背景颜色 数据类型varchar(50)*/
	private String activityBackend;
	
	/**页面图片路径 数据类型varchar(128)*/
	private String activityPicSrc;
	
	/**备注 数据类型varchar(3000)*/
	private String remark;
	
	/**状态(0正常，1删除) 数据类型int(1)*/
	private Integer status;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型int(10)*/
	private Integer modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**预热页面的id 数据类型bigint(11)*/
	private Long preheatId;
	
	
	public Long getId(){
		return id;
	}
	public String getActivityName(){
		return activityName;
	}
	public Date getStartdate(){
		return startdate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public Integer getIfPreheat(){
		return ifPreheat;
	}
	public Date getPreheatStartdate(){
		return preheatStartdate;
	}
	public Date getPreheatEnddate(){
		return preheatEnddate;
	}
	public Integer getPlatform(){
		return platform;
	}
	public String getTitle(){
		return title;
	}
	public String getMetaKeyword(){
		return metaKeyword;
	}
	public String getMetaDescription(){
		return metaDescription;
	}
	public String getActivityBackend(){
		return activityBackend;
	}
	public String getActivityPicSrc(){
		return activityPicSrc;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Integer getModifier(){
		return modifier;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Long getPreheatId(){
		return preheatId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setActivityName(String activityName){
		this.activityName=activityName;
	}
	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}
	public void setIfPreheat(Integer ifPreheat){
		this.ifPreheat=ifPreheat;
	}
	public void setPreheatStartdate(Date preheatStartdate){
		this.preheatStartdate=preheatStartdate;
	}
	public void setPreheatEnddate(Date preheatEnddate){
		this.preheatEnddate=preheatEnddate;
	}
	public void setPlatform(Integer platform){
		this.platform=platform;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setMetaKeyword(String metaKeyword){
		this.metaKeyword=metaKeyword;
	}
	public void setMetaDescription(String metaDescription){
		this.metaDescription=metaDescription;
	}
	public void setActivityBackend(String activityBackend){
		this.activityBackend=activityBackend;
	}
	public void setActivityPicSrc(String activityPicSrc){
		this.activityPicSrc=activityPicSrc;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreater(Integer creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifier(Integer modifier){
		this.modifier=modifier;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setPreheatId(Long preheatId){
		this.preheatId=preheatId;
	}
}
