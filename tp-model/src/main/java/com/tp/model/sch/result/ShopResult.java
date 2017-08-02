package com.tp.model.sch.result;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 9/23/2016.
 */
public class ShopResult extends BaseDO {

    private static final long serialVersionUID = -4847983956006145769L;

    private Long id;

    private Long shop_id;

    private String shop_name;

    private String shop_banner;

    private String shop_logo;

    private String shop_tag;

    private String shop_intro;

    private String supplier_id;

    private  Integer status;

    private Date create_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShop_id() {
        return shop_id;
    }

    public void setShop_id(Long shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_banner() {
        return shop_banner;
    }

    public void setShop_banner(String shop_banner) {
        this.shop_banner = shop_banner;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getShop_tag() {
        return shop_tag;
    }

    public void setShop_tag(String shop_tag) {
        this.shop_tag = shop_tag;
    }

    public String getShop_intro() {
        return shop_intro;
    }

    public void setShop_intro(String shop_intro) {
        this.shop_intro = shop_intro;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
