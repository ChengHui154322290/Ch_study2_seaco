package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 结算订单商品项表
  */
public class SettleOrderItem extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991707L;

	/**主键 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型varchar(32)*/
	private String orderNo;
	
	/**订单状态 数据类型tinyint(2)*/
	private Integer orderStatus;
	
	/**支付时间 数据类型datetime*/
	private Date orderPayedDate;
	
	/**订单创建时间 数据类型datetime*/
	private Date orderCreateTime;
	
	/**商品项上使用优惠券金额（归属供应商） 数据类型double(10,2)*/
	private Double orderCouponAmount;
	
	/**是否开票 数据类型tinyint(1)*/
	private Integer hasReceipt;
	
	/**订单商品项ID 数据类型bigint(14)*/
	private Long orderItemId;
	
	/**订单运费 数据类型double(10,2)*/
	private Double frightAmount;
	
	/**红包金额 数据类型double(10,2)*/
	private Double couponAmount;
	
	/**商品SKU 数据类型varchar(32)*/
	private String skuCode;
	
	/**商品SKU名称 数据类型varchar(200)*/
	private String skuName;
	
	/**支付方式 数据类型tinyint(3)*/
	private Integer payWayId;
	
	/**结算单价（西客商城售价） 数据类型double(10,2)*/
	private Double price;
	
	/**购买数量 数据类型int(10)*/
	private Integer itemQuantity;
	
	/**结算单价（自营） 数据类型double(10,2)*/
	private Double metaPrice;
	
	/**发货时间 数据类型datetime*/
	private Date deliveryDate;
	
	/**供应商ID 数据类型bigint(14)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(100)*/
	private String supplierName;
	
	/**品牌ID 数据类型bigint(14)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(50)*/
	private String brandName;
	
	/**合同ID 数据类型bigint(14)*/
	private Long contractId;
	
	/**活动ID 数据类型bigint(14)*/
	private Long topicId;
	
	/**活动名称 数据类型varchar(150)*/
	private String topicName;
	
	/**活动结束时间 数据类型datetime*/
	private Date topicEndDate;
	
	/**间隔天数 数据类型int(3) unsigned zerofill*/
	private Integer intervalDays;
	
	/**应该结算金额 数据类型double(10,2)*/
	private Double settleAmount;
	
	/**结算佣金比率 数据类型double(10,2)*/
	private Double settleCost;
	
	/**佣金 数据类型double(10,2)*/
	private Double settleCostAmount;
	
	/**结算状态 数据类型tinyint(2)*/
	private Integer status;
	
	/**结算编号 数据类型bigint(20)*/
	private Long settleNo;
	
	/**结算时间 数据类型datetime*/
	private Date settleDate;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	/**更新时间 数据类型datetime*/
	private Date modifyDate;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public Integer getOrderStatus(){
		return orderStatus;
	}
	public Date getOrderPayedDate(){
		return orderPayedDate;
	}
	public Date getOrderCreateTime(){
		return orderCreateTime;
	}
	public Double getOrderCouponAmount(){
		return orderCouponAmount;
	}
	public Integer getHasReceipt(){
		return hasReceipt;
	}
	public Long getOrderItemId(){
		return orderItemId;
	}
	public Double getFrightAmount(){
		return frightAmount;
	}
	public Double getCouponAmount(){
		return couponAmount;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getSkuName(){
		return skuName;
	}
	public Integer getPayWayId(){
		return payWayId;
	}
	public Double getPrice(){
		return price;
	}
	public Integer getItemQuantity(){
		return itemQuantity;
	}
	public Double getMetaPrice(){
		return metaPrice;
	}
	public Date getDeliveryDate(){
		return deliveryDate;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Long getContractId(){
		return contractId;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getTopicName(){
		return topicName;
	}
	public Date getTopicEndDate(){
		return topicEndDate;
	}
	public Integer getIntervalDays(){
		return intervalDays;
	}
	public Double getSettleAmount(){
		return settleAmount;
	}
	public Double getSettleCost(){
		return settleCost;
	}
	public Double getSettleCostAmount(){
		return settleCostAmount;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getSettleNo(){
		return settleNo;
	}
	public Date getSettleDate(){
		return settleDate;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public Date getModifyDate(){
		return modifyDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}
	public void setOrderPayedDate(Date orderPayedDate){
		this.orderPayedDate=orderPayedDate;
	}
	public void setOrderCreateTime(Date orderCreateTime){
		this.orderCreateTime=orderCreateTime;
	}
	public void setOrderCouponAmount(Double orderCouponAmount){
		this.orderCouponAmount=orderCouponAmount;
	}
	public void setHasReceipt(Integer hasReceipt){
		this.hasReceipt=hasReceipt;
	}
	public void setOrderItemId(Long orderItemId){
		this.orderItemId=orderItemId;
	}
	public void setFrightAmount(Double frightAmount){
		this.frightAmount=frightAmount;
	}
	public void setCouponAmount(Double couponAmount){
		this.couponAmount=couponAmount;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setSkuName(String skuName){
		this.skuName=skuName;
	}
	public void setPayWayId(Integer payWayId){
		this.payWayId=payWayId;
	}
	public void setPrice(Double price){
		this.price=price;
	}
	public void setItemQuantity(Integer itemQuantity){
		this.itemQuantity=itemQuantity;
	}
	public void setMetaPrice(Double metaPrice){
		this.metaPrice=metaPrice;
	}
	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTopicName(String topicName){
		this.topicName=topicName;
	}
	public void setTopicEndDate(Date topicEndDate){
		this.topicEndDate=topicEndDate;
	}
	public void setIntervalDays(Integer intervalDays){
		this.intervalDays=intervalDays;
	}
	public void setSettleAmount(Double settleAmount){
		this.settleAmount=settleAmount;
	}
	public void setSettleCost(Double settleCost){
		this.settleCost=settleCost;
	}
	public void setSettleCostAmount(Double settleCostAmount){
		this.settleCostAmount=settleCostAmount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSettleNo(Long settleNo){
		this.settleNo=settleNo;
	}
	public void setSettleDate(Date settleDate){
		this.settleDate=settleDate;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public void setModifyDate(Date modifyDate){
		this.modifyDate=modifyDate;
	}
}
