package com.tp.m.query.promotion;


import java.util.List;

import com.tp.m.base.BaseQuery;

/**
 * 优惠券入参
 * @author zhuss
 * @2016年1月4日 下午8:48:31
 */
public class QueryCoupon extends BaseQuery{

	private static final long serialVersionUID = 5744190481699385926L;

	private String cid;//优惠券ID
	private String ccode;//优惠券编码
	private List<String> ccodes;//优惠券集合
	private String couponId;//兑换码批次
	private String tel;//手机号
	private String type;//类型 1  个人中心 2 可领取列表 3 订单中可使用
	private String status;//优惠券状态： 1 可用 2已使用 3已过期
	private Integer offerType;//是否可以领取
	private String captcha;//短信验证码 - 用于手机领取优惠券使用
	
	private String uuid;//立即购买标识
	
	private String sendtype;//1 新用户自动发放2 分享自动发放
	
	private String point;//现金券兑换成积分
	
	private String channelCode;//渠道代码
	private String mobile;//用户所有ID
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	public List<String> getCcodes() {
		return ccodes;
	}
	public void setCcodes(List<String> ccodes) {
		this.ccodes = ccodes;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSendtype() {
		return sendtype;
	}
	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public Integer getOfferType() {
		return offerType;
	}
	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
