package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 自定义元素表
  */
public class DefinedElement extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**位置表主键 数据类型bigint(11)*/
	private Long positionId;
	
	/**名称 数据类型varchar(50)*/
	private String name;
	
	/**启用时间 数据类型datetime*/
	private Date startdate;
	
	/**失效时间 数据类型datetime*/
	private Date enddate;
	
	/**状态(0正常，1停用，2删除) 数据类型tinyint(2)*/
	private Integer status;
	
	/** 数据类型varchar(21000)*/
	private String content;
	
	/**创建人 数据类型bigint(10)*/
	private Long creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型bigint(11)*/
	private Long modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
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
	public Integer getStatus(){
		return status;
	}
	public String getContent(){
		return content;
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
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setContent(String content){
		this.content=content;
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
}
