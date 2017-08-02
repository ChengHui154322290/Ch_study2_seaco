package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存信息表 记录sku的总库存、总销售占用库存信息
  */
public class InventorySnapshot extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**快照记录时间 数据类型datetime*/
	private Date snapTime;
	
	/**现货库存总数量（包括坏品等） 数据类型int(11)*/
	private Integer inventory;
	
	/**占用库存总数量（即冻结库存） 数据类型int(11)*/
	private Integer occupy;
	
	/**坏品数量 数据类型int(11)*/
	private Integer reject;
	
	/**sku编号 数据类型varchar(50)*/
	private String sku;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**地区id 数据类型bigint(20)*/
	private Long districtId;
	
	/**供应商id 0 表示自营的 数据类型bigint(20)*/
	private Long spId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/***仓库名称**/
	@Virtual
	private String wareHouseName;

	/****仓库地址*/
	@Virtual
	private String address;
	
	/**导出数量**/
	@Virtual
	private Integer exportCount;
	
	/** yyyy-mm-dd*/
	@Virtual
	private Date createBeginTime;

	/** yyyy-mm-dd*/
	@Virtual
	private Date createEndTime;
	
	public Integer getExportCount() {
		return exportCount;
	}
	public void setExportCount(Integer exportCount) {
		this.exportCount = exportCount;
	}
	public Long getId(){
		return id;
	}
	public Date getSnapTime(){
		return snapTime;
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
	public String getSku(){
		return sku;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public Long getSpId(){
		return spId;
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
	public void setSnapTime(Date snapTime){
		this.snapTime=snapTime;
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
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setSpId(Long spId){
		this.spId=spId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreateBeginTime() {
		return createBeginTime;
	}
	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}
	public Date getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}
	
}
