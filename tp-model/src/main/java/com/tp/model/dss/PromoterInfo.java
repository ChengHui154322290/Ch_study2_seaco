package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.MemberConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;

/**
 * @author szy 分销员信息
 */
public class PromoterInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594219L;

	/** 分销员编号 数据类型bigint(12) */
	@Id
	private Long promoterId;

	/** 关联会员 数据类型bigint(14) */
	private Long memberId;

	/** 真实姓名 数据类型varchar(16) */
	private String promoterName;

	/** 密码 数据类型varchar(32) */
	private String passWord;

	/** 密码盐 数据类型varchar(32) */
	private String salt;

	/** 状态(0:未开通，1: 已开通，其它: 禁用) 数据类型tinyint(1) */
	private Integer promoterStatus;

	/** 类型(0:卡券推广，1：店铺分销, 2:扫码关注) 数据类型tinyint(1) */
	private Integer promoterType;

	/** 性别(0:女，1：男) 数据类型tinyint(1) */
	private Integer gender;

	/** 年龄 数据类型datetime */
	private Date birthday;

	/** 手机号 数据类型varchar(15) */
	private String mobile;

	/** QQ 数据类型varchar(12) */
	private String qq;

	/** 微信 数据类型varchar(32) */
	private String weixin;

	/** 邀请码 数据类型varchar(32) */
	private String inviteCode;

	/** 分销员层级 数据类型tiny(2) */
	private Integer promoterLevel;

	/** 上级分销编号 数据类型bigint(14) */
	private Long parentPromoterId;
   
	/** 证件类型(1:身份证，2：居住证，默认为1) 数据类型tinyint(1) */
	private Integer credentialType;

	/** 证件号码 数据类型varchar(32) */
	private String credentialCode;

	/** 开户银行 数据类型varchar(32) */
	private String bankName;

	/** 银行卡号 数据类型varchar(32) */
	private String bankAccount;

	/** 佣金比率 数据类型float(4,2) */
	private Float commisionRate;

	/** 拉新佣金 数据类型double(8,2) */
	private Double referralUnit;

	/** 累计佣金 数据类型double(10,2) */
	private Double accumulatedAmount;

	/** 剩余佣金 数据类型double(6,2) */
	private Double surplusAmount;

	/** 拉新佣金总额 */
	private Double referralFees;

	/** 是否页面显示名称 数据类型tinyint(1) */
	private Integer pageShow;

	private Date passTime;

	/** 创建者 数据类型varchar(32) */
	private String createUser;

	/** 创建时间 数据类型datetime */
	private Date createTime;

	/** 更新者 数据类型varchar(32) */
	private String updateUser;

	/** 更新时间 数据类型datetime */
	private Date updateTime;

	/** 自定义店铺名称 **/
	private String nickName;

	/** 邮箱 **/
	private String email;

	/** 支付宝账号 **/
	private String alipay;

	/** 扫码关注图片 **/
	private String scanAttentionImage;
	/** 扫码二维码带logo图片七牛http路径 **/
	private String qrImagePath;
	/** 带背景的二维码店铺分享七牛http路径 **/
	private String shareImagePath;
	/** 扫码推广图二维码 */
	private String promoterImagePath;
	/** 带背景的扫码推广图二维码 */
	private String promoterShareImagePath;

	/** 店铺对应渠道CODE */
	private String channelCode;

	/** 渠道分享标题 */
	@Virtual
	private String shareTitle;

	/** 渠道分享内容 */
	@Virtual
	private String shareContent;

	/** 第三方商城分销类型：0无分销1店铺分销 */
	@Virtual
	private Integer companyDssType;

	@Virtual
	/** 邀请人手机号 **/
	private String inviter;
	@Virtual
	private String captcha;

	/** 订单总数 */
	@Virtual
	private Integer orderCount;
	@Virtual
	private Double orderAmount;
	@Virtual
	private Integer couponCount;

	// 是否同意西客用户协议 同意：true; 不同意: false
	@Virtual
	private Boolean userAgreed;

	// 客户总数
	@Virtual
	private Integer totalCustomers;

	private Long topPromoterId;
	@Virtual
	private Double dssRegisterAmount;
	@Virtual
	private String inviteCodeUsed;
	
	@Virtual
	private Date startTime;
	@Virtual
	private Date endTime;

	public Long getBizCode() {
		Date currentDate = createTime;
		if (currentDate == null)
			currentDate = new Date();
		Long lPromoteId = promoterId;
		if (lPromoteId == null)
			lPromoteId = 0L;
		return Long.valueOf(
				PaymentConstant.BIZ_TYPE.DSS.code + DateUtil.format(currentDate, DateUtil.SHORT_FORMAT) + lPromoteId);
	}

	public Long getPromoterIdByBizCode(Long bizCode) {

		if (bizCode != null && bizCode.toString().length() > 9) {
			return Long.valueOf(bizCode.toString().substring(9));
		}
		return null;
	}

	public Double getOrderCommision() {
		return BigDecimalUtil
				.formatToPrice(BigDecimalUtil.divide(BigDecimalUtil.multiply(orderAmount, commisionRate), 100))
				.doubleValue();
	}

	public Double getWithdrawAmount() {
		return BigDecimalUtil.formatToPrice(BigDecimalUtil.subtract(accumulatedAmount, surplusAmount)).doubleValue();
	}

	public String getPromoterStatusCn() {
		return DssConstant.PROMOTER_STATUS.getCnName(promoterStatus);
	}

	public String getPromoterTypeCn() {
		return DssConstant.PROMOTER_TYPE.getCnName(promoterType);
	}

	public String getCredentialTypeCn() {
		return DssConstant.CARD_TYPE.getCnName(credentialType);
	}

	public String getGenderCn() {
		return MemberConstant.GENDER.getCnName(gender);
	}

	public String getPromoterLevelCn() {
		return DssConstant.PROMOTER_LEVEL.getCnName(promoterLevel);
	}

	public String getInviteCodeUsedCn() {
		return DssConstant.PROMOTER_SCAN_USE.getCnName(inviteCodeUsed);
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public String getPromoterName() {
		if (StringUtils.isBlank(promoterName)) {
			return nickName;
		}
		return promoterName;
	}

	public String getPassWord() {
		return passWord;
	}

	public Integer getPromoterStatus() {
		return promoterStatus;
	}

	public Integer getPromoterType() {
		return promoterType;
	}

	public Integer getGender() {
		return gender;
	}

	public String getMobile() {
		return mobile;
	}

	public String getQq() {
		return qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public Long getParentPromoterId() {
		return parentPromoterId;
	}

	public Integer getCredentialType() {
		return credentialType;
	}

	public String getCredentialCode() {
		return credentialCode;
	}

	public String getBankName() {
		return bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public Double getAccumulatedAmount() {
		return accumulatedAmount;
	}

	public Double getSurplusAmount() {
		return surplusAmount;
	}

	public Integer getPageShow() {
		return pageShow;
	}

	public String getCreateUser() {
		return createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public void setPromoterStatus(Integer promoterStatus) {
		this.promoterStatus = promoterStatus;
	}

	public void setPromoterType(Integer promoterType) {
		this.promoterType = promoterType;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public void setParentPromoterId(Long parentPromoterId) {
		this.parentPromoterId = parentPromoterId;
	}

	public void setCredentialType(Integer credentialType) {
		this.credentialType = credentialType;
	}

	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setAccumulatedAmount(Double accumulatedAmount) {
		this.accumulatedAmount = accumulatedAmount;
	}

	public void setSurplusAmount(Double surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	public void setPageShow(Integer pageShow) {
		this.pageShow = pageShow;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Float getCommisionRate() {
		return commisionRate;
	}

	public void setCommisionRate(Float commisionRate) {
		this.commisionRate = commisionRate;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Double getReferralFees() {
		return referralFees;
	}

	public void setReferralFees(Double referralFees) {
		this.referralFees = referralFees;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Date getPassTime() {
		return passTime==null?createTime:passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Integer getTotalCustomers() {
		return totalCustomers;
	}

	public void setTotalCustomers(Integer totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getScanAttentionImage() {
		return scanAttentionImage;
	}

	public void setScanAttentionImage(String scanAttentionImage) {
		this.scanAttentionImage = scanAttentionImage;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public Boolean getUserAgreed() {
		return userAgreed;
	}

	public void setUserAgreed(Boolean userAgreed) {
		this.userAgreed = userAgreed;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Long getTopPromoterId() {
		return topPromoterId;
	}

	public void setTopPromoterId(Long topPromoterId) {
		this.topPromoterId = topPromoterId;
	}

	public Double getDssRegisterAmount() {
		return dssRegisterAmount;
	}

	public void setDssRegisterAmount(Double dssRegisterAmount) {
		this.dssRegisterAmount = dssRegisterAmount;
	}

	public Integer getPromoterLevel() {
		return promoterLevel;
	}

	public void setPromoterLevel(Integer promoterLevel) {
		this.promoterLevel = promoterLevel;
	}

	public Double getReferralUnit() {
		return referralUnit;
	}

	public void setReferralUnit(Double referralUnit) {
		this.referralUnit = referralUnit;
	}

	public String getInviteCodeUsed() {
		return inviteCodeUsed;
	}

	public void setInviteCodeUsed(String inviteCodeUsed) {
		this.inviteCodeUsed = inviteCodeUsed;
	}

	public String getQrImagePath() {
		return qrImagePath;
	}

	public void setQrImagePath(String qrImagePath) {
		this.qrImagePath = qrImagePath;
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

	public String getShareImagePath() {
		return shareImagePath;
	}

	public void setShareImagePath(String shareImagePath) {
		this.shareImagePath = shareImagePath;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public Integer getCompanyDssType() {
		return companyDssType;
	}

	public void setCompanyDssType(Integer companyDssType) {
		this.companyDssType = companyDssType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
