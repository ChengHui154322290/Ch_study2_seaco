package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 采购[代销]订单[退货单]-商品表
  */
public class PurchaseProduct extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274733L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long purchaseId;
	
	/**退货单关联的原始订单id 数据类型bigint(11)*/
	private Long originId;
	
	/**退货单关联的原始订单-商品id 数据类型bigint(11)*/
	private Long originProductId;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**订单类型(采购订单,采购退货单,代销订单,代销退货单) 数据类型varchar(45)*/
	private String purchaseType;
	
	/** 数据类型varchar(100)*/
	private String skuCode;
	
	/** 数据类型varchar(100)*/
	private String sku;
	
	/**条形码 数据类型varchar(100)*/
	private String barcode;
	
	/** 数据类型varchar(80)*/
	private String productName;
	
	/**商品在仓库中的名称 数据类型varchar(80)*/
	private String productStorageName;
	
	/** 数据类型varchar(45)*/
	private String productUnit;
	
	/** 数据类型varchar(45)*/
	private String prop1;
	
	/** 数据类型varchar(45)*/
	private String prop2;
	
	/** 数据类型varchar(45)*/
	private String prop3;
	
	/**采购数量 数据类型bigint(11)*/
	private Long count;
	
	/**标准价 数据类型double*/
	private Double standardPrice;
	
	/**折扣 数据类型double*/
	private Double discount;
	
	/**供货价 数据类型double*/
	private Double orderPrice;
	
	/**小计 数据类型double*/
	private Double subtotal;
	
	/**备注 数据类型varchar(255)*/
	private String productDesc;
	
	/**入库数量 数据类型bigint(11)*/
	private Long storageCount;
	
	/**品牌id 数据类型bigint(11)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
	
	/**大类id 数据类型varchar(45)*/
	private String bigId;
	
	/**大类名称 数据类型varchar(80)*/
	private String bigName;
	
	/**中类id 数据类型varchar(45)*/
	private String midId;
	
	/**中类名称 数据类型varchar(80)*/
	private String midName;
	
	/**小类id 数据类型varchar(45)*/
	private String smallId;
	
	/**小类名称 数据类型varchar(100)*/
	private String smallName;
	
	/**批号 数据类型varchar(100)*/
	private String batchNumber;
	
	/**采购税率 数据类型double*/
	private Double purchaseRate;
	
	/**关税税率 数据类型double*/
	private Double tariffRate;
	
	/**未税税率 数据类型double*/
	private Double noTaxRate;
	
	/**未税金额 数据类型double*/
	private Double noTaxAccount;
	
	/**退货数量 数据类型bigint(11)*/
	private Long numberReturns;
	
	/**实际退货数量（仓库回写） 数据类型bigint(11)*/
	private Long numberReturnsWarehouse;
	
	/**可用数量 数据类型bigint(11)*/
	private Long availNumber;
	
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
	
	
	public Long getId(){
		return id;
	}
	public Long getPurchaseId(){
		return purchaseId;
	}
	public Long getOriginId(){
		return originId;
	}
	public Long getOriginProductId(){
		return originProductId;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getPurchaseType(){
		return purchaseType;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getSku(){
		return sku;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getProductName(){
		return productName;
	}
	public String getProductStorageName(){
		return productStorageName;
	}
	public String getProductUnit(){
		return productUnit;
	}
	public String getProp1(){
		return prop1;
	}
	public String getProp2(){
		return prop2;
	}
	public String getProp3(){
		return prop3;
	}
	public Long getCount(){
		return count;
	}
	public Double getStandardPrice(){
		return standardPrice;
	}
	public Double getDiscount(){
		return discount;
	}
	public Double getOrderPrice(){
		return orderPrice;
	}
	public Double getSubtotal(){
		return subtotal;
	}
	public String getProductDesc(){
		return productDesc;
	}
	public Long getStorageCount(){
		return storageCount;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public String getBigId(){
		return bigId;
	}
	public String getBigName(){
		return bigName;
	}
	public String getMidId(){
		return midId;
	}
	public String getMidName(){
		return midName;
	}
	public String getSmallId(){
		return smallId;
	}
	public String getSmallName(){
		return smallName;
	}
	public String getBatchNumber(){
		return batchNumber;
	}
	public Double getPurchaseRate(){
		return purchaseRate;
	}
	public Double getTariffRate(){
		return tariffRate;
	}
	public Double getNoTaxRate(){
		return noTaxRate;
	}
	public Double getNoTaxAccount(){
		return noTaxAccount;
	}
	public Long getNumberReturns(){
		return numberReturns;
	}
	public Long getNumberReturnsWarehouse(){
		return numberReturnsWarehouse;
	}
	public Long getAvailNumber(){
		return availNumber;
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
	public void setOriginId(Long originId){
		this.originId=originId;
	}
	public void setOriginProductId(Long originProductId){
		this.originProductId=originProductId;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setPurchaseType(String purchaseType){
		this.purchaseType=purchaseType;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setProductName(String productName){
		this.productName=productName;
	}
	public void setProductStorageName(String productStorageName){
		this.productStorageName=productStorageName;
	}
	public void setProductUnit(String productUnit){
		this.productUnit=productUnit;
	}
	public void setProp1(String prop1){
		this.prop1=prop1;
	}
	public void setProp2(String prop2){
		this.prop2=prop2;
	}
	public void setProp3(String prop3){
		this.prop3=prop3;
	}
	public void setCount(Long count){
		this.count=count;
	}
	public void setStandardPrice(Double standardPrice){
		this.standardPrice=standardPrice;
	}
	public void setDiscount(Double discount){
		this.discount=discount;
	}
	public void setOrderPrice(Double orderPrice){
		this.orderPrice=orderPrice;
	}
	public void setSubtotal(Double subtotal){
		this.subtotal=subtotal;
	}
	public void setProductDesc(String productDesc){
		this.productDesc=productDesc;
	}
	public void setStorageCount(Long storageCount){
		this.storageCount=storageCount;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBigId(String bigId){
		this.bigId=bigId;
	}
	public void setBigName(String bigName){
		this.bigName=bigName;
	}
	public void setMidId(String midId){
		this.midId=midId;
	}
	public void setMidName(String midName){
		this.midName=midName;
	}
	public void setSmallId(String smallId){
		this.smallId=smallId;
	}
	public void setSmallName(String smallName){
		this.smallName=smallName;
	}
	public void setBatchNumber(String batchNumber){
		this.batchNumber=batchNumber;
	}
	public void setPurchaseRate(Double purchaseRate){
		this.purchaseRate=purchaseRate;
	}
	public void setTariffRate(Double tariffRate){
		this.tariffRate=tariffRate;
	}
	public void setNoTaxRate(Double noTaxRate){
		this.noTaxRate=noTaxRate;
	}
	public void setNoTaxAccount(Double noTaxAccount){
		this.noTaxAccount=noTaxAccount;
	}
	public void setNumberReturns(Long numberReturns){
		this.numberReturns=numberReturns;
	}
	public void setNumberReturnsWarehouse(Long numberReturnsWarehouse){
		this.numberReturnsWarehouse=numberReturnsWarehouse;
	}
	public void setAvailNumber(Long availNumber){
		this.availNumber=availNumber;
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
}
