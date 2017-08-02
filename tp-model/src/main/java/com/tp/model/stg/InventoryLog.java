package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.StorageConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出入库流水信息表
  */
public class InventoryLog extends BaseDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4437913020788901498L;

	/**主键 数据类型bigint(11) unsigned*/
	@Id
	private Long id;
	
	/**仓库流水单号 数据类型varchar(50)*/
	private String whCode;
	
	/**订单号（商品、采购、采购退货） 数据类型varchar(20)*/
	private String orderCode;
	
	/**运单号 数据类型varchar(100)*/
	private String shipCode;
	
	/**sku 数据类型varchar(20)*/
	private String sku;
	
	/**条形码 数据类型varchar(20)*/
	private String barcode;
	
	/**sku数量 数据类型int(11)*/
	private Integer skuCount;
	
	/**剩余库存数量 数据类型int(11)*/
	private Integer inventory;
	
	/**分类 数据类型varchar(20)*/
	private String type;
	
	/**区域，存储到政区级别 数据类型bigint(11)*/
	private Long districtId;
	
	/**批次号 数据类型varchar(100)*/
	private String batchNo;
	
	/**快递公司id 数据类型bigint(20)*/
	private Long expressId;
	
	/**快递公司名称 数据类型varchar(100)*/
	private String expressName;
	
	/**供应商id 数据类型bigint(20)*/
	private Long supplierId;
	
	/**仓库id 数据类型bigint(11)*/
	private Long warehouseId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**导出数量**/
	@Virtual
	private  Integer exportCount;
	
	@Virtual
	private String wareHouseName;
	
	@Virtual
	private Date createBeginTime;

	@Virtual
	private Date createEndTime;
	
	@Virtual
	private String address;
	
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
	public Integer getExportCount() {
		return exportCount;
	}
	public void setExportCount(Integer exportCount) {
		this.exportCount = exportCount;
	}
	public Long getId(){
		return id;
	}
	public String getWhCode(){
		return whCode;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getShipCode(){
		return shipCode;
	}
	public String getSku(){
		return sku;
	}
	public String getBarcode(){
		return barcode;
	}
	public Integer getSkuCount(){
		return skuCount;
	}
	public Integer getInventory(){
		return inventory;
	}
	public String getType(){
		return type;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public String getBatchNo(){
		return batchNo;
	}
	public Long getExpressId(){
		return expressId;
	}
	public String getExpressName(){
		return expressName;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setWhCode(String whCode){
		this.whCode=whCode;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setShipCode(String shipCode){
		this.shipCode=shipCode;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setSkuCount(Integer skuCount){
		this.skuCount=skuCount;
	}
	public void setInventory(Integer inventory){
		this.inventory=inventory;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}
	public void setExpressId(Long expressId){
		this.expressId=expressId;
	}
	public void setExpressName(String expressName){
		this.expressName=expressName;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
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
	public String getTypeDesc(){
		return StorageConstant.InputAndOutputType.getDescByCode(this.getType());
	}
	
	public Boolean getTypeAddStatus(){
		if(StringUtils.isBlank(this.getType())){
			return false;
		}
		StorageConstant.InputAndOutputType andOutputType = StorageConstant.InputAndOutputType.valueOf(this.getType());
		return andOutputType.isAddInventory();
	}
}
