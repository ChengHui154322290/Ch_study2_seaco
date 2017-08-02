package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/26.
 */
public class ItemDetailSearch extends BaseDO {


    private static final long serialVersionUID = 5964770150256787305L;

    private Long detailId;

    private Long itemId;

    private Long countryId;

    private Long brandId;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
