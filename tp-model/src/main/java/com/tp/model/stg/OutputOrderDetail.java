package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库订单明细
  */
public class OutputOrderDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690114L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long outputOrderId;
	
	/**即商品sku 数据类型varchar(20)*/
	private String skuCode;
	
	/**条形码 数据类型varchar(20)*/
	private String barcode;
	
	/**商品名称 数据类型varchar(50)*/
	private String itemName;
	
	/**商品数量 数据类型int(11)*/
	private Integer itemCount;
	
	/**商品金额 数据类型double*/
	private Double itemValue;
	
	/**市场价 数据类型double*/
	private Double itemOriginPrice;
	
	/**关税价格 数据类型double*/
	private Double customsPrice;
	
	/**供应商名称 数据类型varchar(50)*/
	private String batchNo;
	
	/** 数据类型bigint(20)*/
	private Long inventoryDistId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOutputOrderId(){
		return outputOrderId;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getItemName(){
		return itemName;
	}
	public Integer getItemCount(){
		return itemCount;
	}
	public Double getItemValue(){
		return itemValue;
	}
	public Double getItemOriginPrice(){
		return itemOriginPrice;
	}
	public Double getCustomsPrice(){
		return customsPrice;
	}
	public String getBatchNo(){
		return batchNo;
	}
	public Long getInventoryDistId(){
		return inventoryDistId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOutputOrderId(Long outputOrderId){
		this.outputOrderId=outputOrderId;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public void setItemCount(Integer itemCount){
		this.itemCount=itemCount;
	}
	public void setItemValue(Double itemValue){
		this.itemValue=itemValue;
	}
	public void setItemOriginPrice(Double itemOriginPrice){
		this.itemOriginPrice=itemOriginPrice;
	}
	public void setCustomsPrice(Double customsPrice){
		this.customsPrice=customsPrice;
	}
	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}
	public void setInventoryDistId(Long inventoryDistId){
		this.inventoryDistId=inventoryDistId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
