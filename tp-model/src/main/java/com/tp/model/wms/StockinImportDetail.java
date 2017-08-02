package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库单导入明细表
  */
public class StockinImportDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473384017017L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**导入记录主键  数据类型bigint(10)*/
	private Long logId;
	
	/**token标识 数据类型varchar(50)*/
	private String uploadToken;
	
	/***采购单号 数据类型varchar(50)*/
	private String purchaseCode;
	
	/***仓库编号 数据类型varchar(50)*/
	private String warehouseCode;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId;
	
	/***条形码（商品码） 数据类型varchar(50)*/
	private String barcode;
	
	/***商品SKU 数据类型varchar(50)*/
	private String skuCode;
	
	/***备案料号 数据类型varchar(50)*/
	private String articleNumber;
	
	/***实际入库数量 数据类型bigint(20)*/
	private Long factAmount;
	
	/**计划入库数量 数据类型bigint(20)*/
	private Long planAmount;
	
	/***实际入库时间 数据类型datetime*/
	private Date stockinTime;
	
	/**操作人 数据类型varchar(50)*/
	private String operator;
	
	/**操作时间 数据类型datetime*/
	private Date operateTime;
	
	/**导入的excel行号 数据类型bigint(8)*/
	private Long excelIndex;
	
	/**状态:1-导入成功，2-导入失败，默认1 数据类型int(1)*/
	private Integer status;
	
	/**操作描述 数据类型text*/
	private String opMessage;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(100)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getLogId(){
		return logId;
	}
	public String getUploadToken(){
		return uploadToken;
	}
	public String getPurchaseCode(){
		return purchaseCode;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getArticleNumber(){
		return articleNumber;
	}
	public Long getFactAmount(){
		return factAmount;
	}
	public Long getPlanAmount(){
		return planAmount;
	}
	public Date getStockinTime(){
		return stockinTime;
	}
	public String getOperator(){
		return operator;
	}
	public Date getOperateTime(){
		return operateTime;
	}
	public Long getExcelIndex(){
		return excelIndex;
	}
	public Integer getStatus(){
		return status;
	}
	public String getOpMessage(){
		return opMessage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setUploadToken(String uploadToken){
		this.uploadToken=uploadToken;
	}
	public void setPurchaseCode(String purchaseCode){
		this.purchaseCode=purchaseCode;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setArticleNumber(String articleNumber){
		this.articleNumber=articleNumber;
	}
	public void setFactAmount(Long factAmount){
		this.factAmount=factAmount;
	}
	public void setPlanAmount(Long planAmount){
		this.planAmount=planAmount;
	}
	public void setStockinTime(Date stockinTime){
		this.stockinTime=stockinTime;
	}
	public void setOperator(String operator){
		this.operator=operator;
	}
	public void setOperateTime(Date operateTime){
		this.operateTime=operateTime;
	}
	public void setExcelIndex(Long excelIndex){
		this.excelIndex=excelIndex;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setOpMessage(String opMessage){
		this.opMessage=opMessage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
