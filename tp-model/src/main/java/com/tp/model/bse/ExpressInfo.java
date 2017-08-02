package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 快递公司信息表
  */
public class ExpressInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786419L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**公司名称 数据类型varchar(50)*/
	private String name;
	
	/**编号 数据类型varchar(50)*/
	private String code;
	
	/**排序 数据类型int(11)*/
	private Integer sortNo;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getCode(){
		return code;
	}
	public Integer getSortNo(){
		return sortNo;
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
	public void setName(String name){
		this.name=name;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
