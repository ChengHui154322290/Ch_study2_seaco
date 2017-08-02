package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 国家图标表
  */
public class NationalIcon extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**国家英文名称 数据类型varchar(100)*/
	private String nameEn;
	
	/**国家所属大洲id 数据类型int(11)*/
	private Integer continentId;
	
	/**国家id 数据类型int(11)*/
	private Integer countryId;
	
	/**图片url地址 数据类型varchar(255)*/
	private String picPath;
	
	/** 数据类型tinyint(4)*/
	private Integer status;
	
	/**排序号 数据类型int(11)*/
	private Integer sortNo;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getNameEn(){
		return nameEn;
	}
	public Integer getContinentId(){
		return continentId;
	}
	public Integer getCountryId(){
		return countryId;
	}
	public String getPicPath(){
		return picPath;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setNameEn(String nameEn){
		this.nameEn=nameEn;
	}
	public void setContinentId(Integer continentId){
		this.continentId=continentId;
	}
	public void setCountryId(Integer countryId){
		this.countryId=countryId;
	}
	public void setPicPath(String picPath){
		this.picPath=picPath;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
