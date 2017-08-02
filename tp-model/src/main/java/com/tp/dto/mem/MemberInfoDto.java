package com.tp.dto.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.dto.promoter.PromoterInfoMobileDTO;
import com.tp.model.mem.MemberDetail;

public class MemberInfoDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3919359629875902199L;
	private Long time;
	private String username;

	private Boolean sex;

	private String nl;

	private Long uid;
	private String email;
	private String mobile;
	private String lastLoginTime;
	private ResultCode code;
	private String enUserId;
	
	private Date createTime;
	private MemberDetail memberDetail;
	private String appLoginToken;
	private String nickName;
	private String promoterInfo;	
	private PromoterInfoMobileDTO promoterInfoMobile;

	
	
	public PromoterInfoMobileDTO getPromoterInfoMobile() {
		return promoterInfoMobile;
	}

	public void setPromoterInfoMobile(PromoterInfoMobileDTO promoterInfoMobile) {
		this.promoterInfoMobile = promoterInfoMobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = System.currentTimeMillis();
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}
	
	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public String getBasicInfo() {
		StringBuilder sb = new StringBuilder(">>> [");
		if (null != getUid()) {
			sb.append("uid=");
			sb.append(getUid());
			sb.append(",");
		}
		if (null != getEmail()) {
			sb.append("email=");
			sb.append(getEmail());
			sb.append(",");
		}
		if (null != getMobile()) {
			sb.append("mobile=");
			sb.append(getMobile());
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}


	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public ResultCode getCode() {
		return code;
	}

	public void setCode(ResultCode code) {
		this.code = code;
	}

	public String getEnUserId() {
		return enUserId;
	}

	public void setEnUserId(String enUserId) {
		this.enUserId = enUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public MemberDetail getMemberDetail() {
		return memberDetail;
	}

	public void setMemberDetail(MemberDetail memberDetail) {
		this.memberDetail = memberDetail;
	}

	public String getAppLoginToken() {
		return appLoginToken;
	}

	public void setAppLoginToken(String appLoginToken) {
		this.appLoginToken = appLoginToken;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPromoterInfo() {
		return promoterInfo;
	}

	public void setPromoterInfo(String promoterInfo) {
		this.promoterInfo = promoterInfo;
	}
}
