package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 退款、补偿结算项表
  */
public class SettleRefundItemInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991707L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**订单编号 数据类型varchar(20)*/
	private String orderNo;
	
	/**退款编号 数据类型varchar(20)*/
	private String refundNo;
	
	/**状态 数据类型tinyint(2)*/
	private Integer status;
	
	/**SKU编号 数据类型varchar(20)*/
	private String skuCode;
	
	/**SKU名称 数据类型varchar(150)*/
	private String skuName;
	
	/**退款类型(0-退款，1-补偿） 数据类型tinyint(2)*/
	private Integer refundType;
	
	/**退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**结算编号 数据类型bigint(20)*/
	private Long settleNo;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	/**结算时间 数据类型datetime*/
	private Date settleDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public String getRefundNo(){
		return refundNo;
	}
	public Integer getStatus(){
		return status;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getSkuName(){
		return skuName;
	}
	public Integer getRefundType(){
		return refundType;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public Long getSettleNo(){
		return settleNo;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public Date getSettleDate(){
		return settleDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public void setRefundNo(String refundNo){
		this.refundNo=refundNo;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setSkuName(String skuName){
		this.skuName=skuName;
	}
	public void setRefundType(Integer refundType){
		this.refundType=refundType;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setSettleNo(Long settleNo){
		this.settleNo=settleNo;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public void setSettleDate(Date settleDate){
		this.settleDate=settleDate;
	}
}
