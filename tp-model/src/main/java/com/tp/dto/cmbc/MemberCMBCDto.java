package com.tp.dto.cmbc;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.vo.mem.MemberUnionType;
import com.tp.model.mem.MemberDetail;

public class MemberCMBCDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8740366464595608257L;
	/**
	 * 
	 */

	// 联合登录值
	private String unionVal;
	// 联合登录类型
	private MemberUnionType unionType;

	private Long memberId;

	//推荐用户OPENID
	private String tpin;

	private String mobile;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

}
