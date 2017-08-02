package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/16.
 */
public class InventorySearch extends BaseDO{

    private static final long serialVersionUID = 4400920164858322935L;
    private String sku;

    private String bizId;

    private String app;

    private Integer inventory;


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}
