package com.tp.dto.stg;

import java.io.Serializable;

public class InventoryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1666721459436359652L;
	
	private Long id;
	/** 仓库id */
	private Long warehouseId;
	/** 仓库名称 */
	private String warehouseName;
	/** 仓库编号 */
	private String warehouseCode;
	/** 地区id */
	private Long districtId;
	/** 可销售库存 = 总库存 - 预留库存 - 占用库存；其中：占用库存=特殊活动分配库存总数+订单占用(非特殊活动订单占用库存)*/
	private Integer inventory;
	/** 现货库存总数量（总库存）= 预留库存+可销售库存+占用库存*/
	private Integer realInventory;

	/** 占用库存总数量 = 非特殊活动订单占用库存 + 特殊活动限购总量 */
	private Integer occupy;
	
	/** 预留库存 */
	private Integer reserveInventory;
	
	/** 预警库存 */
	private Integer warnInventory;
	
	/** 供应商ID*/
	private Long spId;
	/** 供应商名称*/
	private String spName;
	/** 商品名称*/
	private String mainTitle;
	/** 条形码*/
	private String barcode;
	
	private String sku;
	
	
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getRealInventory() {
		return realInventory;
	}
	public void setRealInventory(Integer realInventory) {
		this.realInventory = realInventory;
	}
	
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getOccupy() {
		return occupy;
	}
	public void setOccupy(Integer occupy) {
		this.occupy = occupy;
	}
	public Integer getReserveInventory() {
		return reserveInventory;
	}
	public void setReserveInventory(Integer reserveInventory) {
		this.reserveInventory = reserveInventory;
	}
	public Integer getWarnInventory() {
		return warnInventory;
	}
	public void setWarnInventory(Integer warnInventory) {
		this.warnInventory = warnInventory;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
