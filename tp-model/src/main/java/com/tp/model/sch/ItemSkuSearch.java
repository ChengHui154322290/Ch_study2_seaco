package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/26.
 */
public class ItemSkuSearch extends BaseDO {


    private static final long serialVersionUID = -7684200394436746903L;

    private String sku;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
