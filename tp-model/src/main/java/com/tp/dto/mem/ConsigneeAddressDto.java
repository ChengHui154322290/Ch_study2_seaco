package com.tp.dto.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

public class ConsigneeAddressDto extends BaseDO implements Serializable {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 5748110753888039313L;


	/** 主键 */
	private Long id;

	/** 用户编号 */
	private Long userId;

	/** 收货人 */
	private String name;

	/** 收货人-手机号码 */
	private String mobile;

	/** 收货人-固定电话 */
	private String phone;

	/** 收货人-email */
	private String email;

	/** 收货地址-省份 */
	private Long provinceId;

	/** 省份-描述-用于展示 */
	private String province;

	/** 收货地址-城市 */
	private Long cityId;

	/** 城市-描述-用于展示 */
	private String city;

	/** 收货地址-区县 */
	private Long countyId;

	/** 区县-描述-用于展示 */
	private String county;

	/** 收货地址-街道 */
	private Long streetId;
	
	/** 街道-描述-用于展示 */
	private String street;

	/** 收货地址-详细地址 */
	private String address;

	/** 收货地址-邮编 */
	private String zipCode;

	/** 创建时间 */
	private Date createTime;

	/** 更新时间 */
	private Date updateTime;
	
	/* 收货时间 */	
	private Long receiptTime;

	/** 是否为默认收货地址 */
	private Boolean isDefault;
	
	/** 收货时间 */
	private String receiptTimeDesc;
	
	/**  */
	private Boolean state;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 用户编号
	 * 
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 设置 收货人
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置 收货人-手机号码
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 设置 收货人-固定电话
	 * 
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 设置 收货人-email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 设置 收货地址-省份
	 * 
	 * @param provinceId
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * 设置 省份-描述-用于展示
	 * 
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 设置 收货地址-城市
	 * 
	 * @param cityId
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * 设置 城市-描述-用于展示
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 设置 收货地址-区县
	 * 
	 * @param countyId
	 */
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	/**
	 * 设置 区县-描述-用于展示
	 * 
	 * @param county
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * 设置 收货地址-街道
	 * 
	 * @param streetId
	 */
	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	/**
	 * 设置 收货地址-详细地址
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 设置 收货地址-邮编
	 * 
	 * @param zipCode
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 设置 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置 更新时间
	 * 
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 设置 是否为默认收货地址
	 * 
	 * @param isDefault
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 用户编号
	 * 
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 获取 收货人
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取 收货人-手机号码
	 * 
	 * @return mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 获取 收货人-固定电话
	 * 
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 获取 收货人-email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 获取 收货地址-省份
	 * 
	 * @return provinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}

	/**
	 * 获取 省份-描述-用于展示
	 * 
	 * @return province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 获取 收货地址-城市
	 * 
	 * @return cityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * 获取 城市-描述-用于展示
	 * 
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 获取 收货地址-区县
	 * 
	 * @return countyId
	 */
	public Long getCountyId() {
		return countyId;
	}

	/**
	 * 获取 区县-描述-用于展示
	 * 
	 * @return county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * 获取 收货地址-街道
	 * 
	 * @return streetId
	 */
	public Long getStreetId() {
		return streetId;
	}

	/**
	 * 获取 收货地址-详细地址
	 * 
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 获取 收货地址-邮编
	 * 
	 * @return zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取 更新时间
	 * 
	 * @return updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 获取 是否为默认收货地址
	 * 
	 * @return isDefault
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 
	 *获取 收货时间
	 *
	 * @returnreceiptTime
	 */
	public Long getReceiptTime() {
		return receiptTime;
	}

	/**
	 * 
	 *设置 收货时间 
	 *
	 * @returnreceiptTime
	 */
	public void setReceiptTime(Long receiptTime) {
		this.receiptTime = receiptTime;
	}

	public String getReceiptTimeDesc() {
		return receiptTimeDesc;
	}

	public void setReceiptTimeDesc(String receiptTimeDesc) {
		this.receiptTimeDesc = receiptTimeDesc;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String toString() {
		return "ConsigneeAddressDto [id=" + id + ", userId=" + userId
				+ ", name=" + name + ", mobile=" + mobile + ", phone=" + phone
				+ ", email=" + email + ", provinceId=" + provinceId
				+ ", province=" + province + ", cityId=" + cityId + ", city="
				+ city + ", countyId=" + countyId + ", county=" + county
				+ ", streetId=" + streetId + ", street=" + street
				+ ", address=" + address + ", zipCode=" + zipCode
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", receiptTime=" + receiptTime + ", isDefault=" + isDefault
				+ ", receiptTimeDesc=" + receiptTimeDesc + ", state=" + state
				+ "]";
	}
	
}
