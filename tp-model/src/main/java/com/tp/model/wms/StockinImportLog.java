package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库单导入日志表
  */
public class StockinImportLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473384017017L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**上传文件名称 数据类型varchar(60)*/
	private String realFileName;
	
	/**文件名(存储在文件服务器上的名称) 数据类型varchar(60)*/
	private String fileName;
	
	/**excel总行数 数据类型int(8)*/
	private Integer totalAmount;
	
	/**成功的行数 数据类型int(8)*/
	private Integer successCount;
	
	/**失败条数 数据类型int(8)*/
	private Integer failCount;
	
	/**文件密钥(MD5文件数据) 数据类型varchar(60)*/
	private String fileKey;
	
	/**上传token 数据类型varchar(60)*/
	private String uploadToken;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**导入时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getRealFileName(){
		return realFileName;
	}
	public String getFileName(){
		return fileName;
	}
	public Integer getTotalAmount(){
		return totalAmount;
	}
	public Integer getSuccessCount(){
		return successCount;
	}
	public Integer getFailCount(){
		return failCount;
	}
	public String getFileKey(){
		return fileKey;
	}
	public String getUploadToken(){
		return uploadToken;
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
	public void setRealFileName(String realFileName){
		this.realFileName=realFileName;
	}
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	public void setTotalAmount(Integer totalAmount){
		this.totalAmount=totalAmount;
	}
	public void setSuccessCount(Integer successCount){
		this.successCount=successCount;
	}
	public void setFailCount(Integer failCount){
		this.failCount=failCount;
	}
	public void setFileKey(String fileKey){
		this.fileKey=fileKey;
	}
	public void setUploadToken(String uploadToken){
		this.uploadToken=uploadToken;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
