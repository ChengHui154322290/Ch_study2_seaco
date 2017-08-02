package com.tp.model.bse;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class DistrictZipAreaCode extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786418L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(50)*/
	private String name;
	
	/**地区id 数据类型bigint(20)*/
	private Long districtId;
	
	/**邮编 数据类型varchar(50)*/
	private String zipCode;
	
	/**区号 数据类型varchar(50)*/
	private String areaCode;
	
	/** 数据类型int(11)*/
	private Integer sortNo;
	
	/**地区的邮编区号等相关信息表s 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public String getZipCode(){
		return zipCode;
	}
	public String getAreaCode(){
		return areaCode;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}
	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
