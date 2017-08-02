package com.tp.dto.map;

import java.io.Serializable;

/**
 * 地理编码信息列表
 * @author szy
 *
 */
public class GeocodesResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5684200237208545935L;
	
	private String 	formatted_address	;//	结构化地址信息	省+市+区+街道+门牌号
	private String 	province	;//	地址所在的省份名	例如：北京市
	private String 	city	;//	地址所在的城市名	例如：北京市
	private String 	citycode	;//	城市编码	例如：010
	private String 	district	;//	地址所在的区	例如：朝阳区
	private String 	township	;//	地址所在的乡镇	
	private String 	street	;//	街道	例如：阜通东大街
	private String 	number	;//	门牌	例如：6号
	private String 	adcode	;//	区域编码	例如：110101
	private String 	location	;//	坐标点	经度，纬度
	private String level	;//	匹配级别	参见“地理编码匹配级别列表”
	
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTownship() {
		return township;
	}
	public void setTownship(String township) {
		this.township = township;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

}
