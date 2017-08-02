package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 图片元素表
  */
public class PictureElement extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**位置表主键 数据类型bigint(11)*/
	private Long positionId;
	
	/**图片名称 数据类型varchar(50)*/
	private String name;
	
	/**启用时间 数据类型datetime*/
	private Date startdate;
	
	/**失效时间 数据类型datetime*/
	private Date enddate;
	
	/**图片地址 数据类型varchar(128)*/
	private String picSrc;
	
	/**状态(0正常，1停用，2删除) 数据类型tinyint(2)*/
	private Integer status;
	
	/**创建人 数据类型bigint(10)*/
	private Long creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型bigint(11)*/
	private Long modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**图片属性 数据类型varchar(50)*/
	private String attr;
	
	/**活动名称 数据类型varchar(50)*/
	private String actname;
	
	/**跳转链接 数据类型varchar(128)*/
	private String link;
	
	/**卷帘图片路径 数据类型varchar(128)*/
	private String rollpicsrc;
	
	/**sku 数据类型varchar(128)*/
	private String sku;
	
	/**活动id 数据类型bigint(11)*/
	private Long activityid;
	
	/**活动类型 数据类型varchar(20)*/
	private String acttype;
	
	
	public Long getId(){
		return id;
	}
	public Long getPositionId(){
		return positionId;
	}
	public String getName(){
		return name;
	}
	public Date getStartdate(){
		return startdate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public String getPicSrc(){
		return picSrc;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getModifier(){
		return modifier;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public String getAttr(){
		return attr;
	}
	public String getActname(){
		return actname;
	}
	public String getLink(){
		return link;
	}
	public String getRollpicsrc(){
		return rollpicsrc;
	}
	public String getSku(){
		return sku;
	}
	public Long getActivityid(){
		return activityid;
	}
	public String getActtype(){
		return acttype;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPositionId(Long positionId){
		this.positionId=positionId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}
	public void setPicSrc(String picSrc){
		this.picSrc=picSrc;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreater(Long creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifier(Long modifier){
		this.modifier=modifier;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setAttr(String attr){
		this.attr=attr;
	}
	public void setActname(String actname){
		this.actname=actname;
	}
	public void setLink(String link){
		this.link=link;
	}
	public void setRollpicsrc(String rollpicsrc){
		this.rollpicsrc=rollpicsrc;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setActivityid(Long activityid){
		this.activityid=activityid;
	}
	public void setActtype(String acttype){
		this.acttype=acttype;
	}
}
