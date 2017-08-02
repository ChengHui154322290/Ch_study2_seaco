package com.tp.dto.pay.appdata;

import java.io.Serializable;

import com.tp.dto.pay.AppPayData;

public class AliAppPayData implements AppPayData, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7195101843969108814L;
	private String partner;
	private String seller;
	private String signedString;
	private String tradeNo;
	private String productName;
	private String amount;
	private String notifyUrl;
	private String orderInfo;

	public AliAppPayData(String partner, String seller, String signedString, String tradeNo, String productName, String amount, String notifyUrl) {
		this.partner = partner;
		this.seller = seller;
		this.signedString = signedString;
		this.tradeNo = tradeNo;
		this.productName = productName;
		this.amount = amount;
		this.notifyUrl = notifyUrl;
	}

	public AliAppPayData() {
		super();
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getSignedString() {
		return signedString;
	}

	public void setSignedString(String signedString) {
		this.signedString = signedString;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getOrderMessage2Sign() {
		StringBuilder msg = new StringBuilder();
		msg.append("partner=\"").append(partner).append('"');
		msg.append("&seller_id=\"").append(seller).append('"');
		msg.append("&out_trade_no=\"").append(tradeNo).append('"');
		if (productName != null && !productName.equals("")) {
			msg.append("&subject=\"").append(productName).append('"');
			msg.append("&body=\"").append(productName).append('"');
		}
		if (amount != null && !amount.equals("")) {
			msg.append("&total_fee=\"").append(amount).append('"');
		}
		if (notifyUrl != null && !notifyUrl.equals("")) {
			 msg.append("&notify_url=\"").append(notifyUrl).append('"');
//			msg.append("&notify_url=\"").append("http://www.xxx.com").append('"');
		}
		msg.append("&service=\"mobile.securitypay.pay\"");
		msg.append("&payment_type=\"1\"");
		msg.append("&_input_charset=\"utf-8\"");
		msg.append("&it_b_pay=\"30m\"");
		msg.append("&show_url=\"m.alipay.com\"");

		return msg.toString();
	}

	@Override
	public String getPaymentTradeNo() {
		return tradeNo;
	}
}
