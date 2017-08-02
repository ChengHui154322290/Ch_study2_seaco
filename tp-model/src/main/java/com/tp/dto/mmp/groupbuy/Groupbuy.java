package com.tp.dto.mmp.groupbuy;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/11.
 */
public class Groupbuy extends BaseDO {

    private static final long serialVersionUID = -224220787802831764L;

    private Long topicId;

    private Long topicItemId;

    private Long groupbuyId;

    private String name;

    private Integer type;

    private Integer memberLimit;

    private Integer duration;

    private Date startTime;

    private Date endTime;

    private String sku;

    private String barcode;

    private Double salePrice;

    private  String itemName;

    private String itemPic;

    private Integer applyInventory;

    private Double groupPrice;

    private Long itemId;


    private String detail;

    private String spu;

    private Long brandId;

    private Long countryId;

    private String countryName;

    private  Long supplierId;

    private String supplierName;

    private Long warehouseId;

    private String warehouseName;
    
    private Integer putSign;

    private Integer whtype;

    private Long categoryId;

    private Long bondedArea;

    private Integer limitAmount;

    private Integer status;

    private Integer progress;

    private Integer sort;

    private String introduce;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public Long getGroupbuyId() {
        return groupbuyId;
    }

    public void setGroupbuyId(Long groupbuyId) {
        this.groupbuyId = groupbuyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTopicItemId() {
        return topicItemId;
    }

    public void setTopicItemId(Long topicItemId) {
        this.topicItemId = topicItemId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(Integer memberLimit) {
        this.memberLimit = memberLimit;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getApplyInventory() {
        return applyInventory;
    }

    public void setApplyInventory(Integer applyInventory) {
        this.applyInventory = applyInventory;
    }

    public Double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(Double groupPrice) {
        this.groupPrice = groupPrice;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getWhtype() {
        return whtype;
    }

    public void setWhtype(Integer whtype) {
        this.whtype = whtype;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBondedArea() {
        return bondedArea;
    }

    public void setBondedArea(Long bondedArea) {
        this.bondedArea = bondedArea;
    }

    public Integer getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Integer limitAmount) {
        this.limitAmount = limitAmount;
    }

	public Integer getPutSign() {
		return putSign;
	}

	public void setPutSign(Integer putSign) {
		this.putSign = putSign;
	}
}
