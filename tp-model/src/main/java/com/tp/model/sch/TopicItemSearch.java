package com.tp.model.sch;

import java.math.BigDecimal;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/16.
 */
public class TopicItemSearch  extends BaseDO{

    private static final long serialVersionUID = 5577341504863558118L;

    private Long topicItemId;

    private String sku;

    private Long itemId;

    private BigDecimal topicPrice;

    private BigDecimal salePrice;

    private String itemName;

    private Long topicId;

    private String spu;

    private String barCode;

    private Integer status;

    private Long brandId;

    private Long lCategoryId;

    private Long mCategoryId;

    private Long sCategoryId;

    private Long supplierId;

    private Integer limitAmount;

    private String itemImage;

    private Long channelId;

    private Long countryId;

    private String countryName;

    private Integer lockStatus;


    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getTopicItemId() {
        return topicItemId;
    }

    public void setTopicItemId(Long topicItemId) {
        this.topicItemId = topicItemId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getTopicPrice() {
        return topicPrice;
    }

    public void setTopicPrice(BigDecimal topicPrice) {
        this.topicPrice = topicPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getlCategoryId() {
        return lCategoryId;
    }

    public void setlCategoryId(Long lCategoryId) {
        this.lCategoryId = lCategoryId;
    }

    public Long getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(Long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public Long getsCategoryId() {
        return sCategoryId;
    }

    public void setsCategoryId(Long sCategoryId) {
        this.sCategoryId = sCategoryId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Integer limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
