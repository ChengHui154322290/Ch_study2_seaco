package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠券适用范围表
  */
public class CouponRange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579103L;

	/**主键id 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**分组id 数据类型bigint(18)*/
	private Long categoryId;
	
	/**商家id 数据类型bigint(18)*/
	private Long brandId;
	
	/**商品纬度 类型 0 :sku 数据类型varchar(2)*/
	private String type;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**优惠券 coupon 表id 数据类型bigint(18)*/
	private Long couponId;
	
	/**商品品类 中类ID 数据类型bigint(18)*/
	private Long categoryMiddleId;
	
	/**商品小类IDs 数据类型bigint(18)*/
	private Long categorySmallId;
	
	/**品牌名称 数据类型varchar(50)*/
	private String brandName;
	
	/**根据type 决定code 数据类型varchar(50)*/
	private String code;
	
	/**选中最后一级属性名 数据类型varchar(50)*/
	private String attributeName;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**创建者id 数据类型bigint(18)*/
	private Long createUserId;
	
	/**修改者id 数据类型bigint(18)*/
	private Long updateUserId;
	
	/**sku对应的商品名 数据类型varchar(100)*/
	private String skuName;
	
	/**白名单：0  黑名单 ：1 数据类型int(1)*/
	private Integer rangeType;
	
	
	public Long getId(){
		return id;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getType(){
		return type;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCouponId(){
		return couponId;
	}
	public Long getCategoryMiddleId(){
		return categoryMiddleId;
	}
	public Long getCategorySmallId(){
		return categorySmallId;
	}
	public String getBrandName(){
		return brandName;
	}
	public String getCode(){
		return code;
	}
	public String getAttributeName(){
		return attributeName;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Long getUpdateUserId(){
		return updateUserId;
	}
	public String getSkuName(){
		return skuName;
	}
	public Integer getRangeType(){
		return rangeType;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCouponId(Long couponId){
		this.couponId=couponId;
	}
	public void setCategoryMiddleId(Long categoryMiddleId){
		this.categoryMiddleId=categoryMiddleId;
	}
	public void setCategorySmallId(Long categorySmallId){
		this.categorySmallId=categorySmallId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setAttributeName(String attributeName){
		this.attributeName=attributeName;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setUpdateUserId(Long updateUserId){
		this.updateUserId=updateUserId;
	}
	public void setSkuName(String skuName){
		this.skuName=skuName;
	}
	public void setRangeType(Integer rangeType){
		this.rangeType=rangeType;
	}
}
