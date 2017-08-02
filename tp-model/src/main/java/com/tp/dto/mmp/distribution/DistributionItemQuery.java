package com.tp.dto.mmp.distribution;

import com.tp.enums.common.PlatformEnum;
import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/4/19.
 */
public class DistributionItemQuery extends BaseDO {

    private static final long serialVersionUID = -1366187804331190646L;

    private Integer sort;

    private PlatformEnum platformEnum;
    
    private Long shopPromoterId;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public PlatformEnum getPlatformEnum() {
        return platformEnum;
    }

    public void setPlatformEnum(PlatformEnum platformEnum) {
        this.platformEnum = platformEnum;
    }

	public Long getShopPromoterId() {
		return shopPromoterId;
	}

	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
}
