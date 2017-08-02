/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class PromoterInfoVO implements BaseVO{
	
	private static final long serialVersionUID = 6557988835680001492L;
	
	private Long promoterid;
	
	/** 上级分销编号 数据类型bigint(14) */
	private Long parentPromoterId;
	private Long userid;
	/*-------------基本信息-----------------*/
	private String name;				//姓名
	private String nickname;			//昵称
	private Integer type;				//类型
	private Integer status;				//账户状态：0未开通 1已开通未认证 2 开通店铺已认证 3店铺被禁用
	private String passtime;			//开通时间
	private String gender;				//性别
	private Long age;					//年龄
	private String mobile;				//手机号
	private String qq;					//qq
	private String weixin;				//微信
	private String email;				//邮箱
	private String credential;			//身份证or居民证
	private String credentialcode;		//证件号码
	
	/*------------银行信息------------------*/
	private String bank;				//银行 名称
	private String bankaccount;			//银行账户
	
	private String alipay;			//支付宝
	
	/*------------佣金信息-------------------*/
	private String totalfees; 			//累计佣金
	private String withdrawedfees;		//已提现佣金
	private String surplusamount;		//未提现佣金	
	private String referralfees; 		//拉新佣金总额
	
	/*------------订单数据------------------*/
	private String orderamount;	 		//订单总金额
	private String ordercount;   		//订单总数量
	
	/*------------卡券信息------------------*/
	private String couponamount; 		//卡券总金额	
	private Integer couponcount;			//卡券总数量
	private Integer cpusedcount;		//卡券已使用数量
	private Integer cpexchangedcount;	//卡券已兑换数量
	private Integer cpunexchangecount;	//卡券未兑换数量
	
	/*------------其他信息-------------------*/
	private Integer withdrawstatus;		//提现状态：0可提现1提现中 2不可提现
	private String  withdrawingamount;	//正在提现的金额
	private Integer totalCustomers; 	// 客户总数
	private String  shareImage;//带背景的图片
	
	/*---------------------------------------*/
	private String paycode;				//支付的订单号
	private String payamount; //支付金额
	
	/*------------扫码信息------------------*/
	private Integer scancount;
	private String imgurl;
	
	/*------------店铺二维码------------------*/
	private String img;
	/** 扫码二维码带logo图片七牛http路径 **/
	private String qrImagePath;
	/** 带背景的二维码店铺分享七牛http路径**/
	private String shareImpagePath;
	/**扫码推广图二维码*/
	private String promoterImagePath;
	/**带背景的扫码推广图二维码*/
	private String promoterShareImagePath;

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Integer getScancount() {
		return scancount;
	}

	public void setScancount(Integer scancount) {
		this.scancount = scancount;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaycode() {
		return paycode;
	}
	
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	public Long getPromoterid() {
		return promoterid;
	}
	public void setPromoterid(Long promoterid) {
		this.promoterid = promoterid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}	
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getCredential() {
		return credential;
	}
	public void setCredential(String credential) {
		this.credential = credential;
	}
	public String getCredentialcode() {
		return credentialcode;
	}
	public void setCredentialcode(String credentialcode) {
		this.credentialcode = credentialcode;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getTotalfees() {
		return totalfees;
	}
	public void setTotalfees(String totalfees) {
		this.totalfees = totalfees;
	}
	public String getWithdrawedfees() {
		return withdrawedfees;
	}
	public void setWithdrawedfees(String withdrawedfees) {
		this.withdrawedfees = withdrawedfees;
	}
	public String getSurplusamount() {
		return surplusamount;
	}
	public void setSurplusamount(String surplusamount) {
		this.surplusamount = surplusamount;
	}
	public String getReferralfees() {
		return referralfees;
	}
	public void setReferralfees(String referralfees) {
		this.referralfees = referralfees;
	}
	public String getOrderamount() {
		return orderamount;
	}
	public void setOrderamount(String orderamount) {
		this.orderamount = orderamount;
	}
	public String getOrdercount() {
		return ordercount;
	}
	public void setOrdercount(String ordercount) {
		this.ordercount = ordercount;
	}
	public String getCouponamount() {
		return couponamount;
	}
	public Integer getCouponcount() {
		return couponcount;
	}
	public void setCouponcount(Integer couponcount) {
		this.couponcount = couponcount;
	}
	public void setCouponamount(String couponamount) {
		this.couponamount = couponamount;
	}
	public String getPasstime() {
		return passtime;
	}
	public void setPasstime(String passtime) {
		this.passtime = passtime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getWithdrawstatus() {
		return withdrawstatus;
	}
	public void setWithdrawstatus(Integer withdrawstatus) {
		this.withdrawstatus = withdrawstatus;
	}
	
	
	public Integer getTotalCustomers() {
		return totalCustomers;
	}
	public void setTotalCustomers(Integer totalCustomers) {
		this.totalCustomers = totalCustomers;
	}
	
	public String getWithdrawingamount() {
		return withdrawingamount;
	}
	public void setWithdrawingamount(String withdrawingamount) {
		this.withdrawingamount = withdrawingamount;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getCpusedcount() {
		return cpusedcount;
	}
	public void setCpusedcount(Integer cpusedcount) {
		this.cpusedcount = cpusedcount;
	}
	public Integer getCpexchangedcount() {
		return cpexchangedcount;
	}
	public void setCpexchangedcount(Integer cpexchangedcount) {
		this.cpexchangedcount = cpexchangedcount;
	}
	public Integer getCpunexchangecount() {
		return cpunexchangecount;
	}
	public void setCpunexchangecount(Integer cpunexchangecount) {
		this.cpunexchangecount = cpunexchangecount;
	}

	public String getShareImage() {
		return shareImage;
	}

	public void setShareImage(String shareImage) {
		this.shareImage = shareImage;
	}

	public String getQrImagePath() {
		return qrImagePath;
	}

	public void setQrImagePath(String qrImagePath) {
		this.qrImagePath = qrImagePath;
	}

	public String getShareImpagePath() {
		return shareImpagePath;
	}

	public void setShareImpagePath(String shareImpagePath) {
		this.shareImpagePath = shareImpagePath;
	}

	public String getPromoterImagePath() {
		return promoterImagePath;
	}

	public void setPromoterImagePath(String promoterImagePath) {
		this.promoterImagePath = promoterImagePath;
	}

	public String getPromoterShareImagePath() {
		return promoterShareImagePath;
	}

	public void setPromoterShareImagePath(String promoterShareImagePath) {
		this.promoterShareImagePath = promoterShareImagePath;
	}

	public Long getParentPromoterId() {
		return parentPromoterId;
	}

	public void setParentPromoterId(Long parentPromoterId) {
		this.parentPromoterId = parentPromoterId;
	}	
	
}
