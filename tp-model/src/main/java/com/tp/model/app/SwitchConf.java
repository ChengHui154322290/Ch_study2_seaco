package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 开关控制表
  */
public class SwitchConf extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型int(11)*/
	@Id
	private Integer id;
	
	/**开关控制的标签 默认为0 表示无标签 数据类型int(11)*/
	private Integer confKey;
	
	/**开关名称 数据类型varchar(20)*/
	private String confName;
	
	/**开关的值  0关1开  默认为0 数据类型tinyint(4)*/
	private Integer confValue;
	
	/**内容 数据类型text*/
	private String childContent;
	
	/**是否有效 0无效1有效  默认0 数据类型tinyint(4)*/
	private Integer isValid;
	
	/**逻辑删除  0正常1删除 默认为0 数据类型tinyint(4)*/
	private Integer isDel;
	
	/**操作用户 数据类型bigint(20)*/
	private Long optUserId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**备注 数据类型varchar(50)*/
	private String remark;
	
	/**平台：1、WAP独立 2.IOS嵌入 3.android嵌入 4.、WAP嵌入 5.IOS独立 6.android独立7、微信 数据类型tinyint(4)*/
	private Integer platform;
	
	/**版本 数据类型varchar(10)*/
	private String version;
	
	
	public Integer getId(){
		return id;
	}
	public Integer getConfKey(){
		return confKey;
	}
	public String getConfName(){
		return confName;
	}
	public Integer getConfValue(){
		return confValue;
	}
	public String getChildContent(){
		return childContent;
	}
	public Integer getIsValid(){
		return isValid;
	}
	public Integer getIsDel(){
		return isDel;
	}
	public Long getOptUserId(){
		return optUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getPlatform(){
		return platform;
	}
	public String getVersion(){
		return version;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setConfKey(Integer confKey){
		this.confKey=confKey;
	}
	public void setConfName(String confName){
		this.confName=confName;
	}
	public void setConfValue(Integer confValue){
		this.confValue=confValue;
	}
	public void setChildContent(String childContent){
		this.childContent=childContent;
	}
	public void setIsValid(Integer isValid){
		this.isValid=isValid;
	}
	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}
	public void setOptUserId(Long optUserId){
		this.optUserId=optUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setPlatform(Integer platform){
		this.platform=platform;
	}
	public void setVersion(String version){
		this.version=version;
	}
}
