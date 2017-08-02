package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/26.
 */
public class ItemSearch extends BaseDO {

    private static final long serialVersionUID = 6830626913147368089L;

    private Long itemId;

    private Long largeId;

    private Long mediumId;

    private Long smallId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getLargeId() {
        return largeId;
    }

    public void setLargeId(Long largeId) {
        this.largeId = largeId;
    }

    public Long getMediumId() {
        return mediumId;
    }

    public void setMediumId(Long mediumId) {
        this.mediumId = mediumId;
    }

    public Long getSmallId() {
        return smallId;
    }

    public void setSmallId(Long smallId) {
        this.smallId = smallId;
    }
}
