package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-大文件上传记录表(如产品质量检验合格证明或质量检测报告)
  */
public class SupplierUploadLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274737L;

	/**主键 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**文件路径 数据类型varchar(100)*/
	private String filePath;
	
	/**文件类型 数据类型varchar(45)*/
	private String fileType;
	
	/**文件大小（单位KB） 数据类型varchar(10)*/
	private String fileSize;
	
	/**是否使用(1:使用 0:没用) 数据类型tinyint(1)*/
	private Integer isUsed;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getFilePath(){
		return filePath;
	}
	public String getFileType(){
		return fileType;
	}
	public String getFileSize(){
		return fileSize;
	}
	public Integer getIsUsed(){
		return isUsed;
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
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setFilePath(String filePath){
		this.filePath=filePath;
	}
	public void setFileType(String fileType){
		this.fileType=fileType;
	}
	public void setFileSize(String fileSize){
		this.fileSize=fileSize;
	}
	public void setIsUsed(Integer isUsed){
		this.isUsed=isUsed;
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
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
