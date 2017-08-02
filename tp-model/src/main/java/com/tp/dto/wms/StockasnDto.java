/**
 * 
 */
package com.tp.dto.wms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 
 * 入库订单/采购订单
 */
public class StockasnDto implements Serializable{

	private static final long serialVersionUID = -5194689209679218198L;

	/*---------------订单基本信息 -----------*/
	/** 采购单编号 **/
	private String orderCode;
	
	/** 合同编号 **/
	private String contractCode;
	
	/** 仓库ID **/
	private Long warehouseId;
	
	/** 仓储编码 **/
	private String warehouseCode;

	/** 供应商编码 **/
	private String supplierCode;
	
	/** 供应商名称 **/
	private String supplierName;
	
	/** 订单类型 **/
	private String orderType;
	
	/** 订单创建时间 **/
	private Date orderCreateTime;
	
	/** 毛重 **/
	private Double grossWeight;
	
	/** 净重 **/
	private Double netWeight;
	
	/** 件数 **/
	private Integer amount;
	
	/** 批次号 **/
	private String batchNo; 
	
	/*-----------------配送信息 ------------*/
	/** 物流公司ID **/
	private String logisticsCode;
	
	/** 运单号 **/
	private String expressCode;
	
	/** 前物流订单号，如退货入库单可能会用到 **/
	private String preOrderCode;
	
	/*-----------------计划信息-------------*/
	/** 预期送达开始时间 **/
	private Date planStartTime;
	
	/** 预期送达结束时间 **/
	private Date planOverTime;
	
	/*-----------------收件人信息------------*/
	/** 收件方邮编 **/ 
	private String postCode;
	
	/** 收件方省份 **/
	private String province;
	
	/** 收件方城市 **/
	private String city;
	
	/** 收件方区县 **/
	private String area;
	
	/** 收件方地址 **/
	private String address;
	
	/** 收件人名称 **/
	private String consignee;
	
	/** 收件人手机 **/
	private String mobile;
	
	/** 收件人电话 **/
	private String tel;
	
	/*------------ 订单商品信息 ------------*/
	/** 订单商品信息 **/
	private List<StockasnItem> orderItemList;
	
	/** 备注 **/
	private String remark;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Date orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getPreOrderCode() {
		return preOrderCode;
	}

	public void setPreOrderCode(String preOrderCode) {
		this.preOrderCode = preOrderCode;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanOverTime() {
		return planOverTime;
	}

	public void setPlanOverTime(Date planOverTime) {
		this.planOverTime = planOverTime;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public List<StockasnItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<StockasnItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
