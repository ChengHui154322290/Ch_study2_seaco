package com.tp.m.vo.pay;



/**
 * WAP的支付宝支付信息
 * @author zhuss
 * @2016年1月8日 上午11:14:53
 */
public class WAPGJZFBPayVO extends BasePayVO{

	private static final long serialVersionUID = 6059331472373503829L;
	private String actionurl;
    private String service;
    private String partner;
    private String inputcharset;
    private String signtype;
    private String notifyurl;
    private String returnurl;
    private String outtradeno;
    private String currency;
    private String merchanturl;
    private String subject;
    private String rmbfee;
    private String body;
    private String productcode;
    // 分账信息
    private String splitfundinfo;
    private String sign;
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
	public String getInputcharset() {
		return inputcharset;
	}
	public void setInputcharset(String inputcharset) {
		this.inputcharset = inputcharset;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMerchanturl() {
		return merchanturl;
	}
	public void setMerchanturl(String merchanturl) {
		this.merchanturl = merchanturl;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRmbfee() {
		return rmbfee;
	}
	public void setRmbfee(String rmbfee) {
		this.rmbfee = rmbfee;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getSplitfundinfo() {
		return splitfundinfo;
	}
	public void setSplitfundinfo(String splitfundinfo) {
		this.splitfundinfo = splitfundinfo;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
