package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/2.
 */
public class ClearanceChannelSearch extends BaseDO {

    private static final long serialVersionUID = -7983545947508152397L;
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
