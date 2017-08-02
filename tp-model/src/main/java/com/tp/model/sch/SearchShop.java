package com.tp.model.sch;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.dto.sch.enums.SearchRecordStatus;
import com.tp.model.BaseDO;

/**
 * @author szy
 */
public class SearchShop extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1474540518109L;

    /**
     * 主键 数据类型bigint(20)
     */
    @Id
    private Long id;

    /**
     * 商家Id 数据类型bigint(20)
     */
    private Long shopId;

    /**
     * 商家名称 数据类型varchar(128)
     */
    private String shopName;

    /**
     * 商家图片 数据类型varchar(255)
     */
    private String shopBanner;

    /**
     * 数据类型varchar(255)
     */
    private String shopLogo;

    /**
     * 商家tag 数据类型varchar(255)
     */
    private String shopTag;

    /**
     * 数据类型varchar(255)
     */
    private String shopIntro;

    /**
     * 数据类型bigint(20)
     */
    private Long supplierId;

    /**
     * 数据类型tinyint(1)
     */
    private Integer shopStatus;

    /**
     * @see  SearchRecordStatus
     */
    private Integer recordStatus;

    private Integer hits;

    /**
     * 数据类型datetime
     */
    private Date createTime;

    /**
     * 数据类型datetime
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopBanner() {
        return shopBanner;
    }

    public void setShopBanner(String shopBanner) {
        this.shopBanner = shopBanner;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopTag() {
        return shopTag;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }

    public String getShopIntro() {
        return shopIntro;
    }

    public void setShopIntro(String shopIntro) {
        this.shopIntro = shopIntro;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SearchShop)) {
            return false;
        }
        SearchShop cp = (SearchShop) obj;
        if (this.shopId.equals(cp.getShopId()) &&
                this.shopName.equals(cp.getShopName()) &&
                this.shopTag.equals(cp.getShopTag()) &&
                this.shopLogo.equals(cp.getShopLogo()) &&
                this.shopBanner.equals(cp.getShopBanner()) &&
                this.shopIntro.equals(cp.getShopIntro()) &&
                this.shopStatus.equals(cp.getShopStatus())&&
                this.recordStatus.equals(cp.getRecordStatus())&&
                this.supplierId.equals(cp.getSupplierId()) &&
                this.hits.equals(cp.getHits())
                ) {
            return true;
        }
        return false;
    }
}
