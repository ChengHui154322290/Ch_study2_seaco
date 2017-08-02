package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单收货人表
  */
public class OrderConsignee extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597511L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单ID 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**订单编号 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**收货人名称 数据类型varchar(30)*/
	private String name;
	
	/**省份/直辖市ID 数据类型bigint(20)*/
	private Long provinceId;
	
	/**省份/直辖市名称 数据类型varchar(32)*/
	private String provinceName;
	
	/**市 数据类型bigint(20)*/
	private Long cityId;
	
	/**市名称 数据类型varchar(32)*/
	private String cityName;
	
	/**区县ID 数据类型bigint(20)*/
	private Long countyId;
	
	/**区县名称 数据类型varchar(32)*/
	private String countyName;
	
	/**镇ID 数据类型bigint(20)*/
	private Long townId;
	
	/**镇名称 数据类型varchar(32)*/
	private String townName;
	
	/**详细地址 数据类型varchar(100)*/
	private String address;
	
	/**手机号 数据类型varchar(16)*/
	private String mobile;
	
	/**电话号码 数据类型varchar(32)*/
	private String telephone;
	
	/**邮箱 数据类型varchar(64)*/
	private String email;
	
	/**邮编 数据类型varchar(20)*/
	private String postcode;
	
	/**会员收货地址ID 数据类型bigint(20)*/
	private Long consigneeId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	/**身份证号*/
	private String identityCard;
	
	public Long getId(){
		return id;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public String getName(){
		return name;
	}
	public Long getProvinceId(){
		return provinceId;
	}
	public String getProvinceName(){
		return provinceName;
	}
	public Long getCityId(){
		return cityId;
	}
	public String getCityName(){
		return cityName;
	}
	public Long getCountyId(){
		return countyId;
	}
	public String getCountyName(){
		return countyName;
	}
	public Long getTownId(){
		return townId;
	}
	public String getTownName(){
		return townName;
	}
	public String getAddress(){
		return address;
	}
	public String getMobile(){
		return mobile;
	}
	public String getTelephone(){
		return telephone;
	}
	public String getEmail(){
		return email;
	}
	public String getPostcode(){
		return postcode;
	}
	public Long getConsigneeId(){
		return consigneeId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}
	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}
	public void setCityId(Long cityId){
		this.cityId=cityId;
	}
	public void setCityName(String cityName){
		this.cityName=cityName;
	}
	public void setCountyId(Long countyId){
		this.countyId=countyId;
	}
	public void setCountyName(String countyName){
		this.countyName=countyName;
	}
	public void setTownId(Long townId){
		this.townId=townId;
	}
	public void setTownName(String townName){
		this.townName=townName;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setPostcode(String postcode){
		this.postcode=postcode;
	}
	public void setConsigneeId(Long consigneeId){
		this.consigneeId=consigneeId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
}
