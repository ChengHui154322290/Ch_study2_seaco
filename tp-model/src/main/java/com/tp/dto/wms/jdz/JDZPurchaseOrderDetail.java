package com.tp.dto.wms.jdz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ldr on 2016/6/29.
 */
public class JDZPurchaseOrderDetail implements Serializable {

    private static final long serialVersionUID = 2037194619043713879L;

    /**
     * 电商SKU编码 发送前请和仓库确认是否该商品已备案维护 必填
     */
    private String sku;
    /**
     * 申报总价 必填
     */
    private BigDecimal totalPrice;
    /**
     * 单价 默认为0
     */
    private BigDecimal price;
    /**
     * 申报数量 必填
     */
    private BigDecimal qty ;
    /**
     * 原产国国别代码
     */
    private String countryCode;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
