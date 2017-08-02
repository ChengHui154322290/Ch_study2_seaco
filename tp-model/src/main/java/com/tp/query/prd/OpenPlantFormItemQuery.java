package com.tp.query.prd;

import java.io.Serializable;

import com.tp.model.BaseDO;

/**
 * 商家平台商品列表查询包装类
 * 
 * @author szy
 */
public class OpenPlantFormItemQuery extends BaseDO implements Serializable {

	private static final long serialVersionUID = -5124858999963254830L;

	private String spu;

	private String prdid;

	private String sku;

	private String barCode;

	private String itemName;

	private String unit;

	private String brandName;

	private Integer status;

	private Long spId;

	private String bindLevel;

	private Long majorDetailId;

	private String unitName;

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public String getBindLevel() {
		return bindLevel;
	}

	public void setBindLevel(String bindLevel) {
		this.bindLevel = bindLevel;
	}

	public Long getMajorDetailId() {
		return majorDetailId;
	}

	public void setMajorDetailId(Long majorDetailId) {
		this.majorDetailId = majorDetailId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
