package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/18.
 */
public class DistrictSearch extends BaseDO {

    private static final long serialVersionUID = 7851513142077953519L;
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
