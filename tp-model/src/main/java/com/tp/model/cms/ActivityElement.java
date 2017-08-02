package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动元素表
  */
public class ActivityElement extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451847L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**位置表主键 数据类型bigint(11)*/
	private Long positionId;
	
	/**活动id 数据类型bigint(11)*/
	private Long activityId;
	
	/**启用时间 数据类型datetime*/
	private Date startdate;
	
	/**失效时间 数据类型datetime*/
	private Date enddate;
	
	/**展示图片(图片地址src) 数据类型varchar(128)*/
	private String picture;
	
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
	
	/**跳转链接 数据类型varchar(128)*/
	private String link;
	
	/** 顺序  **/
	private Integer seq;
	
	/** 主题是否显示 0:不显示 1:显示  **/
	private Integer showTopic;
	
	
	
	public Integer getShowTopic() {
		return showTopic;
	}
	public void setShowTopic(Integer showTopic) {
		this.showTopic = showTopic;
	}
	public Long getId(){
		return id;
	}
	public Long getPositionId(){
		return positionId;
	}
	public Long getActivityId(){
		return activityId;
	}
	public Date getStartdate(){
		return startdate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public String getPicture(){
		return picture;
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
	public String getLink(){
		return link;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPositionId(Long positionId){
		this.positionId=positionId;
	}
	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}
	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}
	public void setPicture(String picture){
		this.picture=picture;
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
	public void setLink(String link){
		this.link=link;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
