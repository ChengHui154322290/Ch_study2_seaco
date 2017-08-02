package com.tp.dto.stg.query;

import com.tp.model.BaseDO;

public class InventoryQuery extends BaseDO{
	
	private static final long serialVersionUID = 303604997667585138L;

	private Long id;
	
	private String sku;
	
	private Long spId;
	
	private Long warehouseId;
	/** 是否达到预警线 1是 2否 */
	private String warnFlag;
	
	private String spuName;
	
	private Integer maxUsableInventory;
	
	private Integer minUsableInventory;

	
	public Long getId() {
		return id;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public String getWarnFlag() {
		return warnFlag;
	}
	public void setWarnFlag(String warnFlag) {
		this.warnFlag = warnFlag;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getMaxUsableInventory() {
		return maxUsableInventory;
	}

	public void setMaxUsableInventory(Integer maxUsableInventory) {
		this.maxUsableInventory = maxUsableInventory;
	}

	public Integer getMinUsableInventory() {
		return minUsableInventory;
	}

	public void setMinUsableInventory(Integer minUsableInventory) {
		this.minUsableInventory = minUsableInventory;
	}
}
