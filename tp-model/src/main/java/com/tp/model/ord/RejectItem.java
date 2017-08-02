package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 退货单子项(商品)
  */
public class RejectItem extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597516L;

	/**退货单商品子项ID 数据类型bigint(20)*/
	@Id
	private Long rejectItemId;
	
	/** 数据类型varchar(32)*/
	private Long rejectCode;
	
	/**子订单编号 数据类型varchar(32)*/
	private Long orderCode;
	
	/**订单子项ID 数据类型bigint(20)*/
	private Long orderItemId;
	
	/**所退商品数量 数据类型int(11)*/
	private Integer itemRefundQuantity;
	
	/**所退商品总金额 数据类型double(10,2)*/
	private Double itemRefundAmount;
	
	/**备注 数据类型varchar(500)*/
	private String remarks;
	
	/**商品SKUID 数据类型varchar(20)*/
	private String itemSkuCode;
	
	/**商品名称 数据类型varchar(200)*/
	private String itemName;
	
	/**商品购买数量 数据类型int(11)*/
	private Integer itemQuantity;
	
	/**商品购买单价 数据类型double(10,2)*/
	private Double itemUnitPrice;
	
	/**商品状态 数据类型tinyint(4)*/
	private Integer itemStatus;
	
	/**商品图片 数据类型varchar(100)*/
	private String itemImgUrl;
	
	/**供应商代码（0-代表是本公司） 数据类型varchar(32)*/
	private String supplierCode;
	
	/**供应商货号 数据类型varchar(32)*/
	private String productCode;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getRejectItemId(){
		return rejectItemId;
	}
	public Long getRejectCode(){
		return rejectCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getOrderItemId(){
		return orderItemId;
	}
	public Integer getItemRefundQuantity(){
		return itemRefundQuantity;
	}
	public Double getItemRefundAmount(){
		return itemRefundAmount;
	}
	public String getRemarks(){
		return remarks;
	}
	public String getItemSkuCode(){
		return itemSkuCode;
	}
	public String getItemName(){
		return itemName;
	}
	public Integer getItemQuantity(){
		return itemQuantity;
	}
	public Double getItemUnitPrice(){
		return itemUnitPrice;
	}
	public Integer getItemStatus(){
		return itemStatus;
	}
	public String getItemImgUrl(){
		return itemImgUrl;
	}
	public String getSupplierCode(){
		return supplierCode;
	}
	public String getProductCode(){
		return productCode;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setRejectItemId(Long rejectItemId){
		this.rejectItemId=rejectItemId;
	}
	public void setRejectCode(Long rejectCode){
		this.rejectCode=rejectCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setOrderItemId(Long orderItemId){
		this.orderItemId=orderItemId;
	}
	public void setItemRefundQuantity(Integer itemRefundQuantity){
		this.itemRefundQuantity=itemRefundQuantity;
	}
	public void setItemRefundAmount(Double itemRefundAmount){
		this.itemRefundAmount=itemRefundAmount;
	}
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public void setItemSkuCode(String itemSkuCode){
		this.itemSkuCode=itemSkuCode;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public void setItemQuantity(Integer itemQuantity){
		this.itemQuantity=itemQuantity;
	}
	public void setItemUnitPrice(Double itemUnitPrice){
		this.itemUnitPrice=itemUnitPrice;
	}
	public void setItemStatus(Integer itemStatus){
		this.itemStatus=itemStatus;
	}
	public void setItemImgUrl(String itemImgUrl){
		this.itemImgUrl=itemImgUrl;
	}
	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}
	public void setProductCode(String productCode){
		this.productCode=productCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
