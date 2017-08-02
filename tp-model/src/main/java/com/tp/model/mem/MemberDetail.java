package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class MemberDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**用户ID 数据类型bigint(20)*/
	private Long uid;
	
	/** 数据类型datetime*/
	private Date birthday;
	
	/**用户真实姓名 数据类型varchar(20)*/
	private String trueName;
	
	/**头像url 数据类型varchar(50)*/
	private String avatarUrl;
	
	/**实名验证照片a 数据类型varchar(200)*/
	private String picA;
	
	/**实名验证照片b 数据类型varchar(200)*/
	private String picB;
	
	/**是否实名验证 数据类型tinyint(5)*/
	private Boolean isCertificateCheck;
	
	/**实名验证审核状态 数据类型int(5)*/
	private Integer verifyStatus;
	
	/**注册平台 数据类型int(5)*/
	private Integer registryPlatform;
	
	/**目前常住地址 数据类型varchar(20)*/
	private String address;
	
	/**宝宝出生医院 数据类型varchar(100)*/
	private String babyBirthHospital;
	
	/**证件类型 数据类型int(5)*/
	private Integer certificateType;
	
	/**证件号 数据类型varchar(100)*/
	private String certificateValue;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型datetime*/
	private Date deleteTime;
	
	/**是否删除 数据类型tinyint(1)*/
	private Boolean isDelete;
	
	/**最后登录时间 数据类型datetime*/
	private Date lastLoginTime;
	
	/**最后登录ip 数据类型varchar(20)*/
	private String lastLoginIp;
	
	
	public Long getId(){
		return id;
	}
	public Long getUid(){
		return uid;
	}
	public Date getBirthday(){
		return birthday;
	}
	public String getTrueName(){
		return trueName;
	}
	public String getAvatarUrl(){
		return avatarUrl;
	}
	public String getPicA(){
		return picA;
	}
	public String getPicB(){
		return picB;
	}
	public Boolean getIsCertificateCheck(){
		return isCertificateCheck;
	}
	public Integer getVerifyStatus(){
		return verifyStatus;
	}
	public Integer getRegistryPlatform(){
		return registryPlatform;
	}
	public String getAddress(){
		return address;
	}
	public String getBabyBirthHospital(){
		return babyBirthHospital;
	}
	public Integer getCertificateType(){
		return certificateType;
	}
	public String getCertificateValue(){
		return certificateValue;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Date getDeleteTime(){
		return deleteTime;
	}
	public Boolean getIsDelete(){
		return isDelete;
	}
	public Date getLastLoginTime(){
		return lastLoginTime;
	}
	public String getLastLoginIp(){
		return lastLoginIp;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUid(Long uid){
		this.uid=uid;
	}
	public void setBirthday(Date birthday){
		this.birthday=birthday;
	}
	public void setTrueName(String trueName){
		this.trueName=trueName;
	}
	public void setAvatarUrl(String avatarUrl){
		this.avatarUrl=avatarUrl;
	}
	public void setPicA(String picA){
		this.picA=picA;
	}
	public void setPicB(String picB){
		this.picB=picB;
	}
	public void setIsCertificateCheck(Boolean isCertificateCheck){
		this.isCertificateCheck=isCertificateCheck;
	}
	public void setVerifyStatus(Integer verifyStatus){
		this.verifyStatus=verifyStatus;
	}
	public void setRegistryPlatform(Integer registryPlatform){
		this.registryPlatform=registryPlatform;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setBabyBirthHospital(String babyBirthHospital){
		this.babyBirthHospital=babyBirthHospital;
	}
	public void setCertificateType(Integer certificateType){
		this.certificateType=certificateType;
	}
	public void setCertificateValue(String certificateValue){
		this.certificateValue=certificateValue;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setDeleteTime(Date deleteTime){
		this.deleteTime=deleteTime;
	}
	public void setIsDelete(Boolean isDelete){
		this.isDelete=isDelete;
	}
	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime=lastLoginTime;
	}
	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp=lastLoginIp;
	}
}
