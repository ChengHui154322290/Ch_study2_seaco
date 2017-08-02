package com.tp.dto.stg;

import java.io.Serializable;

public class WarehouseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -297291549949161938L;
	/** 供应商名称 */
	private String spName;
	/** 供应商id 0 表示自营仓库 */
	private Long spId;
	/** 机构 */
	private Long districtId;
	/** 仓库地址 */
	private String address;
	/** 仓库编号 */
	private String code;
	/** 仓库名称 */
	private String name;
	/** 邮编 */
	private String zipCode;
	/** 联系人 */
	private String linkman;
	/** 电话 */
	private String phone;
	/**配送地区，多个地区id用逗号隔开，为0表示全国*/
	private String deliverAddr;
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDeliverAddr() {
		return deliverAddr;
	}
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
