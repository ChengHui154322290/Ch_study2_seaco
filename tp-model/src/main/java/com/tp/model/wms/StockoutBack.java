package com.tp.model.wms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库单回执
  */
public class StockoutBack extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1465719862113L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单号 数据类型varchar(30)*/
	private String orderCode;
	
	/**仓库编号 数据类型varchar(20)*/
	private String warehouseCode;
	
	/**运单号 数据类型varchar(30)*/
	private String expressNo;
	
	/**物流企业编码 数据类型varchar(50)*/
	private String logisticsCompanyCode;
	
	/**物流企业名称 数据类型varchar(50)*/
	private String logisticsCompanyName;
	
	/**包裹重量 数据类型double(10,2)*/
	private Double weight;
	
	/**审核人 数据类型varchar(50)*/
	private String auditor;
	
	/**审核时间 数据类型datetime*/
	private Date auditTime;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 订单处理发货状态0失败1成功 数据类型tinyint(4) */
	private Integer status;
	
	/** 订单处理发货状态描述 */
	private String message;
	
	/** 仓库WMSCode */
	private String wmsCode; 
	
	/** 商品详情 */
	@Virtual
	private List<StockoutBackDetail> details;
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public String getLogisticsCompanyCode(){
		return logisticsCompanyCode;
	}
	public String getLogisticsCompanyName(){
		return logisticsCompanyName;
	}
	public Double getWeight(){
		return weight;
	}
	public String getAuditor(){
		return auditor;
	}
	public Date getAuditTime(){
		return auditTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setLogisticsCompanyCode(String logisticsCompanyCode){
		this.logisticsCompanyCode=logisticsCompanyCode;
	}
	public void setLogisticsCompanyName(String logisticsCompanyName){
		this.logisticsCompanyName=logisticsCompanyName;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setAuditor(String auditor){
		this.auditor=auditor;
	}
	public void setAuditTime(Date auditTime){
		this.auditTime=auditTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public List<StockoutBackDetail> getDetails() {
		return details;
	}
	public void setDetails(List<StockoutBackDetail> details) {
		this.details = details;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getWmsCode() {
		return wmsCode;
	}
	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}
}
