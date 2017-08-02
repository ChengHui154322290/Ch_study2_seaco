package com.tp.dto.mem;

import java.io.Serializable;

import com.tp.common.vo.mem.MemberUnionType;
import com.tp.enums.common.PlatformEnum;
import com.tp.enums.common.SourceEnum;

public class MemCallDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9181635729047054590L;
	// 登录名称
	private String userName;
	// 登录密码
	private String password;
	// 昵称
	private String nickName;
	// 客户端IP
	private String ip;
	// 登录来源
	private SourceEnum source;
	// 登录平台
	private PlatformEnum platform;
	// 手机
	private String mobile;
	// 手机验证码
	private Integer smsCode;
	// 联合登录值
	private String unionVal;
	// 联合登录类型
	private MemberUnionType unionType;
	
	//店铺ID
	private Long shopPromoterId;

	/**回写的会员ID*/
	private Long memberId;

	//注册渠道
	private String channelCode;
	//推荐用户OPENID
	private String tpin;
	// 扫码关注promoterID
	private Long scanPromoterId;
	
	// 头像图片地址
	private String avatarUrl;
	//广告来源
	private String advertFrom;

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public SourceEnum getSource() {
		return source;
	}
	public void setSource(SourceEnum source) {
		this.source = source;
	}
	public PlatformEnum getPlatform() {
		return platform;
	}
	public void setPlatform(PlatformEnum platform) {
		this.platform = platform;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(Integer smsCode) {
		this.smsCode = smsCode;
	}
	public String getUnionVal() {
		return unionVal;
	}
	public void setUnionVal(String unionVal) {
		this.unionVal = unionVal;
	}
	public MemberUnionType getUnionType() {
		return unionType;
	}
	public void setUnionType(MemberUnionType unionType) {
		this.unionType = unionType;
	}
	public Long getShopPromoterId() {
		return shopPromoterId;
	}
	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public Long getScanPromoterId() {
		return scanPromoterId;
	}

	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
   
	public String getAdvertFrom() {
		return advertFrom;
	}

	public void setAdvertFrom(String advertFrom) {
		this.advertFrom = advertFrom;
	}

	@Override
	public String toString() {
		return "MemCallDto [userName=" + userName + ", password=" + password + ", nickName=" + nickName + ", ip=" + ip
				+ ", source=" + source + ", platform=" + platform + ", mobile=" + mobile + ", smsCode=" + smsCode
				+ ", unionVal=" + unionVal + ", unionType=" + unionType + ", shopPromoterId=" + shopPromoterId
				+ ", memberId=" + memberId + ", channelCode=" + channelCode + ", tpin=" + tpin + ", avatarUrl="
				+ avatarUrl + "+, advertFrom="
				+ advertFrom+ "]";
	}
}
