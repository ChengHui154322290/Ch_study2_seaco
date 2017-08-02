package com.tp.model.sch;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 9/23/2016.
 */
public class SupplierShopSearch extends BaseDO {

    private static final long serialVersionUID = 2968250855388413761L;

    private Long id;

    private Long supplierId;

    private String shopName;

    private String logoPath;

    private String mobileImage;

    private String introMobile;

    private String searchTitle1;

    private String searchTitle2;

    private String searchTitle3;

    private String searchTitle4;

    private String shopImagePath;

    public String getShopImagePath() {
        return shopImagePath;
    }

    public void setShopImagePath(String shopImagePath) {
        this.shopImagePath = shopImagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public String getIntroMobile() {
        return introMobile;
    }

    public void setIntroMobile(String introMobile) {
        this.introMobile = introMobile;
    }

    public String getSearchTitle1() {
        return searchTitle1;
    }

    public void setSearchTitle1(String searchTitle1) {
        this.searchTitle1 = searchTitle1;
    }

    public String getSearchTitle2() {
        return searchTitle2;
    }

    public void setSearchTitle2(String searchTitle2) {
        this.searchTitle2 = searchTitle2;
    }

    public String getSearchTitle3() {
        return searchTitle3;
    }

    public void setSearchTitle3(String searchTitle3) {
        this.searchTitle3 = searchTitle3;
    }

    public String getSearchTitle4() {
        return searchTitle4;
    }

    public void setSearchTitle4(String searchTitle4) {
        this.searchTitle4 = searchTitle4;
    }
}
