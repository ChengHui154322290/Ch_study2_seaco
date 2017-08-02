package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品导入日志表
  */
public class ItemImportLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698778L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**上传文件名称 数据类型varchar(60)*/
	private String realFileName;
	
	/**文件名(存储在文件服务器上的名称) 数据类型varchar(60)*/
	private String fileName;
	
	/**excel总行数 数据类型int(8)*/
	private Integer sumCount;
	
	/**成功的行数 数据类型int(8)*/
	private Integer successCount;
	
	/**失败条数 数据类型int(8)*/
	private Integer failCount;
	
	/**导入日志状态:1-解析excel,2-验证excel,3-插入sku,4-记录日志 数据类型int(1)*/
	private Integer status;
	
	/**逻辑删除标志：1-不删除，0-删除，默认1 数据类型int(1)*/
	private Integer deleteSign;
	
	/**密钥,保存上传时候加密的字符串 数据类型varchar(60)*/
	private String secretKey;
	
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
	public Integer getSumCount(){
		return sumCount;
	}
	public Integer getSuccessCount(){
		return successCount;
	}
	public Integer getFailCount(){
		return failCount;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getDeleteSign(){
		return deleteSign;
	}
	public String getSecretKey(){
		return secretKey;
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
	public void setSumCount(Integer sumCount){
		this.sumCount=sumCount;
	}
	public void setSuccessCount(Integer successCount){
		this.successCount=successCount;
	}
	public void setFailCount(Integer failCount){
		this.failCount=failCount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setDeleteSign(Integer deleteSign){
		this.deleteSign=deleteSign;
	}
	public void setSecretKey(String secretKey){
		this.secretKey=secretKey;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
