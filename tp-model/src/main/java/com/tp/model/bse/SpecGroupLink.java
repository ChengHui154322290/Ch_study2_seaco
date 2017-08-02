package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 规格与规格组关系表
  */
public class SpecGroupLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786421L;

	/**主键ID 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**g规格组id 数据类型bigint(11)*/
	private Long groupId;
	
	/**规格id 数据类型bigint(11)*/
	private Long specId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**规格排序 数据类型int(11)*/
	private Integer sort;
	
	
	public Long getId(){
		return id;
	}
	public Long getGroupId(){
		return groupId;
	}
	public Long getSpecId(){
		return specId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getSort(){
		return sort;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}
	public void setSpecId(Long specId){
		this.specId=specId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setSort(Integer sort){
		this.sort=sort;
	}
}
