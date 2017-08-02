package com.tp.dto.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/29.
 */
public class NavBrandSimple extends BaseDO {

    private static final long serialVersionUID = -6723581906759347203L;
    private Long brandId;

    private String pic;

    private String name;

    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
