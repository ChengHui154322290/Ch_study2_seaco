package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 前台类目跳转链接表
  */
public class FrontCategoryLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786419L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类目id 数据类型bigint(11)*/
	private Long categoryId;
	
	/**跳转方式:1-后台分类,2-固定页面,3-商品,4-品牌,5-搜索词 数据类型tinyint(2)*/
	private Integer linkType;
	
	/** 数据类型varchar(4000)*/
	private String linkContent;
	
	/**后台大类ids 数据类型varchar(1000)*/
	private String largeCategoryIds;
	
	/**后台中类ids 数据类型varchar(1000)*/
	private String mediumCategoryIds;
	
	/**后台小类ids 数据类型varchar(1000)*/
	private String smallCategoryIds;
	
	/** 数据类型varchar(128)*/
	private String linkUrlPc;
	
	/** 数据类型varchar(128)*/
	private String linkUrlApp;
	
	/** 数据类型varchar(128)*/
	private String linkUrlWap;
	
	/**状态:0-正常，1-删除 数据类型tinyint(2)*/
	private Integer status;
	
	/** 数据类型bigint(11)*/
	private Long createUserId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型bigint(11)*/
	private Long modifyUserId;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Integer getLinkType(){
		return linkType;
	}
	public String getLinkContent(){
		return linkContent;
	}
	public String getLargeCategoryIds(){
		return largeCategoryIds;
	}
	public String getMediumCategoryIds(){
		return mediumCategoryIds;
	}
	public String getSmallCategoryIds(){
		return smallCategoryIds;
	}
	public String getLinkUrlPc(){
		return linkUrlPc;
	}
	public String getLinkUrlApp(){
		return linkUrlApp;
	}
	public String getLinkUrlWap(){
		return linkUrlWap;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setLinkType(Integer linkType){
		this.linkType=linkType;
	}
	public void setLinkContent(String linkContent){
		this.linkContent=linkContent;
	}
	public void setLargeCategoryIds(String largeCategoryIds){
		this.largeCategoryIds=largeCategoryIds;
	}
	public void setMediumCategoryIds(String mediumCategoryIds){
		this.mediumCategoryIds=mediumCategoryIds;
	}
	public void setSmallCategoryIds(String smallCategoryIds){
		this.smallCategoryIds=smallCategoryIds;
	}
	public void setLinkUrlPc(String linkUrlPc){
		this.linkUrlPc=linkUrlPc;
	}
	public void setLinkUrlApp(String linkUrlApp){
		this.linkUrlApp=linkUrlApp;
	}
	public void setLinkUrlWap(String linkUrlWap){
		this.linkUrlWap=linkUrlWap;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
