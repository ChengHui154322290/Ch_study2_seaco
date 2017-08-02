package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠政策适用范围
  */
public class FullDiscountItemRange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579107L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠政策序号 数据类型bigint(18)*/
	private Long discountId;
	
	/**规则类型 1-白名单 2-黑名单 数据类型smallint(6)*/
	private Integer type;
	
	/**限定类型 1-分类 2-品牌 3-商品明细 数据类型smallint(6)*/
	private Integer limitType;
	
	/**大分类 数据类型bigint(18)*/
	private Long categoryTopId;
	
	/**大分类名称 数据类型varchar(50)*/
	private String categoryTopName;
	
	/**中分类 数据类型bigint(18)*/
	private Long categoryMiddleId;
	
	/**中分类名称 数据类型varchar(50)*/
	private String categoryMiddleName;
	
	/**小分类 数据类型bigint(18)*/
	private Long categorySmallId;
	
	/**小分类名称 数据类型varchar(50)*/
	private String categorySmallName;
	
	/**品牌Id 数据类型bigint(18)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(50)*/
	private String brandName;
	
	/**商品限定维度类型 1-sku 数据类型smallint(6)*/
	private Integer itemLimitType;
	
	/**商品限定维度 数据类型varchar(20)*/
	private String itemLimitValue;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getDiscountId(){
		return discountId;
	}
	public Integer getType(){
		return type;
	}
	public Integer getLimitType(){
		return limitType;
	}
	public Long getCategoryTopId(){
		return categoryTopId;
	}
	public String getCategoryTopName(){
		return categoryTopName;
	}
	public Long getCategoryMiddleId(){
		return categoryMiddleId;
	}
	public String getCategoryMiddleName(){
		return categoryMiddleName;
	}
	public Long getCategorySmallId(){
		return categorySmallId;
	}
	public String getCategorySmallName(){
		return categorySmallName;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Integer getItemLimitType(){
		return itemLimitType;
	}
	public String getItemLimitValue(){
		return itemLimitValue;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDiscountId(Long discountId){
		this.discountId=discountId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setLimitType(Integer limitType){
		this.limitType=limitType;
	}
	public void setCategoryTopId(Long categoryTopId){
		this.categoryTopId=categoryTopId;
	}
	public void setCategoryTopName(String categoryTopName){
		this.categoryTopName=categoryTopName;
	}
	public void setCategoryMiddleId(Long categoryMiddleId){
		this.categoryMiddleId=categoryMiddleId;
	}
	public void setCategoryMiddleName(String categoryMiddleName){
		this.categoryMiddleName=categoryMiddleName;
	}
	public void setCategorySmallId(Long categorySmallId){
		this.categorySmallId=categorySmallId;
	}
	public void setCategorySmallName(String categorySmallName){
		this.categorySmallName=categorySmallName;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setItemLimitType(Integer itemLimitType){
		this.itemLimitType=itemLimitType;
	}
	public void setItemLimitValue(String itemLimitValue){
		this.itemLimitValue=itemLimitValue;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
