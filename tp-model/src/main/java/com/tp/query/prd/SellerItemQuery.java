package com.tp.query.prd;

import java.io.Serializable;

import com.tp.model.BaseDO;

/**
 * 商家平台商品列表查询包装类
 * 
 * @author szy
 */
public class SellerItemQuery extends BaseDO implements Serializable {

    private static final long serialVersionUID = 2624666323433004363L;

    private String spu;

    private String prdid;

    private String sku;

    private String barCode;

    private String itemName;

    private String unit;

    private String brandName;

    private Integer status;

    private Long spId;

    private String auditStatus;

    private String bindLevel;

    private Long majorDetailId;
    
    private String unitName;

    public String getSpu() {
        return spu;
    }

    public void setSpu(final String spu) {
        this.spu = spu;
    }

    public String getPrdid() {
        return prdid;
    }

    public void setPrdid(final String prdid) {
        this.prdid = prdid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(final String barCode) {
        this.barCode = barCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(final String brandName) {
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

    public void setSpId(final Long spId) {
        this.spId = spId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(final String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getBindLevel() {
        return bindLevel;
    }

    public void setBindLevel(final String bindLevel) {
        this.bindLevel = bindLevel;
    }

    public Long getMajorDetailId() {
        return majorDetailId;
    }

    public void setMajorDetailId(final Long majorDetailId) {
        this.majorDetailId = majorDetailId;
    }

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
    
}
