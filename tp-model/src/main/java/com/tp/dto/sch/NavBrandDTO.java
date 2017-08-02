package com.tp.dto.sch;

import java.util.List;

import com.tp.model.bse.NavigationCategory;

/**
 * Created by ldr on 2016/2/29.
 */
public class NavBrandDTO extends NavigationCategory {

    private static final long serialVersionUID = 7217268931318637054L;

    private List<NavBrandSimple> brands;

    public List<NavBrandSimple> getBrands() {
        return brands;
    }

    public void setBrands(List<NavBrandSimple> brands) {
        this.brands = brands;
    }
}
