package com.tp.m.vo.search;

import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 9/23/2016.
 */
public class SearchShopVO implements BaseVO {

    private static final long serialVersionUID = -1500262235674705732L;

    private String shopid;

    private String shopname;

    private String shopbanner;

    private String shoplogo;

    private String supplierid;

    private String shopintro;

    private String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopbanner() {
        return shopbanner;
    }

    public void setShopbanner(String shopbanner) {
        this.shopbanner = shopbanner;
    }

    public String getShoplogo() {
        return shoplogo;
    }

    public void setShoplogo(String shoplogo) {
        this.shoplogo = shoplogo;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getShopintro() {
        return shopintro;
    }

    public void setShopintro(String shopintro) {
        this.shopintro = shopintro;
    }
}
