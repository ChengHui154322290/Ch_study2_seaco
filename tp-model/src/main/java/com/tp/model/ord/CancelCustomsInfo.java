package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 取消海淘单海关申报
  */
public class CancelCustomsInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1466047015329L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**取消单的ID 数据类型bigint(20)*/
	private Long cancelId;
	
	/**订单编号(子单) 数据类型bigint(20)*/
	private Long orderCode;
	
	/**海关编号 数据类型varchar(30)*/
	private String customsCode;
	
	/**运单号 数据类型varchar(50)*/
	private String expressNo;
	
	/**物流公司编号 数据类型varchar(30)*/
	private String expressCompanyCode;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(50)*/
	private String supplierName;
	
	/**取消单状态 数据类型tinyint(11)*/
	private Integer cancelStatus;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCancelId(){
		return cancelId;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getCustomsCode(){
		return customsCode;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public String getExpressCompanyCode(){
		return expressCompanyCode;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Integer getCancelStatus(){
		return cancelStatus;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCancelId(Long cancelId){
		this.cancelId=cancelId;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setCustomsCode(String customsCode){
		this.customsCode=customsCode;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setExpressCompanyCode(String expressCompanyCode){
		this.expressCompanyCode=expressCompanyCode;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setCancelStatus(Integer cancelStatus){
		this.cancelStatus=cancelStatus;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
