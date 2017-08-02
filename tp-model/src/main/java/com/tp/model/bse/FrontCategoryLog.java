package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 前台类目操作日志表
  */
public class FrontCategoryLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类目id 数据类型bigint(11)*/
	private Long categoryId;
	
	/** 数据类型bigint(11)*/
	private Long operId;
	
	/** 数据类型varchar(20)*/
	private String operName;
	
	/** 数据类型datetime*/
	private Date operTime;
	
	/** 数据类型varchar(200)*/
	private String operContent;
	
	/** 数据类型bigint(11)*/
	private Long createUserId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getOperId(){
		return operId;
	}
	public String getOperName(){
		return operName;
	}
	public Date getOperTime(){
		return operTime;
	}
	public String getOperContent(){
		return operContent;
	}
	public Long getCreateUserId(){
		return createUserId;
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
	public void setOperId(Long operId){
		this.operId=operId;
	}
	public void setOperName(String operName){
		this.operName=operName;
	}
	public void setOperTime(Date operTime){
		this.operTime=operTime;
	}
	public void setOperContent(String operContent){
		this.operContent=operContent;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
