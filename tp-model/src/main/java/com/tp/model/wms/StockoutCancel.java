package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 取消配货表
  */
public class StockoutCancel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**配货单号 数据类型varchar(50)*/
	private String orderCode;
	
	/**取消配货数据 数据类型varchar(1000)*/
	private String reqMsg;
	
	/**取消时间 数据类型datetime*/
	private Date cancelTime;
	
	/**取消成功：0失败1成功 数据类型tinyint(1)*/
	private Integer success;
	
	/**错误信息 数据类型varchar(500)*/
	private String error;
	
	/**返回信息 数据类型varchar(1000)*/
	private String resMsg;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getReqMsg(){
		return reqMsg;
	}
	public Date getCancelTime(){
		return cancelTime;
	}
	public Integer getSuccess(){
		return success;
	}
	public String getError(){
		return error;
	}
	public String getResMsg(){
		return resMsg;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setReqMsg(String reqMsg){
		this.reqMsg=reqMsg;
	}
	public void setCancelTime(Date cancelTime){
		this.cancelTime=cancelTime;
	}
	public void setSuccess(Integer success){
		this.success=success;
	}
	public void setError(String error){
		this.error=error;
	}
	public void setResMsg(String resMsg){
		this.resMsg=resMsg;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
