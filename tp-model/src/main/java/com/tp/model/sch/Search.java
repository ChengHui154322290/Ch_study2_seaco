package com.tp.model.sch;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;

/**
 * @author szy
 */
public class Search extends BaseDO implements Serializable {


    private static final long serialVersionUID = 7442366396190928474L;
    /**
     * 数据类型bigint(20)
     */
    @Id
    private Long id;

    private Long topicItemId;

    /**
     * 数据类型varchar(255)
     */
    private String itemName;

    /**
     * 数据类型bigint(20)
     */
    private Long topicId;

    private Integer topicType;


    /**销售类型*/
    private Integer salesPattern;

    /**
     * 数据类型varchar(255)
     */
    private String sku;

    /**
     * 数据类型varchar(255)
     */
    private String spu;

    private Long itemId;

    /**
     * 数据类型varchar(255)
     */
    private String barCode;

    /**
     * 数据类型varchar(255)
     */
    private String platform;

    /**
     * 数据类型decimal(10,2)
     */
    private BigDecimal topicPrice;

    /**
     * 数据类型decimal(10,2)
     */
    private BigDecimal salePrice;

    /**
     * 数据类型tinyint(1)
     */
    private Integer status;

    private Integer itemStatus;

    /**
     * 数据类型int(11)
     */
    private Integer inventory;

    /**
     * 数据类型int(11)
     */
    private Integer salesCount;

    private Integer commentCount;

    private Float rating;

    private Long hits;

    /**
     * 数据类型varchar(255)
     */
    private String itemImg;

    /**
     * 数据类型bigint(20)
     */
    private Long brandId;

    /**
     * 数据类型varchar(255)
     */
    private String brandName;

    /**
     * 数据类型bigint(20)
     */
    private Long lCategoryId;

    /**
     * 数据类型varchar(255)
     */
    private String lCategoryName;

    /**
     * 数据类型bigint(20)
     */
    private Long mCategoryId;

    /**
     * 数据类型varchar(255)
     */
    private String mCategoryName;

    /**
     * 数据类型bigint(20)
     */
    private Long sCategoryId;

    /**
     * 数据类型varchar(255)
     */
    private String sCategoryName;

    private Long channelId;

    private String channelName;

    private Long countryId;

    private String countryName;

    /**
     * 数据类型varchar(255)
     */
    private String specIds;

    /**
     * 数据类型varchar(255)
     */
    private String specDetails;

    /**
     * 数据类型bigint(20)
     */
    private Long supplierId;

    /**
     * 数据类型int(11)
     */
    private Integer limitAmount;

    /**
     * 数据类型datetime
     */
    private Date topicStart;

    /**
     * 数据类型datetime
     */
    private Date topicEnd;

    private Date createTime;

    private Date updateTime;

    private String shopName;

    public Integer getSalesPattern() {
        return salesPattern;
    }

    public void setSalesPattern(Integer salesPattern) {
        this.salesPattern = salesPattern;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Integer itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getlCategoryId() {
        return lCategoryId;
    }

    public void setlCategoryId(Long lCategoryId) {
        this.lCategoryId = lCategoryId;
    }

    public String getlCategoryName() {
        return lCategoryName;
    }

    public void setlCategoryName(String lCategoryName) {
        this.lCategoryName = lCategoryName;
    }

    public Long getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(Long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public Long getsCategoryId() {
        return sCategoryId;
    }

    public void setsCategoryId(Long sCategoryId) {
        this.sCategoryId = sCategoryId;
    }

    public String getsCategoryName() {
        return sCategoryName;
    }

    public void setsCategoryName(String sCategoryName) {
        this.sCategoryName = sCategoryName;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getSpecDetails() {
        return specDetails;
    }

    public void setSpecDetails(String specDetails) {
        this.specDetails = specDetails;
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

    public Date getTopicStart() {
        return topicStart;
    }

    public void setTopicStart(Date topicStart) {
        this.topicStart = topicStart;
    }

    public Date getTopicEnd() {
        return topicEnd;
    }

    public void setTopicEnd(Date topicEnd) {
        this.topicEnd = topicEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Search)) return false;
        Search search = (Search) obj;
        return topicItemId.equals(search.getTopicItemId()) &&
                itemName.equals(search.getItemName()) &&
                topicId.equals(search.getTopicId()) &&
                sku.equals(search.getSku()) &&
                spu.equals(search.getSpu()) &&
                status.equals(search.getStatus()) &&
                barCode.equals(search.getBarCode()) &&
                platform.equals(search.getPlatform()) &&
                topicPrice.equals(search.getTopicPrice()) &&
                salePrice.equals(search.getSalePrice()) &&
                salesCount.equals(search.getSalesCount()) &&
                inventory.equals(search.getInventory()) &&
                itemImg.equals(search.getItemImg()) &&
                brandId.equals(search.getBrandId()) &&
                brandName.equals(search.getBrandName()) &&
                sCategoryId.equals(search.getsCategoryId()) &&
                mCategoryId.equals(search.getmCategoryId()) &&
                lCategoryId.equals(search.getlCategoryId()) &&
                supplierId.equals(search.getSupplierId()) &&
                topicStart.equals(search.getTopicStart()) &&
                topicEnd.equals(search.getTopicEnd()) &&
                countryId.equals(search.getCountryId()) &&
                countryName.equals(search.getCountryName()) &&
                channelId.equals(search.getChannelId()) &&
                channelName.equals(search.getChannelName()) &&
                commentCount.equals(search.getCommentCount()) &&
                rating.equals(search.getRating()) &&
                hits.equals(search.getHits()) &&
                itemStatus.equals(search.getItemStatus()) &&
                topicType != null &&
                topicType.equals(search.getTopicType()) &&
                shopName.equals(search.getShopName())&&
                salesPattern.equals(search.getSalesPattern());


    }
}
