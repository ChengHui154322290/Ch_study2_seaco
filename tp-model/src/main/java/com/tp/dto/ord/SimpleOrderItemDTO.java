package com.tp.dto.ord;

import java.io.Serializable;

/**
 * 简单订购商品DTO
 *
 * @author szy
 */
public class SimpleOrderItemDTO implements Serializable {

    private static final long serialVersionUID = -533976238789214131L;

    /** 商品id **/
    private Long itemId;

    /** 商品编号 */
    private String itemCode;

    /** sku code */
    private String skuCode;

    /** topic id */
    private Long topicId;

    /** 仓库id */
    private Long warehouseId;

    /** 所属的满减ID */
    private Long fullDiscountId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(final String skuCode) {
        this.skuCode = skuCode;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(final Long topicId) {
        this.topicId = topicId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getFullDiscountId() {
        return fullDiscountId;
    }

    public void setFullDiscountId(final Long fullDiscountId) {
        this.fullDiscountId = fullDiscountId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}