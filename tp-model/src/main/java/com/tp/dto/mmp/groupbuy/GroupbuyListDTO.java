package com.tp.dto.mmp.groupbuy;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/25.
 */
public class GroupbuyListDTO extends BaseDO {

    private Long groupbuyId;

    private Long topicId;

    private Long topicItemId;

    private String sku;

    private String topicName;

    private String itemImg;

    private String feature;

    private Double salePrice;

    private Double groupPrice;

    private String countryImg;

    private String countryName;

    private Long countryId;

    private String channel;

    private Long channelId;

    private Integer planAmount;

    private Integer groupType;

    private Date startTime;

    private Date endTime;

    private boolean forIndex;

    public boolean isForIndex() {
        return forIndex;
    }

    public void setForIndex(boolean forIndex) {
        this.forIndex = forIndex;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getGroupbuyId() {
        return groupbuyId;
    }

    public void setGroupbuyId(Long groupbuyId) {
        this.groupbuyId = groupbuyId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(Double groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getCountryImg() {
        return countryImg;
    }

    public void setCountryImg(String countryImg) {
        this.countryImg = countryImg;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(Integer planAmount) {
        this.planAmount = planAmount;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
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
}
