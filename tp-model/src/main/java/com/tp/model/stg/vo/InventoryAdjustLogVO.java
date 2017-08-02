package com.tp.model.stg.vo;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;

public class InventoryAdjustLogVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2998890504770444075L;

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
	
	private String warehouseName;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getExportCount() {
		return exportCount;
	}
	public void setExportCount(Integer exportCount) {
		this.exportCount = exportCount;
	}
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
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}	
}
