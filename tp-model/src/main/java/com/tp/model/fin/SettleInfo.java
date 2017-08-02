package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 结算信息表
  */
public class SettleInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991704L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**结算号（yyMMdd+4位供应商ID+6位序列号） 数据类型bigint(20)*/
	private Long settleNo;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(100)*/
	private String supplierName;
	
	/**品牌ID 数据类型bigint(14)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(50)*/
	private String brandName;
	
	/**交易笔数 数据类型int(6)*/
	private Integer tradeCount;
	
	/**退款笔数 数据类型int(6)*/
	private Integer refundCount;
	
	/**补偿笔数 数据类型int(6)*/
	private Integer offsetCount;
	
	/**交易金额 数据类型double(10,2)*/
	private Double tradeAmount;
	
	/**结算金额 数据类型double(10,2)*/
	private Double settleAmount;
	
	/**退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**补偿金额 数据类型double(10,2)*/
	private Double offsetAmount;
	
	/**实付金额 数据类型double(10,2)*/
	private Double actualAmount;
	
	/**结算状态（0-待审核，1-审核通过，2-审核驳回，3-等待打款，4-打款中，5-打款完成） 数据类型tinyint(4)*/
	private Integer status;
	
	/**结算对象ID 数据类型bigint(20)*/
	private Long targetId;
	
	/**结算对象名称 数据类型varchar(100)*/
	private String targetName;
	
	/**审核时间 数据类型datetime*/
	private Date auditDate;
	
	/**审核人 数据类型varchar(32)*/
	private String auditUser;
	
	/**审核说明 数据类型varchar(500)*/
	private String remark;
	
	/**结算间隔天数 数据类型int(3)*/
	private Integer intervalDays;
	
	/**创建人 数据类型varchar(32)*/
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
	public Long getSettleNo(){
		return settleNo;
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
	public Integer getTradeCount(){
		return tradeCount;
	}
	public Integer getRefundCount(){
		return refundCount;
	}
	public Integer getOffsetCount(){
		return offsetCount;
	}
	public Double getTradeAmount(){
		return tradeAmount;
	}
	public Double getSettleAmount(){
		return settleAmount;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public Double getOffsetAmount(){
		return offsetAmount;
	}
	public Double getActualAmount(){
		return actualAmount;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getTargetId(){
		return targetId;
	}
	public String getTargetName(){
		return targetName;
	}
	public Date getAuditDate(){
		return auditDate;
	}
	public String getAuditUser(){
		return auditUser;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getIntervalDays(){
		return intervalDays;
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
	public void setSettleNo(Long settleNo){
		this.settleNo=settleNo;
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
	public void setTradeCount(Integer tradeCount){
		this.tradeCount=tradeCount;
	}
	public void setRefundCount(Integer refundCount){
		this.refundCount=refundCount;
	}
	public void setOffsetCount(Integer offsetCount){
		this.offsetCount=offsetCount;
	}
	public void setTradeAmount(Double tradeAmount){
		this.tradeAmount=tradeAmount;
	}
	public void setSettleAmount(Double settleAmount){
		this.settleAmount=settleAmount;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setOffsetAmount(Double offsetAmount){
		this.offsetAmount=offsetAmount;
	}
	public void setActualAmount(Double actualAmount){
		this.actualAmount=actualAmount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setTargetId(Long targetId){
		this.targetId=targetId;
	}
	public void setTargetName(String targetName){
		this.targetName=targetName;
	}
	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}
	public void setAuditUser(String auditUser){
		this.auditUser=auditUser;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setIntervalDays(Integer intervalDays){
		this.intervalDays=intervalDays;
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
