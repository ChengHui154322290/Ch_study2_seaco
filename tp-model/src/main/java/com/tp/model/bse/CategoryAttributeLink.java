package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 类别-属性-中间表
  */
public class CategoryAttributeLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786416L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**小类id 数据类型bigint(11)*/
	private Long categoryId;
	
	/**属性项 id 数据类型bigint(11)*/
	private Long attributeId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getAttributeId(){
		return attributeId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setAttributeId(Long attributeId){
		this.attributeId=attributeId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
