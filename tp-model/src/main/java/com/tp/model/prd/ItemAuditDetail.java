package com.tp.model.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商家平台审核表
  */
public class ItemAuditDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451219235509L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**审核者 user id 数据类型bigint(20)*/
	private Long auditUserId;
	
	/**ÉóºËÕßÃû³Æ 数据类型varchar(50)*/
	private String auditUserName;
	
	/**商家平台sku id 数据类型bigint(20)*/
	private Long sellerSkuId;
	
	/**供应商主键id 数据类型bigint(20)*/
	private Long supplyerId;
	
	/**审核层级 数据类型varchar(50)*/
	private String auditLevel;
	
	/**审核结果 数据类型varchar(255)*/
	private String auditResult;
	
	/**审核意见 数据类型varchar(1024)*/
	private String auditDesc;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**审核时间 数据类型datetime*/
	private Date auditTime;
	
	@Virtual
	private String[] listRejectKey;
	
	public Long getId(){
		return id;
	}
	public Long getAuditUserId(){
		return auditUserId;
	}
	public String getAuditUserName(){
		return auditUserName;
	}
	public Long getSellerSkuId(){
		return sellerSkuId;
	}
	public Long getSupplyerId(){
		return supplyerId;
	}
	public String getAuditLevel(){
		return auditLevel;
	}
	public String getAuditResult(){
		return auditResult;
	}
	public String getAuditDesc(){
		return auditDesc;
	}
	public String getRemark(){
		return remark;
	}
	public Date getAuditTime(){
		return auditTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAuditUserId(Long auditUserId){
		this.auditUserId=auditUserId;
	}
	public void setAuditUserName(String auditUserName){
		this.auditUserName=auditUserName;
	}
	public void setSellerSkuId(Long sellerSkuId){
		this.sellerSkuId=sellerSkuId;
	}
	public void setSupplyerId(Long supplyerId){
		this.supplyerId=supplyerId;
	}
	public void setAuditLevel(String auditLevel){
		this.auditLevel=auditLevel;
	}
	public void setAuditResult(String auditResult){
		this.auditResult=auditResult;
	}
	public void setAuditDesc(String auditDesc){
		this.auditDesc=auditDesc;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setAuditTime(Date auditTime){
		this.auditTime=auditTime;
	}
	public String[] getListRejectKey() {
		return listRejectKey;
	}
	public void setListRejectKey(String[] listRejectKey) {
		this.listRejectKey = listRejectKey;
	}
}
