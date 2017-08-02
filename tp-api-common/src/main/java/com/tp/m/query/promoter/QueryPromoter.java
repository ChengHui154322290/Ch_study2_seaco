package com.tp.m.query.promoter;

import java.util.Date;

import com.tp.m.base.BaseQuery;

public class QueryPromoter extends BaseQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7770690374187253295L;
	/** 推广员id **/
	private Long promoterid;
	/** 推广员类型 **/
	private String type;
	/** 推广员级别 **/
	private String level;
	/** 订单状态 **/
	private String orderstatus;
	/** 开始时间 **/
	private String startdate;
	/** 结束时间 **/
	private String enddate;
	/** 优惠券编号 **/
	private String couponcode;
	/** 优惠券状态0全部1已消费2已兑换3未兑换 **/
	private Integer couponstatus;
	/** 订单号 **/
	private String ordercode;
	/** 每页个数 **/
	private Integer pagesize = 10;
	/** 分销员姓名 **/
	private String promoterName;
	/** 证件类型 **/
	private Integer credentialType;
	/** 证件号码 **/
	private String credentialCode;
	/** 银行名称 **/
	private String bankName;
	/** 银行账号 **/
	private String bankAccount;
	/** 支付宝账号 **/
	private String alipay;
	/** 本次提现金额 **/
	private Double curWithdraw;

	/** QQ 数据类型varchar(12) */
	private String qq;

	/** 微信 数据类型varchar(32) */
	private String weixin;

	/** 邮箱 **/
	private String email;

	/** 邀请者 **/
	private String inviter;
	/** 店铺 **/
	private String shop;
	/** 优先级 0:token 1: shop**/	
	private Integer priority;

	/**二级分销员ID**/	
	private Long sonpromoterid;
		
	/**专题类型**/	
	private Integer topictype;
	
	
	/**提现方式**/	
	private Integer withdrawType;
	/**电话号码**/		
	private  String userMobile;
	
	/** 扫码二维码带logo图片七牛http路径 **/
	private String qrImagePath;
	/** 带背景的二维码店铺分享七牛http路径**/
	private String shareImpagePath;
	/**扫码推广图二维码*/
	private String promoterImagePath;
	/**带背景的扫码推广图二维码*/
	private String promoterShareImagePath;
	/**邀请码*/
	private String inviteCode;
	/** 密码**/
	private String password;
	/**验证码*/
	private Integer realSmsCode;
	/**顶级分销员ID*/
	private Long topPromoterId;
	
	/**开始时间*/
	private Date createBeginTime;
	/**结束时间*/
	private Date createEndTime;
	public Integer getRealSmsCode() {
		return realSmsCode;
	}

	public void setRealSmsCode(Integer realSmsCode) {
		this.realSmsCode = realSmsCode;
	}

	public String getUserMobile() {
		return userMobile;
	}
   
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Integer getWithdrawType() {
		return withdrawType;
	}

	public void setWithdrawType(Integer withdrawType) {
		this.withdrawType = withdrawType;
	}

	public Integer getTopictype() {
		return topictype;
	}

	public void setTopictype(Integer topictype) {
		this.topictype = topictype;
	}

	public Long getSonpromoterid() {
		return sonpromoterid;
	}

	public void setSonpromoterid(Long sonpromoterid) {
		this.sonpromoterid = sonpromoterid;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getCurWithdraw() {
		return curWithdraw;
	}

	public void setCurWithdraw(Double curWithdraw) {
		this.curWithdraw = curWithdraw;
	}

	public String getCredentialCode() {
		return credentialCode;
	}

	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public Integer getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(Integer credentialType) {
		this.credentialType = credentialType;
	}

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPromoterid() {
		return promoterid;
	}

	public void setPromoterid(Long promoterid) {
		this.promoterid = promoterid;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public Integer getCouponstatus() {
		return couponstatus;
	}

	public void setCouponstatus(Integer couponstatus) {
		this.couponstatus = couponstatus;
	}

	public String getCouponcode() {
		return couponcode;
	}

	public void setCouponcode(String couponcode) {
		this.couponcode = couponcode;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
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

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getTopPromoterId() {
		return topPromoterId;
	}

	public void setTopPromoterId(Long topPromoterId) {
		this.topPromoterId = topPromoterId;
	}

	public Date getCreateBeginTime() {
		return createBeginTime;
	}

	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

    
	
}
