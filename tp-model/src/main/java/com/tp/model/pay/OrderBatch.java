package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单合并支付表(业务类型为订单)
  */
public class OrderBatch extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638869L;

	/**批次号 数据类型int(11)*/
	@Id
	private Integer batchId;
	
	/**多个订单号，以,分割(最多10单) 数据类型varchar(200)*/
	private String ordersNo;
	
	/**流水号 数据类型varchar(32)*/
	private String serial;
	
	/**订单合并金额 数据类型double(12,2)*/
	private Double amount;
	
	/**网关交易流水号 数据类型varchar(32)*/
	private String gatewayTradeNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间（回调时间） 数据类型datetime*/
	private Date updateTime;
	
	
	public Integer getBatchId(){
		return batchId;
	}
	public String getOrdersNo(){
		return ordersNo;
	}
	public String getSerial(){
		return serial;
	}
	public Double getAmount(){
		return amount;
	}
	public String getGatewayTradeNo(){
		return gatewayTradeNo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setBatchId(Integer batchId){
		this.batchId=batchId;
	}
	public void setOrdersNo(String ordersNo){
		this.ordersNo=ordersNo;
	}
	public void setSerial(String serial){
		this.serial=serial;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setGatewayTradeNo(String gatewayTradeNo){
		this.gatewayTradeNo=gatewayTradeNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
