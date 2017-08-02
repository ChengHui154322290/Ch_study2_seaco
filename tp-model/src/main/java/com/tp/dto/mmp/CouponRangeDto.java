package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;

public class CouponRangeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;

	/** 分组id */
	private Long categoryId;

	/** 商家id */
	private Long brandId;

	/** 商品纬度 类型 0 :sku */
	private String type;

	/** 根据type 决定code */
	private String code;

	/** 创建时间 */
	private Date createTime;

	/** 更新时间 */
	private Date modifyTime;

	/** 创建者id */
	private Long createUserId;

	/** 修改者id */
	private Long updateUserId;
	
	/***商品中类Id**/
	private Long categoryMiddleId;

	/***商品小类Id**/
	private Long categorySmallId;
	
	/**品牌名称**/
	private String brandName;
	/**分类属性名称**/
	private String attributeName;
	
	/**包含或者不包含*/
	private Integer rangeType;
	
	public Integer getRangeType() {
		return rangeType;
	}

	public void setRangeType(Integer rangeType) {
		this.rangeType = rangeType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Long getCategoryMiddleId() {
		return categoryMiddleId;
	}

	public void setCategoryMiddleId(Long categoryMiddleId) {
		this.categoryMiddleId = categoryMiddleId;
	}

	public Long getCategorySmallId() {
		return categorySmallId;
	}

	public void setCategorySmallId(Long categorySmallId) {
		this.categorySmallId = categorySmallId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
}
