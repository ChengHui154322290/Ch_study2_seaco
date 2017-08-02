package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/18.
 */
public class BrandSearch extends BaseDO {

    private static final long serialVersionUID = -1662681601434247627L;
    private Long brandId;

    private String brandName;

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
}
