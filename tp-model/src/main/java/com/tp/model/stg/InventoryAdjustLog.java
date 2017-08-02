package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 盘盈盘亏操作日志
  */
public class InventoryAdjustLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**1：盘盈 2：盘亏 数据类型int(11)*/
	private Integer action;
	
	/** 数据类型int(11)*/
	private Integer quantity;
	
	/** 数据类型varchar(50)*/
	private String sku;
	
	/** 数据类型bigint(20)*/
	private Long inventoryId;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**调整原因 数据类型varchar(100)*/
	private String reason;
	
	/** 数据类型varchar(200)*/
	private String remark;
	
	/** 数据类型bigint(20)*/
	private Long createUserId;
	
	/** 数据类型datetime*/
	private Date createDate;
	
	/** 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/** 数据类型datetime*/
	private Date modifyDate;
	
	/**导出条数**/
	@Virtual
	private Integer exportCount;
	
	/**盘点开始时间**/
	@Virtual
	private String startDate;
	
	/**盘点结束时间**/
	@Virtual
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getExportCount() {
		return exportCount;
	}
	public void setExportCount(Integer exportCount) {
		this.exportCount = exportCount;
	}
	public Long getId(){
		return id;
	}
	public Integer getAction(){
		return action;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public String getSku(){
		return sku;
	}
	public Long getInventoryId(){
		return inventoryId;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getReason(){
		return reason;
	}
	public String getRemark(){
		return remark;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public Date getModifyDate(){
		return modifyDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAction(Integer action){
		this.action=action;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setInventoryId(Long inventoryId){
		this.inventoryId=inventoryId;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyDate(Date modifyDate){
		this.modifyDate=modifyDate;
	}
}
