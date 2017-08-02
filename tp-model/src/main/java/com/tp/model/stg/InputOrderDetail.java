package com.tp.model.stg;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库订单明细

  */
public class InputOrderDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**入库订单id 数据类型bigint(20)*/
	private Long inputOrderId;
	
	/**即sku 数据类型varchar(50)*/
	private String skuCode;
	
	/**条形码 数据类型varchar(50)*/
	private String barcode;
	
	/**商品名称 数据类型varchar(100)*/
	private String itemName;
	
	/**商品数量 数据类型int(11)*/
	private Integer itemCount;
	
	/**商品金额 数据类型double*/
	private Double itemValue;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**商品品牌 数据类型varchar(255)*/
	private String itemBrandName;
	
	
	public Long getId(){
		return id;
	}
	public Long getInputOrderId(){
		return inputOrderId;
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
	public String getRemark(){
		return remark;
	}
	public String getItemBrandName(){
		return itemBrandName;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setInputOrderId(Long inputOrderId){
		this.inputOrderId=inputOrderId;
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
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setItemBrandName(String itemBrandName){
		this.itemBrandName=itemBrandName;
	}
}
