package com.tp.model.sch.result;

import java.util.List;

/**
 * Created by ldr on 9/23/2016.
 */
public class ShopSearchResultDetail extends SearchResultDetailBase {

    private static final long serialVersionUID = 8538400051761004162L;

    private List<ShopResult> items;

    public List<ShopResult> getItems() {
        return items;
    }

    public void setItems(List<ShopResult> items) {
        this.items = items;
    }
}
