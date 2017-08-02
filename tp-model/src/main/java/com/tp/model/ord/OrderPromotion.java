package com.tp.model.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.ord.OrderPromotionConstant.PromotionType;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单促销明细表
  */
public class OrderPromotion extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597513L;

	/**PK 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单ID 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**子订单ID 数据类型bigint(20)*/
	private Long orderId;
	
	/**订单行ID 数据类型bigint(20)*/
	private Long orderItemId;
	
	/**促销ID 数据类型bigint(20)*/
	private Long promotionId;
	
	/**类型（整单优惠、赠品、包邮等等） 数据类型tinyint(3)*/
	private Integer type;
	
	/**父订单代码 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**订单编码 数据类型bigint(20)*/
	private Long orderCode;
	
	/**优惠金额 数据类型double(10,2)*/
	private Double discount;
	
	/**促销名称 数据类型varchar(255)*/
	private String promotionName;
	
	/**ȯ12 数据类型tinyint(3)*/
	private Integer sourceType;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**优惠券码 数据类型varchar(32)*/
	private String couponCode;
	
	/**优惠券类型 数据类型tinyint(3)*/
	private Integer couponType;
	
	/**优惠券面值 数据类型double(10,2)*/
	private Double couponFaceAmount;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**关联推广员ID*/
	private Long promoterId;
	
	@Virtual
	/** 用户优惠券Id*/
	private Long couponUserId;
	
	/**减免金额备注*/
	@Virtual
	private String remark;
	
	
	public String getTypeStr() {
		return PromotionType.getCnName(getType());
	}
	public Long getId(){
		return id;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getOrderId(){
		return orderId;
	}
	public Long getOrderItemId(){
		return orderItemId;
	}
	public Long getPromotionId(){
		return promotionId;
	}
	public Integer getType(){
		return type;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Double getDiscount(){
		return discount;
	}
	public String getPromotionName(){
		return promotionName;
	}
	public Integer getSourceType(){
		return sourceType;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getCouponCode(){
		return couponCode;
	}
	public Integer getCouponType(){
		return couponType;
	}
	public Double getCouponFaceAmount(){
		return couponFaceAmount;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}
	public void setOrderItemId(Long orderItemId){
		this.orderItemId=orderItemId;
	}
	public void setPromotionId(Long promotionId){
		this.promotionId=promotionId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setDiscount(Double discount){
		this.discount=discount;
	}
	public void setPromotionName(String promotionName){
		this.promotionName=promotionName;
	}
	public void setSourceType(Integer sourceType){
		this.sourceType=sourceType;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setCouponCode(String couponCode){
		this.couponCode=couponCode;
	}
	public void setCouponType(Integer couponType){
		this.couponType=couponType;
	}
	public void setCouponFaceAmount(Double couponFaceAmount){
		this.couponFaceAmount=couponFaceAmount;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public Long getCouponUserId() {
		return couponUserId;
	}
	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
}
