package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  *  二维码扫描统计
  */
public class StatisticQrcode extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**二维码来源 数据类型varchar(255)*/
	private String qrSource;
	
	/**扫描结果 数据类型varchar(255)*/
	private String qrResult;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getQrSource(){
		return qrSource;
	}
	public String getQrResult(){
		return qrResult;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setQrSource(String qrSource){
		this.qrSource=qrSource;
	}
	public void setQrResult(String qrResult){
		this.qrResult=qrResult;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
