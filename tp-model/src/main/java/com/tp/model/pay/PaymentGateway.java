package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 支付网关配置表
  */
public class PaymentGateway extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638869L;

	/**支付网关编号 数据类型int(11)*/
	@Id
	private Long gatewayId;
	
	/**网关代码 数据类型varchar(32)*/
	private String gatewayCode;
	
	/**网关名称 数据类型varchar(32)*/
	private String gatewayName;
	
	/**支付方式顺序 数据类型int(11)*/
	private Integer orderIndex;
	
	/**是否允许退款 数据类型tinyint(4)*/
	private Integer allowRefund;
	
	/**退款网关 数据类型varchar(32)*/
	private String refundGateway;
	
	/**网关状态 数据类型tinyint(4)*/
	private Integer status;
	
	/**网关类型(线上-1，线下-2，POS-3，现金-4,其它-5) 数据类型tinyint(4)*/
	private Integer gatewayType;
	
	/**发送报文URL 数据类型varchar(200)*/
	private String sendUrl;
	
	/**回调URL 数据类型varchar(200)*/
	private String callbackUrl;
	
	/**退款顺序（部分退款时） 数据类型tinyint(11)*/
	private Integer refundSort;
	
	/**排序 数据类型tinyint(4)*/
	private Integer paySort;
	
	/**图片地址 数据类型varchar(100)*/
	private String imgUrl;
	
	/**帮助链接 数据类型varchar(100)*/
	private String helpUrl;
	
	/**父网关号 数据类型int(11)*/
	private Integer parentId;
	
	/**是否可进行合并支付 数据类型tinyint(1)*/
	private Integer mergePay;
	
	/**说明 数据类型varchar(100)*/
	private String remark;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	/**创建时间 数据类型datetime*/
	private Date creatTime;
	
	
	public Long getGatewayId(){
		return gatewayId;
	}
	public String getGatewayCode(){
		return gatewayCode;
	}
	public String getGatewayName(){
		return gatewayName;
	}
	public Integer getOrderIndex(){
		return orderIndex;
	}
	public Integer getAllowRefund(){
		return allowRefund;
	}
	public String getRefundGateway(){
		return refundGateway;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getGatewayType(){
		return gatewayType;
	}
	public String getSendUrl(){
		return sendUrl;
	}
	public String getCallbackUrl(){
		return callbackUrl;
	}
	public Integer getRefundSort(){
		return refundSort;
	}
	public Integer getPaySort(){
		return paySort;
	}
	public String getImgUrl(){
		return imgUrl;
	}
	public String getHelpUrl(){
		return helpUrl;
	}
	public Integer getParentId(){
		return parentId;
	}
	public String getRemark(){
		return remark;
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
	public Date getCreatTime(){
		return creatTime;
	}
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public void setGatewayCode(String gatewayCode){
		this.gatewayCode=gatewayCode;
	}
	public void setGatewayName(String gatewayName){
		this.gatewayName=gatewayName;
	}
	public void setOrderIndex(Integer orderIndex){
		this.orderIndex=orderIndex;
	}
	public void setAllowRefund(Integer allowRefund){
		this.allowRefund=allowRefund;
	}
	public void setRefundGateway(String refundGateway){
		this.refundGateway=refundGateway;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setGatewayType(Integer gatewayType){
		this.gatewayType=gatewayType;
	}
	public void setSendUrl(String sendUrl){
		this.sendUrl=sendUrl;
	}
	public void setCallbackUrl(String callbackUrl){
		this.callbackUrl=callbackUrl;
	}
	public void setRefundSort(Integer refundSort){
		this.refundSort=refundSort;
	}
	public void setPaySort(Integer paySort){
		this.paySort=paySort;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}
	public void setHelpUrl(String helpUrl){
		this.helpUrl=helpUrl;
	}
	public void setParentId(Integer parentId){
		this.parentId=parentId;
	}
	public void setRemark(String remark){
		this.remark=remark;
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
	public void setCreatTime(Date creatTime){
		this.creatTime=creatTime;
	}
	public Integer getMergePay() {
		return mergePay;
	}
	public void setMergePay(Integer mergePay) {
		this.mergePay = mergePay;
	}
}
