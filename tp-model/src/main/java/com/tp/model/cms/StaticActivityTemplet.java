package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动模块表
  */
public class StaticActivityTemplet extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**活动页面表主键 数据类型bigint(11)*/
	private Long activityId;
	
	/**排序 数据类型int(4)*/
	private Integer sort;
	
	/**状态(0正常，1预热,2删除) 数据类型int(1)*/
	private Integer status;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型int(10)*/
	private Integer modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**模板id 数据类型bigint(11)*/
	private Long templetId;
	
	
	public Long getId(){
		return id;
	}
	public Long getActivityId(){
		return activityId;
	}
	public Integer getSort(){
		return sort;
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
	public Long getTempletId(){
		return templetId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}
	public void setSort(Integer sort){
		this.sort=sort;
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
	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}
}
