package com.tp.m.query.user;

import com.tp.m.base.BaseQuery;

public class QueryUser extends BaseQuery{

	private static final long serialVersionUID = -7978348649506738984L;

	private String loginname;//登录用户
	private String nickname;//昵称
	private String headimg;//头像
	private String pwd;//密码
	private String tel;//手机
	private String type;//验证码类型：1注册 2修改(忘记)密码 3绑定手机 11换绑手机
	private String captcha;//验证码
	private String uniontype;//联合登录的类型
	private String unionval;//联合登录的值
	private String shopPromoterId;//店铺ID
	private String shopMobile;//店主手机号
	private String channelcode;//注册渠道
	private String tpin;//推荐用户OPENID
	private Long scanPromoterId;//扫码推广ID
	private String graphic; //图形验证码
	private String advertFrom;//广告来源

	private String openId;
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getUniontype() {
		return uniontype;
	}
	public void setUniontype(String uniontype) {
		this.uniontype = uniontype;
	}
	public String getUnionval() {
		return unionval;
	}
	public void setUnionval(String unionval) {
		this.unionval = unionval;
	}
	public String getShopPromoterId() {
		return shopPromoterId;
	}
	public void setShopPromoterId(String shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
	public String getShopMobile() {
		return shopMobile;
	}

	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}

	public Long getScanPromoterId() {
		return scanPromoterId;
	}

	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}

	public String getGraphic() {
		return graphic;
	}

	public void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	public String getAdvertFrom() {
		return advertFrom;
	}

	public void setAdvertFrom(String advertFrom) {
		this.advertFrom = advertFrom;
	}
	
	
}
