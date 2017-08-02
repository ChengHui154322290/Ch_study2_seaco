package com.tp.model.bse;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class IpInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(255)*/
	private String ipstart;
	
	/** 数据类型varchar(255)*/
	private String ipend;
	
	/**ipstart的int型表示 数据类型int(11) unsigned*/
	private Integer ipstartInt;
	
	/**ipend的int型表示 数据类型int(11) unsigned*/
	private Integer ipendInt;
	
	/** 数据类型bigint(20)*/
	private Long countryId;
	
	/** 数据类型varchar(255)*/
	private String country;
	
	/** 数据类型bigint(20)*/
	private Long provinceId;
	
	/** 数据类型varchar(255)*/
	private String province;
	
	/** 数据类型bigint(20)*/
	private Long cityId;
	
	/** 数据类型varchar(255)*/
	private String city;
	
	/** 数据类型varchar(255)*/
	private String district;
	
	
	public Long getId(){
		return id;
	}
	public String getIpstart(){
		return ipstart;
	}
	public String getIpend(){
		return ipend;
	}
	public Integer getIpstartInt(){
		return ipstartInt;
	}
	public Integer getIpendInt(){
		return ipendInt;
	}
	public Long getCountryId(){
		return countryId;
	}
	public String getCountry(){
		return country;
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
	public String getDistrict(){
		return district;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setIpstart(String ipstart){
		this.ipstart=ipstart;
	}
	public void setIpend(String ipend){
		this.ipend=ipend;
	}
	public void setIpstartInt(Integer ipstartInt){
		this.ipstartInt=ipstartInt;
	}
	public void setIpendInt(Integer ipendInt){
		this.ipendInt=ipendInt;
	}
	public void setCountryId(Long countryId){
		this.countryId=countryId;
	}
	public void setCountry(String country){
		this.country=country;
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
	public void setDistrict(String district){
		this.district=district;
	}
}
