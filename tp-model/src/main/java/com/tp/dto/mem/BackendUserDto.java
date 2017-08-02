package com.tp.dto.mem;

import java.util.ArrayList;
import java.util.List;

import com.tp.model.usr.UserInfo;


public class BackendUserDto extends UserInfo {

	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -2445745969953810958L;
	private String sex;
	private String birthday;
	private String trueName;
	private String idCardNo;
	private List<String> address = new ArrayList<String>();
	/** 优惠券数量 **/
	private Integer couponNum; 
	/** 代金券数量 **/
	private Integer voucherNum;
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public List<String> getAddress() {
		return address;
	}
	public void setAddress(List<String> address) {
		this.address = address;
	}
	public Integer getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}
	public Integer getVoucherNum() {
		return voucherNum;
	}
	public void setVoucherNum(Integer voucherNum) {
		this.voucherNum = voucherNum;
	}

}
