package com.tp.dto.sup;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 7/18/2016.
 */
public class WarehouseOrderFact extends BaseDO {

    private static final long serialVersionUID = -1821288800685800058L;

    private Long purchaseProductId;

    private String sku;

    private String itemName;

    private Integer planAmount;

    private Integer factAmount;


    public Long getPurchaseProductId() {
        return purchaseProductId;
    }

    public void setPurchaseProductId(Long purchaseProductId) {
        this.purchaseProductId = purchaseProductId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(Integer planAmount) {
        this.planAmount = planAmount;
    }

    public Integer getFactAmount() {
        return factAmount;
    }

    public void setFactAmount(Integer factAmount) {
        this.factAmount = factAmount;
    }
}
