package com.tp.query.sch;

import com.tp.enums.common.PlatformEnum;
import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/18.
 */
public class SearchQuery extends BaseDO {

    private static final long serialVersionUID = 5883884908918133226L;

    /**
     * 关键字
     */
    private String key;
    /**
     * 品牌Id
     */
    private Long brandId;
    /**
     * 是否只显示有库存的，1是，0否 默认0
     */
    private Integer hasInventoryOnly = 0;
    /**
     * 排序字段
     * 1 价格降序
     * 2 价格升序
     * 3 销售数量降序
     * 4 销售数量升序
     * @see com.tp.dto.sch.enums.Sort
     */
    private Integer sort;

    /**
     * 平台
     */
    private PlatformEnum platform;
    /**
     * 国家Id
     */
    private Long countryId;

    /**
     * 分类导航Id
     */
    private Long navCategoryId;

    /**
     * 分类导航BrandId
     */
    private Long navBrandId;

    private Long userId;

    /**
     * 销售方式
     * @see com.tp.dto.mmp.enums.SalesPartten
     */
    private Integer salesPattern;

    /**
     * 优惠券Id 此id为usercoupon主键
     * 用于查询优惠券可用商品
     */
    private Long couponId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getSalesPattern() {
        return salesPattern;
    }

    public void setSalesPattern(Integer salesPattern) {
        this.salesPattern = salesPattern;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getHasInventoryOnly() {
        return hasInventoryOnly;
    }

    public void setHasInventoryOnly(Integer hasInventoryOnly) {
        this.hasInventoryOnly = hasInventoryOnly;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getNavCategoryId() {
        return navCategoryId;
    }

    public void setNavCategoryId(Long navCategoryId) {
        this.navCategoryId = navCategoryId;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public Long getNavBrandId() {
        return navBrandId;
    }

    public void setNavBrandId(Long navBrandId) {
        this.navBrandId = navBrandId;
    }
}
