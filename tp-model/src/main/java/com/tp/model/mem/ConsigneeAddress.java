package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 用户收货地址表
  */
public class ConsigneeAddress extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300755L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**用户编号 数据类型bigint(11)*/
	private Long userId;
	
	/**收货人 数据类型varchar(20)*/
	private String name;
	
	/**收货人-手机号码 数据类型varchar(20)*/
	private String mobile;
	
	/**收货人-固定电话 数据类型varchar(20)*/
	private String phone;
	
	/**收货人-email 数据类型varchar(100)*/
	private String email;
	
	/**收货地址-省份 数据类型bigint(11)*/
	private Long provinceId;
	
	/**省份-描述-用于展示 数据类型varchar(20)*/
	private String province;
	
	/**收货地址-城市 数据类型bigint(11)*/
	private Long cityId;
	
	/**城市-描述-用于展示 数据类型varchar(20)*/
	private String city;
	
	/**收货地址-区县 数据类型bigint(11)*/
	private Long countyId;
	
	/**区县-描述-用于展示 数据类型varchar(20)*/
	private String county;
	
	/**收货地址-街道 数据类型bigint(11)*/
	private Long streetId;
	
	/**街道地址-描述 数据类型varchar(50)*/
	private String street;
	
	/**收货地址-详细地址 数据类型varchar(200)*/
	private String address;
	
	/**收货地址-邮编 数据类型varchar(10)*/
	private String zipCode;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**是否为默认收货地址 数据类型tinyint(1)*/
	private Boolean isDefault;
	
	/**收货时间 数据类型bigint(20)*/
	private Long receiptTime;
	
	/**收货时间描述 数据类型varchar(50)*/
	private String receiptTimeDesc;
	
	/**平台来源  0:pc 1:app 2:wap 3:ios 4:BTM  5:wx 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**网站来源 0:来自西客商城 1:来自seagoor 数据类型tinyint(1)*/
	private Integer source;
	
	/**状态 1:正常 0:已删除 数据类型tinyint(4)*/
	private Boolean state;
	/**身份证号*/
	private String identityCard;
	/**身份证正面图片*/
	private String backImg;
	/**身份证反面图片*/
	private String frontImg;
	/**地理经度*/
	private String longitude;
	/**地理纬度*/
	private String latitude;
	
	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public String getFrontImg() {
		return frontImg;
	}

	public void setFrontImg(String frontImg) {
		this.frontImg = frontImg;
	}

	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getName(){
		return name;
	}
	public String getMobile(){
		return mobile;
	}
	public String getPhone(){
		return phone;
	}
	public String getEmail(){
		return email;
	}
	public Long getProvinceId(){
		return provinceId;
	}
	public String getProvince(){
		return province;
	}
	public Long getCityId(){
		return cityId;
	}
	public String getCity(){
		return city;
	}
	public Long getCountyId(){
		return countyId;
	}
	public String getCounty(){
		return county;
	}
	public Long getStreetId(){
		return streetId;
	}
	public String getStreet(){
		return street;
	}
	public String getAddress(){
		return address;
	}
	public String getZipCode(){
		return zipCode;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Boolean getIsDefault(){
		return isDefault;
	}
	public Long getReceiptTime(){
		return receiptTime;
	}
	public String getReceiptTimeDesc(){
		return receiptTimeDesc;
	}
	public Integer getPlatForm(){
		return platForm;
	}
	public Integer getSource(){
		return source;
	}
	public Boolean getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}
	public void setProvince(String province){
		this.province=province;
	}
	public void setCityId(Long cityId){
		this.cityId=cityId;
	}
	public void setCity(String city){
		this.city=city;
	}
	public void setCountyId(Long countyId){
		this.countyId=countyId;
	}
	public void setCounty(String county){
		this.county=county;
	}
	public void setStreetId(Long streetId){
		this.streetId=streetId;
	}
	public void setStreet(String street){
		this.street=street;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setIsDefault(Boolean isDefault){
		this.isDefault=isDefault;
	}
	public void setReceiptTime(Long receiptTime){
		this.receiptTime=receiptTime;
	}
	public void setReceiptTimeDesc(String receiptTimeDesc){
		this.receiptTimeDesc=receiptTimeDesc;
	}
	public void setPlatForm(Integer platForm){
		this.platForm=platForm;
	}
	public void setSource(Integer source){
		this.source=source;
	}
	public void setState(Boolean state){
		this.state=state;
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
