package com.tp.model.sch.result;

/**
 * Created by ldr on 9/23/2016.
 */
public class ShopSearchResult extends SearchResultBase {

    private static final long serialVersionUID = 3250847237249050140L;

   private ShopSearchResultDetail result;

    public ShopSearchResultDetail getResult() {
        return result;
    }

    public void setResult(ShopSearchResultDetail result) {
        this.result = result;
    }
}
