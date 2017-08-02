package com.tp.dto.sch;

import java.util.List;

import com.tp.model.bse.NavigationCategory;

/**
 * Created by ldr on 2016/2/25.
 */
public class NavCategoryDTO extends NavigationCategory {

    private static final long serialVersionUID = 2560423313957929703L;

    private List<NavCategoryDTO> child;

    public List<NavCategoryDTO> getChild() {
        return child;
    }

    public void setChild(List<NavCategoryDTO> child) {
        this.child = child;
    }
}
