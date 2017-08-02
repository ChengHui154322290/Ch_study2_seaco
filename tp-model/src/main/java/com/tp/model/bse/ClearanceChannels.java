package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 通关渠道信息
  */
public class ClearanceChannels extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786417L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**编号 数据类型varchar(255)*/
	private String code;
	
	/**通关渠道名称 数据类型varchar(255)*/
	private String name;
	
	/** 数据类型varchar(50)*/
	private String realName;
	
	/**通关状态 数据类型tinyint(4)*/
	private Integer status;
	
	/**通关渠道 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
	public String getRealName(){
		return realName;
	}
	public Integer getStatus(){
		return status;
	}
	public String getRemark(){
		return remark;
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
	public void setCode(String code){
		this.code=code;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setRealName(String realName){
		this.realName=realName;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
