package com.tp.dto.sch;

import java.util.List;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/29.
 */
public class Nav extends BaseDO {

    private static final long serialVersionUID = 6993365998446923963L;

    private List<NavCategoryDTO> categories ;

    private List<NavBrandDTO> brands;

    public Nav() {
    }

    public Nav(List<NavCategoryDTO> categories, List<NavBrandDTO> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public List<NavCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<NavCategoryDTO> categories) {
        this.categories = categories;
    }

    public List<NavBrandDTO> getBrands() {
        return brands;
    }

    public void setBrands(List<NavBrandDTO> brands) {
        this.brands = brands;
    }
}
