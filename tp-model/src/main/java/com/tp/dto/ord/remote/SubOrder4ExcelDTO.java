package com.tp.dto.ord.remote;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * excel导出订单dto
 * 
 * @author szy
 * @version 0.0.1
 */
public class SubOrder4ExcelDTO implements Serializable {

	private static final long serialVersionUID = -7078298081465815278L;

	private String code;
	private String orderCode;
	private String statusStr;
	private String typeStr;
	private String payWayStr;
	private String supplierName;
	private String loginName;
	private Double payTotal;
	private String consigneeAddress;
	private String consigneeMobile;
	private String consigneeName;
	private Date createTime;
	private Date payTime;
	private Date deliveryTime;
	
	private String seaChannel;
	private String payCode;
	/** 身份证号 */
	private String idCode;
	/** 真实姓名 */
	private String realName;
	private Double discount;
	private List<OrderLine4ExcelDTO> lineList;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getPayWayStr() {
		return payWayStr;
	}
	public void setPayWayStr(String payWayStr) {
		this.payWayStr = payWayStr;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Double getPayTotal() {
		return payTotal;
	}
	public void setPayTotal(Double payTotal) {
		this.payTotal = payTotal;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public List<OrderLine4ExcelDTO> getLineList() {
		return lineList;
	}
	public void setLineList(List<OrderLine4ExcelDTO> lineList) {
		this.lineList = lineList;
	}
	public String getSeaChannel() {
		return seaChannel;
	}
	public void setSeaChannel(String seaChannel) {
		this.seaChannel = seaChannel;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
