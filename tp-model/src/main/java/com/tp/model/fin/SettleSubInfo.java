package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 结算子项表
  */
public class SettleSubInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991707L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**结算子项编号 数据类型bigint(20)*/
	private Long settleSubNo;
	
	/**结算编号 数据类型bigint(20)*/
	private Long settleNo;
	
	/**顺序号 数据类型int(2)*/
	private Integer sortNo;
	
	/**付应金额 数据类型double(10,2)*/
	private Double payableAmount;
	
	/**应付时间 数据类型datetime*/
	private Date payableDate;
	
	/**实付金额 数据类型double(10,2)*/
	private Double actualAmount;
	
	/**调整金额 数据类型double(10,2)*/
	private Double adjustAmount;
	
	/** 退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**说明 数据类型varchar(500)*/
	private String remark;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**动活号 数据类型bigint(20)*/
	private Long topicId;
	
	/**结算对象ID 数据类型bigint(20)*/
	private Long targetId;
	
	/**状态 数据类型tinyint(2)*/
	private Integer status;
	
	/**活动名称 数据类型varchar(100)*/
	private String topicName;
	
	/**创建时间 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	/**更新人 数据类型varchar(32)*/
	private String modifyUser;
	
	/**更新时间 数据类型datetime*/
	private Date modifyDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getSettleSubNo(){
		return settleSubNo;
	}
	public Long getSettleNo(){
		return settleNo;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public Double getPayableAmount(){
		return payableAmount;
	}
	public Date getPayableDate(){
		return payableDate;
	}
	public Double getActualAmount(){
		return actualAmount;
	}
	public Double getAdjustAmount(){
		return adjustAmount;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public String getRemark(){
		return remark;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getTargetId(){
		return targetId;
	}
	public Integer getStatus(){
		return status;
	}
	public String getTopicName(){
		return topicName;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public String getModifyUser(){
		return modifyUser;
	}
	public Date getModifyDate(){
		return modifyDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSettleSubNo(Long settleSubNo){
		this.settleSubNo=settleSubNo;
	}
	public void setSettleNo(Long settleNo){
		this.settleNo=settleNo;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setPayableAmount(Double payableAmount){
		this.payableAmount=payableAmount;
	}
	public void setPayableDate(Date payableDate){
		this.payableDate=payableDate;
	}
	public void setActualAmount(Double actualAmount){
		this.actualAmount=actualAmount;
	}
	public void setAdjustAmount(Double adjustAmount){
		this.adjustAmount=adjustAmount;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTargetId(Long targetId){
		this.targetId=targetId;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setTopicName(String topicName){
		this.topicName=topicName;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public void setModifyUser(String modifyUser){
		this.modifyUser=modifyUser;
	}
	public void setModifyDate(Date modifyDate){
		this.modifyDate=modifyDate;
	}
}
