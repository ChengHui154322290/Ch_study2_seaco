package com.tp.dto.stg;


import java.io.Serializable;

import com.tp.common.vo.StorageConstant.App;


/**
 * 库存申请
 * @author szy

 */
public class InventoryApply implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5155352652674468026L;

	/** 活动类型 （必填）*/
	private App app;
	
	/** 活动ID （必填）*/
	private String bizId;
	
	/** 供应商ID */
	private Long spId;
	 
	/** 待申请库存 （必填）*/
	private Integer inventory;

	/** 商品SKU （必填）*/
	private String sku;
	
	/** 仓库ID （必填）*/
	private Long warehouseId;
	
	/** 预占库存 true预留库存 false不预留库存 */
	private boolean isPreOccupy;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public boolean isPreOccupy() {
		return isPreOccupy;
	}

	public void setPreOccupy(boolean isPreOccupy) {
		this.isPreOccupy = isPreOccupy;
	}
}
