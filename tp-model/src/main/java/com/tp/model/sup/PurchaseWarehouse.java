package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 仓库预约单
  */
public class PurchaseWarehouse extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274734L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(45)*/
	private Long purchaseId;
	
	/**预约单号 数据类型varchar(100)*/
	private String bookingCode;
	
	/**预约日期 数据类型datetime*/
	private Date bookingDate;
	
	/**预约单备注 数据类型varchar(255)*/
	private String bookingDesc;
	
	/**单据类型 数据类型varchar(45)*/
	private String purchaseType;
	
	/**单据编号 数据类型varchar(100)*/
	private String purchaseCode;
	
	/**单据日期 数据类型date*/
	private Date purchaseDate;
	
	/**单据备注 数据类型varchar(255)*/
	private String purchaseDesc;
	
	/**期望日期->到货日期 数据类型datetime*/
	private Date purchaseExpectDate;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**仓库id 数据类型bigint(11)*/
	private Long warehouseId;
	
	/**仓库名称 数据类型varchar(80)*/
	private String warehouseName;
	
	/**仓库地址 数据类型varchar(80)*/
	private String warehouseAddr;
	
	/**仓库联系人id 数据类型varchar(100)*/
	private String warehouseLinkmanId;
	
	/**仓库联系人名称 数据类型varchar(80)*/
	private String warehouseLinkmanName;
	
	/**仓库联系人电话 数据类型varchar(60)*/
	private String warehouseLinkmanTel;
	
	/**仓库联系人电子邮箱 数据类型varchar(45)*/
	private String warehouseLinkmanEmail;
	
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
    private Date orderExpectDate;
	
	public Long getId(){
		return id;
	}
	public Long getPurchaseId(){
		return purchaseId;
	}
	public String getBookingCode(){
		return bookingCode;
	}
	public Date getBookingDate(){
		return bookingDate;
	}
	public String getBookingDesc(){
		return bookingDesc;
	}
	public String getPurchaseType(){
		return purchaseType;
	}
	public String getPurchaseCode(){
		return purchaseCode;
	}
	public Date getPurchaseDate(){
		return purchaseDate;
	}
	public String getPurchaseDesc(){
		return purchaseDesc;
	}
	public Date getPurchaseExpectDate(){
		return purchaseExpectDate;
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
	public String getWarehouseAddr(){
		return warehouseAddr;
	}
	public String getWarehouseLinkmanId(){
		return warehouseLinkmanId;
	}
	public String getWarehouseLinkmanName(){
		return warehouseLinkmanName;
	}
	public String getWarehouseLinkmanTel(){
		return warehouseLinkmanTel;
	}
	public String getWarehouseLinkmanEmail(){
		return warehouseLinkmanEmail;
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
	public void setPurchaseId(Long purchaseId){
		this.purchaseId=purchaseId;
	}
	public void setBookingCode(String bookingCode){
		this.bookingCode=bookingCode;
	}
	public void setBookingDate(Date bookingDate){
		this.bookingDate=bookingDate;
	}
	public void setBookingDesc(String bookingDesc){
		this.bookingDesc=bookingDesc;
	}
	public void setPurchaseType(String purchaseType){
		this.purchaseType=purchaseType;
	}
	public void setPurchaseCode(String purchaseCode){
		this.purchaseCode=purchaseCode;
	}
	public void setPurchaseDate(Date purchaseDate){
		this.purchaseDate=purchaseDate;
	}
	public void setPurchaseDesc(String purchaseDesc){
		this.purchaseDesc=purchaseDesc;
	}
	public void setPurchaseExpectDate(Date purchaseExpectDate){
		this.purchaseExpectDate=purchaseExpectDate;
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
	public void setWarehouseAddr(String warehouseAddr){
		this.warehouseAddr=warehouseAddr;
	}
	public void setWarehouseLinkmanId(String warehouseLinkmanId){
		this.warehouseLinkmanId=warehouseLinkmanId;
	}
	public void setWarehouseLinkmanName(String warehouseLinkmanName){
		this.warehouseLinkmanName=warehouseLinkmanName;
	}
	public void setWarehouseLinkmanTel(String warehouseLinkmanTel){
		this.warehouseLinkmanTel=warehouseLinkmanTel;
	}
	public void setWarehouseLinkmanEmail(String warehouseLinkmanEmail){
		this.warehouseLinkmanEmail=warehouseLinkmanEmail;
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
	public Date getOrderExpectDate() {
		return orderExpectDate;
	}
	public void setOrderExpectDate(Date orderExpectDate) {
		this.orderExpectDate = orderExpectDate;
	}
}
