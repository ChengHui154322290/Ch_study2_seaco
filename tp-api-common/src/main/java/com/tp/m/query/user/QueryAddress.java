package com.tp.m.query.user;

import com.tp.m.base.BaseQuery;

/**
 * 用户收货地址入参
 * @author zhuss
 * @2016年1月4日 下午4:06:28
 */
public class QueryAddress extends BaseQuery{

	private static final long serialVersionUID = -7321859285481512545L;

	private String aid;//地址id
	private String name;//收货人姓名
	private String tel; //收货人手机号
	private String provid;//省份id
	private String provname;//省份姓名
	private String cityid;//城市id
	private String cityname;//城市姓名
	private String districtid;//地区id
	private String districtname;//地区姓名
	private String streetid;//街道id
	private String streetname;//街道姓名
	private String info;//具体地址
	private String isdefault;//是否是默认地址 0否1是
	private String identityCard;//身份证
	private String frontimg;//身份证正面图片地址
	private String backimg;//身份证反面图片地址
	private String longitude;//地理经度
	private String latitude;//地理纬度
	
	public String getFrontimg() {
		return frontimg;
	}

	public void setFrontimg(String frontimg) {
		this.frontimg = frontimg;
	}

	public String getBackimg() {
		return backimg;
	}

	public void setBackimg(String backimg) {
		this.backimg = backimg;
	}

	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getProvid() {
		return provid;
	}
	public void setProvid(String provid) {
		this.provid = provid;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getDistrictid() {
		return districtid;
	}
	public void setDistrictid(String districtid) {
		this.districtid = districtid;
	}
	public String getStreetid() {
		return streetid;
	}
	public void setStreetid(String streetid) {
		this.streetid = streetid;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getDistrictname() {
		return districtname;
	}
	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}
	public String getStreetname() {
		return streetname;
	}
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
