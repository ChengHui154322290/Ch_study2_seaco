package com.tp.model.sch.result;

import java.util.List;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 9/23/2016.
 */
public class SearchResultDetailBase extends BaseDO {

    private static final long serialVersionUID = 6201110207320681125L;

    private Integer total;

    private Integer num;

    private Integer viewTotal;

    private List<String> facet;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getViewTotal() {
        return viewTotal;
    }

    public void setViewTotal(Integer viewTotal) {
        this.viewTotal = viewTotal;
    }

    public List<String> getFacet() {
        return facet;
    }

    public void setFacet(List<String> facet) {
        this.facet = facet;
    }
}
