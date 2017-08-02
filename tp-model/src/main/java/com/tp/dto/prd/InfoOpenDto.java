package com.tp.dto.prd;

import java.io.Serializable;

/**
 * 商品信息dto
 * 
 * @author szy
 *
 */
public class InfoOpenDto implements Serializable {

	private static final long serialVersionUID = -7410133654376798171L;

	/** 主键 */
	private Long id;

	/** spu */
	private String spu;

	/** spu名称 */
	private String spuName;

	/** 品牌id */
	private Long barandId;

	/** 品牌名称 */
	private String brandName;

	/** 大中小类信息 */
	private CategoryOpenDto categoryOpenDto;

	/** 单位id */
	private Long unitId;

	/** 单位名称 */
	private String unitName;

	/** 备注 */
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public Long getBarandId() {
		return barandId;
	}

	public void setBarandId(Long barandId) {
		this.barandId = barandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public CategoryOpenDto getCategoryOpenDto() {
		return categoryOpenDto;
	}

	public void setCategoryOpenDto(CategoryOpenDto categoryOpenDto) {
		this.categoryOpenDto = categoryOpenDto;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
