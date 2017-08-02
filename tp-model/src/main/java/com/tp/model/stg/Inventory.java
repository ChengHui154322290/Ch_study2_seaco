package com.tp.model.stg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存信息表 记录sku的总库存、总销售占用库存信息
  */

public class Inventory extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**sku编号 数据类型varchar(50)*/
	private String sku;
	
	/**现货库存总数量（包括坏品等） 数据类型int(11)*/
	private Integer inventory;
	
	/**占用库存总数量（即冻结库存） 数据类型int(11)*/
	private Integer occupy;
	
	/** 预留库存 数据类型int(11)*/
	private Integer reserveInventory;
	
	/** 预警库存 数据类型int(11)*/
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
	
	@Virtual
	private String warehouseName;
	@Virtual
	private String warehouseCode;
	
	@Virtual
	private List<InventoryDistribute> inventoryDistributes;
	
	public List<InventoryDistribute> getInventoryDistributes() {
		return inventoryDistributes;
	}

	public void setInventoryDistributes(List<InventoryDistribute> inventoryDistributes) {
		this.inventoryDistributes = inventoryDistributes;
	}

	public Integer getAvailableInventory(){
		if(reserveInventory==null){
			return inventory;
		}
		return inventory - reserveInventory;
	}
	
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Long getId(){
		return id;
	}
	public String getSku(){
		return sku;
	}
	public Integer getInventory(){
		return inventory;
	}
	public Integer getOccupy(){
		return occupy;
	}
	public Integer getReject(){
		return reject;
	}
	public Integer getSample(){
		return sample;
	}
	public Integer getFreeze(){
		return freeze;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Long getSpId(){
		return spId;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setInventory(Integer inventory){
		this.inventory=inventory;
	}
	public void setOccupy(Integer occupy){
		this.occupy=occupy;
	}
	public void setReject(Integer reject){
		this.reject=reject;
	}
	public void setSample(Integer sample){
		this.sample=sample;
	}
	public void setFreeze(Integer freeze){
		this.freeze=freeze;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setSpId(Long spId){
		this.spId=spId;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
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

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	
}
