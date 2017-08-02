package com.tp.dto.prd;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 商家平台商品列表查询结果包装类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OpenPlantFormItemDto implements Serializable {

	private static final long serialVersionUID = 7649863089314302799L;

	private Long id;

	private String spu;

	private String prdid;

	private String sku;

	private String barCode;

	private String itemName;

	private String unit;

	private String brandName;

	private String status;

	private Long spId;

	private Long detailId;

	private String unitName;
	// 标识位
	private Boolean itemMark;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Boolean getItemMark() {
		return itemMark;
	}

	public void setItemMark(Boolean itemMark) {
		this.itemMark = itemMark;
	}

}
