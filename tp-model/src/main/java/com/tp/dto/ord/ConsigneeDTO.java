/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;

/**
 * <pre>
 * 收货人信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ConsigneeDTO implements Serializable {
    private static final long serialVersionUID = 2406815454123238405L;
    /** 主键 */
    private Long id;

    /** 收货人名称 */
    private String name;

    /** 省份/直辖市ID */
    private Long provinceId;

    /** 省份/直辖市名称 */
    private String provinceName;

    /** 市 */
    private Long cityId;

    /** 市名称 */
    private String cityName;

    /** 县ID */
    private Long countyId;

    /** 县名称 */
    private String countyName;

    /** 镇ID */
    private Long townId;

    /** 镇名称 */
    private String townName;

    /** 详细地址 */
    private String address;

    /** 手机号 */
    private String mobile;

    /** 电话号码 */
    private String telephone;

    /** 邮箱 */
    private String email;

    /** 邮编 */
    private String postcode;

    /** 是否为默认地址 */
    private Boolean isDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Long getTownId() {
        return townId;
    }

    public void setTownId(Long townId) {
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
