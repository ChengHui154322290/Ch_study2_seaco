package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库订单
  */
public class InputOrder extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**采购单号 数据类型varchar(50)*/
	private String orderCode;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**仓库编号 数据类型varchar(50)*/
	private String warehouseCode;
	
	/**采购类型 数据类型varchar(50)*/
	private String type;
	
	/**客户id 数据类型varchar(50)*/
	private String customerId;
	
	/**制单日期 数据类型datetime*/
	private Date zdrq;
	
	/**到货日期 数据类型datetime*/
	private Date dhrq;
	
	/**批次号 数据类型varchar(50)*/
	private String zdr;
	
	/**备注 数据类型varchar(50)*/
	private String bz;
	
	/**状态 0 失败 1 成功 数据类型tinyint(4)*/
	private Integer status;
	
	/**失败次数 数据类型int(11)*/
	private Integer failTimes;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商联系人 数据类型varchar(50)*/
	private String supplierContacts;
	
	/**供应商名称 数据类型varchar(100)*/
	private String supplierName;
	
	/**供应商电话 数据类型varchar(50)*/
	private String supplierPhone;
	
	/**供应商地址 数据类型varchar(200)*/
	private String supplierAddresses;
	
	/** 订单详情 **/
	@Virtual
	private List<InputOrderDetail> inputOrderDetails;
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getType(){
		return type;
	}
	public String getCustomerId(){
		return customerId;
	}
	public Date getZdrq(){
		return zdrq;
	}
	public Date getDhrq(){
		return dhrq;
	}
	public String getZdr(){
		return zdr;
	}
	public String getBz(){
		return bz;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getFailTimes(){
		return failTimes;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierContacts(){
		return supplierContacts;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public String getSupplierPhone(){
		return supplierPhone;
	}
	public String getSupplierAddresses(){
		return supplierAddresses;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setCustomerId(String customerId){
		this.customerId=customerId;
	}
	public void setZdrq(Date zdrq){
		this.zdrq=zdrq;
	}
	public void setDhrq(Date dhrq){
		this.dhrq=dhrq;
	}
	public void setZdr(String zdr){
		this.zdr=zdr;
	}
	public void setBz(String bz){
		this.bz=bz;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setFailTimes(Integer failTimes){
		this.failTimes=failTimes;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierContacts(String supplierContacts){
		this.supplierContacts=supplierContacts;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setSupplierPhone(String supplierPhone){
		this.supplierPhone=supplierPhone;
	}
	public void setSupplierAddresses(String supplierAddresses){
		this.supplierAddresses=supplierAddresses;
	}
	public List<InputOrderDetail> getInputOrderDetails() {
		return inputOrderDetails;
	}
	public void setInputOrderDetails(List<InputOrderDetail> inputOrderDetails) {
		this.inputOrderDetails = inputOrderDetails;
	}
}
