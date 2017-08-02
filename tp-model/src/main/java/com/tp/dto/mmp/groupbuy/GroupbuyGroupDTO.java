package com.tp.dto.mmp.groupbuy;

import com.tp.model.mmp.GroupbuyGroup;

/**
 * Created by ldr on 2016/3/22.
 */
public class GroupbuyGroupDTO extends GroupbuyGroup {

    private static final long serialVersionUID = -32954709123901748L;
    private String pic;

    private Double salePrice;

    private Double groupPrice;

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(Double groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
