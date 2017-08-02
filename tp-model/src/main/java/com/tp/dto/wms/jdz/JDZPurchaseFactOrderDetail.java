package com.tp.dto.wms.jdz;

import java.io.Serializable;

/**
 * Created by ldr on 2016/7/7.
 */
public class JDZPurchaseFactOrderDetail implements Serializable {

    private static final long serialVersionUID = -3131771633521760655L;

    private String sku;

    private Integer qty;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
