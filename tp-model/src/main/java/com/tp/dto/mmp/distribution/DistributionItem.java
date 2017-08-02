package com.tp.dto.mmp.distribution;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/4/19.
 */
public class DistributionItem extends BaseDO {

    private static final long serialVersionUID = -6656453049415624466L;

    private Long topicId;

    private Long topicItemId;

    private Long itemId;

    private String sku;

    private String name;

    private String pic;

    private Double salesPrice;

    private Double topicPirce;

    private Double disRate;

    private Double disAmount;

    private Integer lockStatus;

    private Integer itemStatus;

    public Integer getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Integer itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Double getTopicPirce() {
        return topicPirce;
    }

    public void setTopicPirce(Double topicPirce) {
        this.topicPirce = topicPirce;
    }

    public Double getDisRate() {
        return disRate;
    }

    public void setDisRate(Double disRate) {
        this.disRate = disRate;
    }

    public Double getDisAmount() {
        return disAmount;
    }

    public void setDisAmount(Double disAmount) {
        this.disAmount = disAmount;
    }
}
