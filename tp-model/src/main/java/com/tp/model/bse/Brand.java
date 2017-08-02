package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品品牌
  */
public class Brand extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786415L;

	/**品牌id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**品牌名 数据类型varchar(64)*/
	private String name;
	
	/**品牌英文名 数据类型varchar(255)*/
	private String nameEn;
	
	/**所属国家 数据类型int(11)*/
	private Integer countryId;
	
	/**备注信息 数据类型varchar(255)*/
	private String remark;
	
	/**状态,1为可用,0为不可用 数据类型tinyint(1)*/
	private Integer status;
	
	/** 数据类型varchar(255)*/
	private String logo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**排序 数据类型int(11)*/
	private Integer sortNo;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getNameEn(){
		return nameEn;
	}
	public Integer getCountryId(){
		return countryId;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public String getLogo(){
		return logo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setNameEn(String nameEn){
		this.nameEn=nameEn;
	}
	public void setCountryId(Integer countryId){
		this.countryId=countryId;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setLogo(String logo){
		this.logo=logo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
}
