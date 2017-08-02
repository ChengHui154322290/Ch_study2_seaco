package com.tp.m.vo.pay;



/**
 * WAP的支付宝支付信息
 * @author zhuss
 * @2016年1月8日 上午11:14:53
 */
public class WAPZFBPayVO extends BasePayVO{

	private static final long serialVersionUID = 6059331472373503829L;
	private String actionurl;
	private String service;
	private String partner;
	private String sellerid;
	private String inputcharset;
	private String paymenttype;
	private String notifyurl;
	private String returnurl;
	private String outtradeno;
	private String subject;
	private String totalfee;
	private String showurl;
	private String sign;
	private String signtype;
	
	public String getActionurl() {
		return actionurl;
	}
	public void setActionurl(String actionurl) {
		this.actionurl = actionurl;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getSellerid() {
		return sellerid;
	}
	public void setSellerid(String sellerid) {
		this.sellerid = sellerid;
	}
	public String getInputcharset() {
		return inputcharset;
	}
	public void setInputcharset(String inputcharset) {
		this.inputcharset = inputcharset;
	}
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}
	public String getReturnurl() {
		return returnurl;
	}
	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}
	public String getOuttradeno() {
		return outtradeno;
	}
	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}
	public String getShowurl() {
		return showurl;
	}
	public void setShowurl(String showurl) {
		this.showurl = showurl;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
}
