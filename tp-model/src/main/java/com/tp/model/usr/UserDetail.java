package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class UserDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/**主键id 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(14)*/
	private Long userId;
	
	/**地址 数据类型varchar(200)*/
	private String address;
	
	/**邮编 数据类型varchar(10)*/
	private String zipCode;
	
	/**手机 数据类型varchar(20)*/
	private String mobile;
	
	/**email 数据类型varchar(100)*/
	private String email;
	
	/**传真 数据类型varchar(30)*/
	private String fax;
	
	/**qq 数据类型varchar(15)*/
	private String qq;
	
	/**固定电话 数据类型varchar(30)*/
	private String fixedPhone;
	
	/**0:女 1:男 数据类型int(1)*/
	private Integer sex;
	
	/**0:不可用 1:可用 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getAddress(){
		return address;
	}
	public String getZipCode(){
		return zipCode;
	}
	public String getMobile(){
		return mobile;
	}
	public String getEmail(){
		return email;
	}
	public String getFax(){
		return fax;
	}
	public String getQq(){
		return qq;
	}
	public String getFixedPhone(){
		return fixedPhone;
	}
	public Integer getSex(){
		return sex;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setFax(String fax){
		this.fax=fax;
	}
	public void setQq(String qq){
		this.qq=qq;
	}
	public void setFixedPhone(String fixedPhone){
		this.fixedPhone=fixedPhone;
	}
	public void setSex(Integer sex){
		this.sex=sex;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
