package com.tp.m.vo.topic;

import com.tp.m.base.BaseVO;

/**
 * 专题详情
 *
 * @author zhuss
 * @2016年1月4日 下午7:01:43
 */
public class TopicDetailVO implements BaseVO {

    private static final long serialVersionUID = -4531637895637194929L;

    private String name;//专题名称
    private String tophtml;//顶部HTML
    private String shareurl;//用于分享的url
    private String status;//状态:1正常  2未开始 3已结束 4 无效
    private String statusdesc;//状态描述
    private Integer type;//专场类型
    private Long supplierId;//供应商ID
    private String logoPath;//logo图片地址
    private String mobileImage;//*移动首页图片
    private String introMobile;//*店铺介绍
    private String shopName;//店铺名 数据类型
    private String shopImagePath;//店铺头图片
    private String prestime;//营业时间
    private String addr;//店铺地址
    private String tel;//店铺电话
    private String notice;//店铺公告
    private String longitude;//经度
    private String latitude;//维度
    private String point;//卖点

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPrestime() {
        return prestime;
    }

    public void setPrestime(String prestime) {
        this.prestime = prestime;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * type.
     *
     * @return the type
     * @since JDK 1.8
     */
    public Integer getType() {
        return type;
    }

    /**
     * type.
     *
     * @param type the type to set
     * @since JDK 1.8
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTophtml() {
        return tophtml;
    }

    public void setTophtml(String tophtml) {
        this.tophtml = tophtml;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * shopName.
     *
     * @return the shopName
     * @since JDK 1.8
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * shopName.
     *
     * @param shopName the shopName to set
     * @since JDK 1.8
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * shopImagePath.
     *
     * @return the shopImagePath
     * @since JDK 1.8
     */
    public String getShopImagePath() {
        return shopImagePath;
    }

    /**
     * shopImagePath.
     *
     * @param shopImagePath the shopImagePath to set
     * @since JDK 1.8
     */
    public void setShopImagePath(String shopImagePath) {
        this.shopImagePath = shopImagePath;
    }

}
