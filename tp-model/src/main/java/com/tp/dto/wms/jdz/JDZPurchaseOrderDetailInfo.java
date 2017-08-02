package com.tp.dto.wms.jdz;

import java.io.Serializable;

/**
 * Created by ldr on 2016/6/29.
 */
public class JDZPurchaseOrderDetailInfo implements Serializable {

    private static final long serialVersionUID = 2037194619043713879L;

    /**
     * 电商SKU编码 发送前请和仓库确认是否该商品已备案维护 必填
     */
    private String sku;
    /**
     * 申报总价 必填
     */
    private Double totalPrice;
    /**
     * 单价 默认为0
     */
    private Double price;
    /**
     * 申报数量 必填
     */
    private Integer qty ;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
