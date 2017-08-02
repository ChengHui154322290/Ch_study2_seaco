package com.tp.dto.pay.postdata;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.tp.dto.pay.AppPayData;


public class KqPayPostData implements PayPostData, Serializable, AppPayData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6138764724508557251L;
	private String inputCharset;
	private String pageUrl;
	private String bgUrl;
	private String version;
	private String mobileGateway;
	private String language;
	private String signType;
	private String merchantAcctId;
	private String payerName;
	private String payerIdType;
	private String payerId;
	private String orderId;
	private String orderAmount;
	private String orderTime;
	private String productName;
	private String productNum;
	private String productId;
	private String productDesc;
	private String ext1;
	private String ext2;
	private String payType;
	private String bankId;
	private String redoFlag;
	private String pid;
	private String signMsg;

	private String actionUrl;

	public String getMsg2Sign() {
		StringBuilder result = new StringBuilder();
		result.append("inputCharset").append('=').append(inputCharset);
		if (StringUtils.isNotBlank(pageUrl)) {
			result.append('&').append("pageUrl").append('=').append(pageUrl);
		}
		if (StringUtils.isNotBlank(bgUrl)) {
			result.append('&').append("bgUrl").append('=').append(bgUrl);
		}
		if (StringUtils.isNotBlank(version)) {
			result.append('&').append("version").append('=').append(version);
		}
		if (StringUtils.isNotBlank(language)) {
			result.append('&').append("language").append('=').append(language);
		}
		if (StringUtils.isNotBlank(signType)) {
			result.append('&').append("signType").append('=').append(signType);
		}
		if (StringUtils.isNotBlank(merchantAcctId)) {
			result.append('&').append("merchantAcctId").append('=')
					.append(merchantAcctId);
		}
		if (StringUtils.isNotBlank(payerName)) {
			result.append('&').append("payerName").append('=')
					.append(payerName);
		}
		if (StringUtils.isNotBlank(payerIdType)) {
			result.append('&').append("payerIdType").append('=')
					.append(payerIdType);
		}
		if (StringUtils.isNotBlank(payerId)) {
			result.append('&').append("payerId").append('=').append(payerId);
		}
		if (StringUtils.isNotBlank(orderId)) {
			result.append('&').append("orderId").append('=').append(orderId);
		}
		if (StringUtils.isNotBlank(orderAmount)) {
			result.append('&').append("orderAmount").append('=')
					.append(orderAmount);
		}
		if (StringUtils.isNotBlank(orderTime)) {
			result.append('&').append("orderTime").append('=')
					.append(orderTime);
		}
		if (StringUtils.isNotBlank(productName)) {
			result.append('&').append("productName").append('=')
					.append(productName);
		}
		if (StringUtils.isNotBlank(productNum)) {
			result.append('&').append("productNum").append('=')
					.append(productNum);
		}
		if (StringUtils.isNotBlank(productId)) {
			result.append('&').append("productId").append('=')
					.append(productId);
		}
		if (StringUtils.isNotBlank(productDesc)) {
			result.append('&').append("productDesc").append('=')
					.append(productDesc);
		}
		if (StringUtils.isNotBlank(ext1)) {
			result.append('&').append("ext1").append('=').append(ext1);
		}
		if (StringUtils.isNotBlank(ext2)) {
			result.append('&').append("ext2").append('=').append(ext2);
		}
		if (StringUtils.isNotBlank(payType)) {
			result.append('&').append("payType").append('=').append(payType);
		}
		if (StringUtils.isNotBlank(bankId)) {
			result.append('&').append("bankId").append('=').append(bankId);
		}
		if (StringUtils.isNotBlank(redoFlag)) {
			result.append('&').append("redoFlag").append('=').append(redoFlag);
		}
		if (StringUtils.isNotBlank(pid)) {
			result.append('&').append("pid").append('=').append(pid);
		}
		if (StringUtils.isNotBlank(mobileGateway)) {
			result.append('&').append("mobileGateway").append('=')
					.append(mobileGateway);
		}

		return result.toString();
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getBgUrl() {
		return bgUrl;
	}

	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMerchantAcctId() {
		return merchantAcctId;
	}

	public void setMerchantAcctId(String merchantAcctId) {
		this.merchantAcctId = merchantAcctId;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getMobileGateway() {
		return mobileGateway;
	}

	public void setMobileGateway(String mobileGateway) {
		this.mobileGateway = mobileGateway;
	}

	public String getPayerIdType() {
		return payerIdType;
	}

	public void setPayerIdType(String payerIdType) {
		this.payerIdType = payerIdType;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getRedoFlag() {
		return redoFlag;
	}

	public void setRedoFlag(String redoFlag) {
		this.redoFlag = redoFlag;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public String getSignature() {
		return getSignMsg();
	}

	@Override
	public String getPaymentTradeNo() {
		return getOrderId();
	}

}
