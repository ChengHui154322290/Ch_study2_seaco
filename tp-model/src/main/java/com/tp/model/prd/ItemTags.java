package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品标签信息表，仅做新增和删除，不做修改
  */
public class ItemTags extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**标签名称 数据类型varchar(20)*/
	private String name;
	
	/**商品prdid信息id 数据类型bigint(20)*/
	private Long detailId;
	
	/**商品prdid 数据类型varchar(50)*/
	private String prdid;
	
	/**商品spu 数据类型varchar(50)*/
	private String spu;
	
	/** 数据类型tinyint(5)*/
	private Integer sortNo;
	
	/**创建人id 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public Long getDetailId(){
		return detailId;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getSpu(){
		return spu;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
