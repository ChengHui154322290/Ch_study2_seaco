package com.tp.dto.prd;

import java.io.Serializable;

public class SellerItemDto implements Serializable {

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
	
	/***主库数据表示符***/
	private String mainMark;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(final Long spId) {
        this.spId = spId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(final Long detailId) {
        this.detailId = detailId;
    }

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getMainMark() {
		return mainMark;
	}

	public void setMainMark(String mainMark) {
		this.mainMark = mainMark;
	}
	
}
