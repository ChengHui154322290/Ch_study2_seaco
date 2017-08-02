package com.tp.m.vo.pay;


/**
 * APP的微信支付信息
 * @author zhuss
 * @2016年1月8日 上午11:12:49
 */
public class APPWXPayVO extends BasePayVO{
	
	private static final long serialVersionUID = 1281980799249635214L;
	private String appid; //公众账号ID
	private String partnerid; //商户号
	private String prepayid;//预支付id
	private String signedinfo; //签名后的信息
	private String noncestr;//随机串
	private String timestart;//交易起始时间,取出的数据为yyyyMMdd,先转成时间戳(秒)

	

	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getSignedinfo() {
		return signedinfo;
	}
	public void setSignedinfo(String signedinfo) {
		this.signedinfo = signedinfo;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestart() {
		return timestart;
	}
	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}
}
