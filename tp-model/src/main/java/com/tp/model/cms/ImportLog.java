package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 导入日志表
  */
public class ImportLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**上传文件名称 数据类型varchar(60)*/
	private String realFileName;
	
	/**文件名(存储在文件服务器上的名称) 数据类型varchar(60)*/
	private String fileName;
	
	/**密钥,保存上传时候加密的字符串 数据类型varchar(60)*/
	private String secretKey;
	
	/**创建人 数据类型bigint(20)*/
	private Long createUserId;
	
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
	public String getSecretKey(){
		return secretKey;
	}
	public Long getCreateUserId(){
		return createUserId;
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
	public void setSecretKey(String secretKey){
		this.secretKey=secretKey;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
