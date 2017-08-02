package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author zhouguofeng
  * 广告统计
  */
public class AdvertPlatform extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1483508352040L;

	/**日志ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**平台名称 数据类型varchar(128)*/
	private String advertPlatName;
	
	/**平台编码 数据类型varchar(64)*/
	private String advertPlatCode;
	
	/**点击总数 数据类型int(10)*/
	private Integer clickNum;
	
	/**注册总数 数据类型int(10)*/
	private Integer registerNum;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public String getAdvertPlatName(){
		return advertPlatName;
	}
	public String getAdvertPlatCode(){
		return advertPlatCode;
	}
	public Integer getClickNum(){
		return clickNum;
	}
	public Integer getRegisterNum(){
		return registerNum;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAdvertPlatName(String advertPlatName){
		this.advertPlatName=advertPlatName;
	}
	public void setAdvertPlatCode(String advertPlatCode){
		this.advertPlatCode=advertPlatCode;
	}
	public void setClickNum(Integer clickNum){
		this.clickNum=clickNum;
	}
	public void setRegisterNum(Integer registerNum){
		this.registerNum=registerNum;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
