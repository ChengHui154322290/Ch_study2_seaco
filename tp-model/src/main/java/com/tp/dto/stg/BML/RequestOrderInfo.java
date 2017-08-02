/**
 * 
 */
package com.tp.dto.stg.BML;

import java.util.List;

/**
 * @author szy
 * 出库单
 */
public class RequestOrderInfo {
	/** 仓库编号 */
	private String warehouseid;

	/** 客户id */
	private String customerId;
	
	/** 即订单编号 */
	private String orderCode;
	
	/** 即订单编号 */
	private String systemId;

	/** 订单类型 */
	private String orderType;

	/** 配送方式 */
	private String shipping;

	/** 销售渠道 */
	private String issuePartyId;

	/** 销售渠道名称 */
	private String issuePartyName;

	/** 客户昵称 */
	private String customerName;

	/** 付款方式 */
	private String payment;

	/** 订购时间 */
	private String orderTime;
	
	/** 网址  暂时设置为发票抬头*/
	private String website;

	/** 运费 */
	private Double freight;

	/** 货到付款服务费  折扣金额  */
	private Double serviceCharge;

	/** 付款时间 */
	private String payTime;
	
	/** 是否开票 */
	private String isCashsale;

	/** 订单优先级 */
	private String priority;
	
	/** 预期发货时间 */
	private String expectedTime;

	/** 要求交货时间 */
	private String requiredTime;
	
	/** 收件人 */
	private String name;

	/** 邮编 */
	private String postcode;

	/** 固定电话 */
	private String phone;

	/** 手机号 */
	private String mobile;

	/** 省 */
	private String prov;

	/** 市 */
	private String city;

	/** 区 */
	private String district;

	/** 地址 */
	private String address;

	/** 订单实付总金额 */
	private Double itemsValue;

	/** 卖家备注 */
	private String remark;
	
	private List<RequestOrderItem> items;

	public String getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getIssuePartyId() {
		return issuePartyId;
	}

	public void setIssuePartyId(String issuePartyId) {
		this.issuePartyId = issuePartyId;
	}

	public String getIssuePartyName() {
		return issuePartyName;
	}

	public void setIssuePartyName(String issuePartyName) {
		this.issuePartyName = issuePartyName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getIsCashsale() {
		return isCashsale;
	}

	public void setIsCashsale(String isCashsale) {
		this.isCashsale = isCashsale;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(String expectedTime) {
		this.expectedTime = expectedTime;
	}

	public String getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(String requiredTime) {
		this.requiredTime = requiredTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(Double itemsValue) {
		this.itemsValue = itemsValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<RequestOrderItem> getItems() {
		return items;
	}

	public void setItems(List<RequestOrderItem> items) {
		this.items = items;
	}
	
}
