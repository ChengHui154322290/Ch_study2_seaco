package com.tp.model.stg;



import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存信息表 记录sku的总库存、总销售占用库存信息
  */
public class InventoryManageLog extends BaseDO implements Serializable {
	
	

	public InventoryManageLog(){};
	public InventoryManageLog(InventoryManageDto inventoryObj) {
		super();
		this.inventoryId = inventoryObj.getId();
		this.sku = inventoryObj.getSku();
		this.inventory = inventoryObj.getInventory();
		this.occupy = inventoryObj.getOccupy();
		this.reserveInventory = inventoryObj.getReserveInventory();
		this.warnInventory = inventoryObj.getWarnInventory();
		this.reject = inventoryObj.getReject();
		this.sample = inventoryObj.getSample();
		this.freeze = inventoryObj.getFreeze();
		this.warehouseId = inventoryObj.getWarehouseId();
		this.spId = inventoryObj.getSpId();
		this.districtId = inventoryObj.getDistrictId();
		this.mainTitle = inventoryObj.getMainTitle();
		this.warehouseName = inventoryObj.getWarehouseName();
		this.spName = inventoryObj.getSpName();
	}
	
	public InventoryManageLog(Inventory inventoryObj,String mainTitle,String warehouseName,String spName) {
		super();
		this.inventoryId = inventoryObj.getId();
		this.sku = inventoryObj.getSku();
		this.inventory = inventoryObj.getInventory();
		this.occupy = 0;
		this.reserveInventory = inventoryObj.getReserveInventory();
		this.warnInventory = inventoryObj.getWarnInventory();
		this.reject = 0;
		this.sample = 0;
		this.freeze = 0;
		this.warehouseId = inventoryObj.getWarehouseId();
		this.spId = inventoryObj.getSpId();
		this.districtId = inventoryObj.getDistrictId();
		this.mainTitle = mainTitle;
		this.warehouseName = warehouseName;
		this.spName = spName;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4135302851258975401L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	private Long inventoryId;
	
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
	

	
	private String mainTitle;
	
	private String warehouseName;
	
	private String spName;
	private String createUser;
	/**更改标志：1：新增，2：修改；3：删除*/
	private Integer changeType;//
	
	//备注
	private String remark;
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	
	
}
