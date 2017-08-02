package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/26.
 */
public class ItemDetailSalesCountSearch extends BaseDO {

    private static final long serialVersionUID = -8606985706380825495L;

    private Long detailId;

    private Long defSalesCount;

    private Long relSalesCount;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getDefSalesCount() {
        return defSalesCount;
    }

    public void setDefSalesCount(Long defSalesCount) {
        this.defSalesCount = defSalesCount;
    }

    public Long getRelSalesCount() {
        return relSalesCount;
    }

    public void setRelSalesCount(Long realSalesCount) {
        this.relSalesCount = realSalesCount;
    }
}
