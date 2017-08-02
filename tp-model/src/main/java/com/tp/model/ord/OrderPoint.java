package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单积分使用表
  */
public class OrderPoint extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1471421712809L;

	/**ID 数据类型bigint(11)*/
	@Id
	private Long orderPointId;
	
	/**订单项ID 数据类型bigint(11)*/
	private Long orderItemId;
	
	/**订单id 数据类型bigint(11)*/
	private Long orderId;
	
	/**订单编码 数据类型bigint(16)*/
	private Long orderCode;
	
	/**父订单ID 数据类型bigint(11)*/
	private Long parentOrderId;
	
	/**父订单编码 数据类型bigint(16)*/
	private Long parentOrderCode;
	
	/**积分包ID 数据类型bigint(11)*/
	private Long pointPackageId;
	
	/**使用积分 数据类型int(8)*/
	private Integer point;
	
	/**已返还积分 数据类型int(8)*/
	private Integer refundedPoint;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	/**支付积分类型*/
	private String pointType;
	
	/** 渠道代码 **/
	private String channelCode;
	
	public Long getOrderPointId(){
		return orderPointId;
	}
	public Long getOrderItemId(){
		return orderItemId;
	}
	public Long getOrderId(){
		return orderId;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Integer getPoint(){
		return point;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setOrderPointId(Long orderPointId){
		this.orderPointId=orderPointId;
	}
	public void setOrderItemId(Long orderItemId){
		this.orderItemId=orderItemId;
	}
	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public Long getPointPackageId() {
		return pointPackageId;
	}
	public void setPointPackageId(Long pointPackageId) {
		this.pointPackageId = pointPackageId;
	}
	public Integer getRefundedPoint() {
		return refundedPoint;
	}
	public void setRefundedPoint(Integer refundedPoint) {
		this.refundedPoint = refundedPoint;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	
}
