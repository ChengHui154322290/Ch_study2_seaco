package com.tp.model.sup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 采购[代销]订单[退货单]主表
  */
public class PurchaseInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836567624L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**订[退货]单号 数据类型varchar(100)*/
	private String purchaseCode;
	
	/**订[退]单日期 数据类型datetime*/
	private Date purchaseDate;
	
	/**订单类型(采购订单,采购退货单,代销订单,代销退货单) 数据类型varchar(45)*/
	private String purchaseType;
	
	/**订单类型：一般订单、紧急订单 数据类型varchar(45)*/
	private String purchaseTypeLevel;
	
	/**备注 数据类型varchar(255)*/
	private String purchaseDesc;
	
	/**批号 数据类型varchar(100)*/
	private String batchNumber;
	
	/**是否已入库(0:未入库,1:已入库) 数据类型tinyint(1)*/
	private Integer hasStorage;
	
	/**订单是否确认(0:未确认,1:已确认) 数据类型tinyint(1)*/
	private Integer isConfirm;
	
	/**订单确认时间 数据类型datetime*/
	private Date confirmDate;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**仓库id 数据类型bigint(11)*/
	private Long warehouseId;
	
	/**仓库名称 数据类型varchar(80)*/
	private String warehouseName;
	
	/**税率(%) 数据类型double*/
	private Double rate;
	
	/**汇率(CNY) 数据类型double*/
	private Double exchangeRate;
	
	/**币别 数据类型varchar(45)*/
	private String currency;
	
	/**期望日期 数据类型datetime*/
	private Date expectDate;
	
	/**可用数量 数据类型bigint(11)*/
	private Long totalAvailable;
	
	/**数量合计 数据类型bigint(11)*/
	private Long totalCount;
	
	/**金额总计 数据类型double*/
	private Double totalMoney;
	
	/**入[出]库总数 数据类型bigint(11)*/
	private Long totalStorage;
	
	/**实际库存(退货单) 数据类型bigint(11)*/
	private Long totalRealStorage;
	
	/**退货数量(退货单) 数据类型bigint(11)*/
	private Long totalReturn;
	
	/**退货金额(退货单) 数据类型double*/
	private Double totalMoneyReturn;
	
	/**出库数量(退货单) 数据类型bigint(11)*/
	private Long totalReturnStorage;
	
	/**可退数量 数据类型bigint(11)*/
	private Long totalRefundable;
	
	/**收货状态 数据类型varchar(45)*/
	private String receiveStatus;
	
	/**订单状态 数据类型int(8)*/
	private Integer auditStatus;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(32)*/
	private String updateUser;
	
	@Virtual
	private List<PurchaseProduct> purchaseProductList = new ArrayList<PurchaseProduct>();
	@Virtual
	private Date startTime;
	@Virtual
	private Date endTime;
	
	public Long getId(){
		return id;
	}
	public String getPurchaseCode(){
		return purchaseCode;
	}
	public Date getPurchaseDate(){
		return purchaseDate;
	}
	public String getPurchaseType(){
		return purchaseType;
	}
	public String getPurchaseTypeLevel(){
		return purchaseTypeLevel;
	}
	public String getPurchaseDesc(){
		return purchaseDesc;
	}
	public String getBatchNumber(){
		return batchNumber;
	}
	public Integer getHasStorage(){
		return hasStorage;
	}
	public Integer getIsConfirm(){
		return isConfirm;
	}
	public Date getConfirmDate(){
		return confirmDate;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseName(){
		return warehouseName;
	}
	public Double getRate(){
		return rate;
	}
	public Double getExchangeRate(){
		return exchangeRate;
	}
	public String getCurrency(){
		return currency;
	}
	public Date getExpectDate(){
		return expectDate;
	}
	public Long getTotalAvailable(){
		return totalAvailable;
	}
	public Long getTotalCount(){
		return totalCount;
	}
	public Double getTotalMoney(){
		return totalMoney;
	}
	public Long getTotalStorage(){
		return totalStorage;
	}
	public Long getTotalRealStorage(){
		return totalRealStorage;
	}
	public Long getTotalReturn(){
		return totalReturn;
	}
	public Double getTotalMoneyReturn(){
		return totalMoneyReturn;
	}
	public Long getTotalReturnStorage(){
		return totalReturnStorage;
	}
	public Long getTotalRefundable(){
		return totalRefundable;
	}
	public String getReceiveStatus(){
		return receiveStatus;
	}
	public Integer getAuditStatus(){
		return auditStatus;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPurchaseCode(String purchaseCode){
		this.purchaseCode=purchaseCode;
	}
	public void setPurchaseDate(Date purchaseDate){
		this.purchaseDate=purchaseDate;
	}
	public void setPurchaseType(String purchaseType){
		this.purchaseType=purchaseType;
	}
	public void setPurchaseTypeLevel(String purchaseTypeLevel){
		this.purchaseTypeLevel=purchaseTypeLevel;
	}
	public void setPurchaseDesc(String purchaseDesc){
		this.purchaseDesc=purchaseDesc;
	}
	public void setBatchNumber(String batchNumber){
		this.batchNumber=batchNumber;
	}
	public void setHasStorage(Integer hasStorage){
		this.hasStorage=hasStorage;
	}
	public void setIsConfirm(Integer isConfirm){
		this.isConfirm=isConfirm;
	}
	public void setConfirmDate(Date confirmDate){
		this.confirmDate=confirmDate;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}
	public void setRate(Double rate){
		this.rate=rate;
	}
	public void setExchangeRate(Double exchangeRate){
		this.exchangeRate=exchangeRate;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setExpectDate(Date expectDate){
		this.expectDate=expectDate;
	}
	public void setTotalAvailable(Long totalAvailable){
		this.totalAvailable=totalAvailable;
	}
	public void setTotalCount(Long totalCount){
		this.totalCount=totalCount;
	}
	public void setTotalMoney(Double totalMoney){
		this.totalMoney=totalMoney;
	}
	public void setTotalStorage(Long totalStorage){
		this.totalStorage=totalStorage;
	}
	public void setTotalRealStorage(Long totalRealStorage){
		this.totalRealStorage=totalRealStorage;
	}
	public void setTotalReturn(Long totalReturn){
		this.totalReturn=totalReturn;
	}
	public void setTotalMoneyReturn(Double totalMoneyReturn){
		this.totalMoneyReturn=totalMoneyReturn;
	}
	public void setTotalReturnStorage(Long totalReturnStorage){
		this.totalReturnStorage=totalReturnStorage;
	}
	public void setTotalRefundable(Long totalRefundable){
		this.totalRefundable=totalRefundable;
	}
	public void setReceiveStatus(String receiveStatus){
		this.receiveStatus=receiveStatus;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public List<PurchaseProduct> getPurchaseProductList() {
		return purchaseProductList;
	}
	public void setPurchaseProductList(List<PurchaseProduct> purchaseProductList) {
		this.purchaseProductList = purchaseProductList;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
