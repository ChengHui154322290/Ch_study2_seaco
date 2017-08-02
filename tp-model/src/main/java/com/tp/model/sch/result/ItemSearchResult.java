package com.tp.model.sch.result;

/**
 * Created by ldr on 2016/2/17.
 */
public class ItemSearchResult extends SearchResultBase {

    private static final long serialVersionUID = -33165365003671020L;

    private ItemSearchResultDetail result;

    public ItemSearchResultDetail getResult() {
        return result;
    }

    public void setResult(ItemSearchResultDetail result) {
        this.result = result;
    }
}
