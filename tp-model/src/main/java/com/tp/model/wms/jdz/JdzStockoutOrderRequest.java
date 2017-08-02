/**
 * 
 */
package com.tp.model.wms.jdz;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class JdzStockoutOrderRequest implements Serializable{
	
	private static final long serialVersionUID = 2833251501227887406L;

	/**订单编号*/
	private String orderCode;
	
	/**仓库编号*/
	private String warehouseCode;
	
	/**来源类型*/
	private String fromType;
	
	/**来源单号*/
	private String fromCode;
	
	/**来源平台编码*/
	private String platformCode;
	
	/**来源平台名称*/
	private String platformName;
	
	/**是否紧急(0普通1紧急)*/
	private Integer isUrgency;
	
	/**下单时间yyyy-MM-dd HH:mm:ss*/
	private String orderTime;
	
	/**支付时间yyyy-MM-dd HH:mm:ss*/
	private String payTime;
	
	/**审核时间(生成通知单的时间)yyyy-MM-dd HH:mm:ss*/
	private String auditTime;
	
	/**审核人编码*/
	private String auditorCode;
	
	/**物流公司编码*/
	private String logisticsCompanyCode;
	
	/**物流公司名称*/
	private String logisticsCompanyName;
	
	/**邮费*/
	private String postage;
	
	/**是否货到付款(0是1否)*/
	private Integer isDeliveryPay;
	
	/**店铺编码*/
	private String shopCode;
	
	/**店铺名称*/
	private String shopName;
	
	/**会员昵称*/
	private String member;
	
	/**收货人姓名*/
	private String consignee;
	
	/**邮编*/
	private String postcode;
	
	/**省名称*/
	private String provinceName;
	
	/**市名称*/
	private String cityName;
	
	/**区名称*/
	private String areaName;
	
	/**收件地址*/
	private String address;
	
	/**移动电话*/
	private String mobile;
	
	/**固定电话*/
	private String tel;
	
	/**卖家留言*/
	private String sellersMessage;
	
	/**买家留言*/
	private String buyerMessage;
	
	/**商家留言*/
	private String merchantMessage;
	
	/**内部便签*/
	private String internalMemo;
	
	/**应收金额*/
	private Double amountReceivable;
	
	/**实际支付*/
	private Double actualPayment;
	
	/**邮费是否到付0是/1 否*/
	private Integer isPostagePay;
	
	/**是否需要开发票0是/1否*/
	private Integer isInvoice;
	
	/**发票抬头,多张发票用分号区分*/
	private String invoiceName;
	
	/**发票金额,多张发票用分号区分*/
	private String invoicePrice;
	
	/**发票内容,多张发票用分号区分*/
	private String invoiceText;
	
	/**快递单号*/
	private String expressNo;
	
	/**厂商编码(电商备案编码)*/
	private String providerCode;
	
	/**备用字段1*/
	private String remain1;
	
	/**备用字段2*/
	private String remain2;
	
	/**备用字段3*/
	private String remain3;
	
	/**备用字段4*/
	private String remain4;
	
	/**备用字段5*/
	private String remain5;
	
	/**账册编号*/
	private String goodsOwner;
	
	/** 详情 */
	private List<JdzStockoutOrderDetail> items;
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Integer getIsUrgency() {
		return isUrgency;
	}

	public void setIsUrgency(Integer isUrgency) {
		this.isUrgency = isUrgency;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditorCode() {
		return auditorCode;
	}

	public void setAuditorCode(String auditorCode) {
		this.auditorCode = auditorCode;
	}

	public String getLogisticsCompanyCode() {
		return logisticsCompanyCode;
	}

	public void setLogisticsCompanyCode(String logisticsCompanyCode) {
		this.logisticsCompanyCode = logisticsCompanyCode;
	}

	public String getLogisticsCompanyName() {
		return logisticsCompanyName;
	}

	public void setLogisticsCompanyName(String logisticsCompanyName) {
		this.logisticsCompanyName = logisticsCompanyName;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public Integer getIsDeliveryPay() {
		return isDeliveryPay;
	}

	public void setIsDeliveryPay(Integer isDeliveryPay) {
		this.isDeliveryPay = isDeliveryPay;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getSellersMessage() {
		return sellersMessage;
	}

	public void setSellersMessage(String sellersMessage) {
		this.sellersMessage = sellersMessage;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getMerchantMessage() {
		return merchantMessage;
	}

	public void setMerchantMessage(String merchantMessage) {
		this.merchantMessage = merchantMessage;
	}

	public String getInternalMemo() {
		return internalMemo;
	}

	public void setInternalMemo(String internalMemo) {
		this.internalMemo = internalMemo;
	}

	public Double getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(Double amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public Double getActualPayment() {
		return actualPayment;
	}

	public void setActualPayment(Double actualPayment) {
		this.actualPayment = actualPayment;
	}

	public Integer getIsPostagePay() {
		return isPostagePay;
	}

	public void setIsPostagePay(Integer isPostagePay) {
		this.isPostagePay = isPostagePay;
	}

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoicePrice() {
		return invoicePrice;
	}

	public void setInvoicePrice(String invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	public String getInvoiceText() {
		return invoiceText;
	}

	public void setInvoiceText(String invoiceText) {
		this.invoiceText = invoiceText;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getRemain1() {
		return remain1;
	}

	public void setRemain1(String remain1) {
		this.remain1 = remain1;
	}

	public String getRemain2() {
		return remain2;
	}

	public void setRemain2(String remain2) {
		this.remain2 = remain2;
	}

	public String getRemain3() {
		return remain3;
	}

	public void setRemain3(String remain3) {
		this.remain3 = remain3;
	}

	public String getRemain4() {
		return remain4;
	}

	public void setRemain4(String remain4) {
		this.remain4 = remain4;
	}

	public String getRemain5() {
		return remain5;
	}

	public void setRemain5(String remain5) {
		this.remain5 = remain5;
	}

	public String getGoodsOwner() {
		return goodsOwner;
	}

	public void setGoodsOwner(String goodsOwner) {
		this.goodsOwner = goodsOwner;
	}

	public List<JdzStockoutOrderDetail> getItems() {
		return items;
	}

	public void setItems(List<JdzStockoutOrderDetail> items) {
		this.items = items;
	}
}
