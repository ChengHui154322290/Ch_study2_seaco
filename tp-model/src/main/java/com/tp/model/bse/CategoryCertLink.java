package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 经营品类所需要的资质
  */
public class CategoryCertLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690111L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long categoryId;
	
	/** 数据类型bigint(20)*/
	private Long dictionaryInfoId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getDictionaryInfoId(){
		return dictionaryInfoId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setDictionaryInfoId(Long dictionaryInfoId){
		this.dictionaryInfoId=dictionaryInfoId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
