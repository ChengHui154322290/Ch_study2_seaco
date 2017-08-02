package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库订单反馈
  */
public class OutputBack extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**即订单编号，也对应出库订单里的system_id 数据类型varchar(50)*/
	private String orderNo;
	
	/**运单号 数据类型varchar(50)*/
	private String shipNo;
	
	/**发运时间 数据类型datetime*/
	private Date shipTime;
	
	/**发送的物流公司名 数据类型varchar(10)*/
	private String carrierId;
	
	/**发送的物流公司名 数据类型varchar(15)*/
	private String carrierName;
	
	/**客户id 数据类型varchar(15)*/
	private String customerId;
	
	/**对应仓库的id号 数据类型varchar(15)*/
	private String bgNo;
	
	/**仓库id 数据类型bigint(20)*/
	private Long varehouseId;
	
	/**订单包裹重量 数据类型double*/
	private Double weight;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public String getShipNo(){
		return shipNo;
	}
	public Date getShipTime(){
		return shipTime;
	}
	public String getCarrierId(){
		return carrierId;
	}
	public String getCarrierName(){
		return carrierName;
	}
	public String getCustomerId(){
		return customerId;
	}
	public String getBgNo(){
		return bgNo;
	}
	public Long getVarehouseId(){
		return varehouseId;
	}
	public Double getWeight(){
		return weight;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public void setShipNo(String shipNo){
		this.shipNo=shipNo;
	}
	public void setShipTime(Date shipTime){
		this.shipTime=shipTime;
	}
	public void setCarrierId(String carrierId){
		this.carrierId=carrierId;
	}
	public void setCarrierName(String carrierName){
		this.carrierName=carrierName;
	}
	public void setCustomerId(String customerId){
		this.customerId=customerId;
	}
	public void setBgNo(String bgNo){
		this.bgNo=bgNo;
	}
	public void setVarehouseId(Long varehouseId){
		this.varehouseId=varehouseId;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
