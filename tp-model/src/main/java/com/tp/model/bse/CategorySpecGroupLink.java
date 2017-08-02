package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  *  小类与规格关联表

  */
public class CategorySpecGroupLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786417L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**小类id 数据类型bigint(20)*/
	private Long categoryId;
	
	/**规格组id 数据类型bigint(20)*/
	private Long specGroupId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getSpecGroupId(){
		return specGroupId;
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
	public void setSpecGroupId(Long specGroupId){
		this.specGroupId=specGroupId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
