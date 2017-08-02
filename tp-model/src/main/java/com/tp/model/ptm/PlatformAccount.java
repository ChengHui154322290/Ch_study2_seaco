package com.tp.model.ptm;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 开放平台账户表
  */
public class PlatformAccount extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450411493006L;

	/**appkey 数据类型bigint(20)*/
	@Id
	private Long id;
	
	private String appkey;
	
	/**密码 数据类型varchar(45)*/
	private String token;
	
	/**对接方名称 数据类型varchar(50)*/
	private String name;
	
	/**账号状态（1＝有效，0＝无效，-1＝删除） 数据类型tinyint(3)*/
	private Integer status;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型bigint(20)*/
	private Long createUserId;
	
	/** 数据类型bigint(20)*/
	private Long modifyUserId;
	
	
	public Long getId(){
		return id;
	}
	public String getToken(){
		return token;
	}
	public String getName(){
		return name;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setToken(String token){
		this.token=token;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
}
