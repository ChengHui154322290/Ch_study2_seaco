package com.tp.dto.stg;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;

public class InventoryManageDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1504341717988868755L;

	private Long id;
	
	/**sku编号 数据类型varchar(50)*/
	private String sku;
	
	/**现货库存总数量（包括坏品等） 数据类型int(11)*/
	private Integer inventory;
	
	/**占用库存总数量（即冻结库存） 数据类型int(11)*/
	private Integer occupy;
	
	/**现货库存总数量（包括坏品等） 数据类型int(11)*/
	private Integer reserveInventory;
	
	/**现货库存总数量（包括坏品等） 数据类型int(11)*/
	private Integer warnInventory;
	
	/**残次数量 数据类型int(11)*/
	private Integer reject;
	
	/**样品 数据类型int(11)*/
	private Integer sample;
	
	/**冻结状态 数据类型int(11)*/
	private Integer freeze;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**供应商id 数据类型bigint(20)*/
	private Long spId;
	
	/**地区id 数据类型bigint(20)*/
	private Long districtId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/** 供应商名称  */
	private String spName;
	/** 商品名称  */
	private String mainTitle;
	/** 条形码  */
	private String barcode;
	/** 仓库名称  */
	private String warehouseName;
	
	
	public Long getId() {
		return id;
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
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
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
	public Integer getReject() {
		return reject;
	}
	public void setReject(Integer reject) {
		this.reject = reject;
	}
	public Integer getSample() {
		return sample;
	}
	public void setSample(Integer sample) {
		this.sample = sample;
	}
	public Integer getFreeze() {
		return freeze;
	}
	public void setFreeze(Integer freeze) {
		this.freeze = freeze;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	

}
