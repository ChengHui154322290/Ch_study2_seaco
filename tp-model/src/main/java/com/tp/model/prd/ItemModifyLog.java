package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品修改日志
  */
public class ItemModifyLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**商品skuId 数据类型bigint(18)*/
	private Long skuId;
	
	/**变化数量 数据类型int(11)*/
	private Integer qutity;
	
	/**操作类型:1-新增商品，2-修改商品，默认1 数据类型int(1)*/
	private Integer status;
	
	/**商品被修改前的信息 数据类型varchar(4000)*/
	private String lastContent;
	
	/**商品被修改后的信息 数据类型varchar(4000)*/
	private String content;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getSkuId(){
		return skuId;
	}
	public Integer getQutity(){
		return qutity;
	}
	public Integer getStatus(){
		return status;
	}
	public String getLastContent(){
		return lastContent;
	}
	public String getContent(){
		return content;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSkuId(Long skuId){
		this.skuId=skuId;
	}
	public void setQutity(Integer qutity){
		this.qutity=qutity;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setLastContent(String lastContent){
		this.lastContent=lastContent;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
