package com.tp.model.wms;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 公共仓入库订单明细
  */
public class StockasnDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**入库订单id 数据类型bigint(20)*/
	private Long stockasnId;
	
	/**sku 数据类型varchar(50)*/
	private String itemSku;
	
	/**商品条形码 数据类型varchar(50)*/
	private String itemBarcode;
	
	/**库存类型 数据类型varchar(10)*/
	private String inventoryType;
	
	/**申报数量 数据类型int(11)*/
	private Integer quantity;
	
	/**商品实际价格 数据类型double(10,2)*/
	private Double actualPrice;
	
	/**销售价格 数据类型double(10,2)*/
	private Double price;
	
	/**原产国国别代码 数据类型varchar(10)*/
	private String countryCode;
	
	/**原产国名字 数据类型varchar(20)*/
	private String countryName;
	
	/** 创建时间 **/
	private Date createTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public Long getStockasnId(){
		return stockasnId;
	}
	public String getItemSku(){
		return itemSku;
	}
	public String getItemBarcode(){
		return itemBarcode;
	}
	public String getInventoryType(){
		return inventoryType;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Double getActualPrice(){
		return actualPrice;
	}
	public Double getPrice(){
		return price;
	}
	public String getCountryCode(){
		return countryCode;
	}
	public String getCountryName(){
		return countryName;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setStockasnId(Long stockasnId){
		this.stockasnId=stockasnId;
	}
	public void setItemSku(String itemSku){
		this.itemSku=itemSku;
	}
	public void setItemBarcode(String itemBarcode){
		this.itemBarcode=itemBarcode;
	}
	public void setInventoryType(String inventoryType){
		this.inventoryType=inventoryType;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setActualPrice(Double actualPrice){
		this.actualPrice=actualPrice;
	}
	public void setPrice(Double price){
		this.price=price;
	}
	public void setCountryCode(String countryCode){
		this.countryCode=countryCode;
	}
	public void setCountryName(String countryName){
		this.countryName=countryName;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
