package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 品类与标签关联信息表
  */
public class CategoryTagsLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786417L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**分类id 数据类型bigint(20)*/
	private Long categoryId;
	
	/**标签id 数据类型bigint(20)*/
	private Long tagsId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**创建人id 数据类型bigint(20)*/
	private Long createUserId;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getTagsId(){
		return tagsId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setTagsId(Long tagsId){
		this.tagsId=tagsId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
}
