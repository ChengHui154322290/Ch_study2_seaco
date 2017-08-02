package com.tp.m.vo.groupbuy;

import com.tp.m.vo.product.ProductDetailVO;

/**
 * Created by ldr on 2016/3/21.
 */
public class GroupbuyDetailVO {

    /**
     * 商品信息
     */
    private ProductDetailVO product;

    /**
     * 团购对应的专场Id
     */
    private String tid;

    /**
     * 团购活动Id
     */
    private String gbid;

    /**
     * 参团Id
     */
    private String gid;

    /**
     * 参团剩余时间 单位秒
     */
    private String lsecond;

    /**
     * 计划人数
     */
    private String pa;

    /**
     * 当前人数
     */
    private String fa;

    /**
     * 团购详情
     */
    private String detail;

    /**
     * 专场状态
     * 1 未开始 3 进行中 5 结束
     */
    private String tstatus;

    /**
     * 发起团购状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupLaunchStatus
     */
    private String lstatus;

    /**
     * 团状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupStatus
     */
    public String gstatus;

    /**
     * 参团状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupJoinStatus
     */
    public String jstatus;

    /**
     * 支付状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupPayStatus
     */
    public String pstatus;

    /**
     * 团购类型 1 普通,2新人
     */
    public String gtype;

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public ProductDetailVO getProduct() {
        return product;
    }

    public void setProduct(ProductDetailVO product) {
        this.product = product;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getGbid() {
        return gbid;
    }

    public void setGbid(String gbid) {
        this.gbid = gbid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getLsecond() {
        return lsecond;
    }

    public void setLsecond(String lsecond) {
        this.lsecond = lsecond;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTstatus() {
        return tstatus;
    }

    public void setTstatus(String tstatus) {
        this.tstatus = tstatus;
    }

    public String getLstatus() {
        return lstatus;
    }

    public void setLstatus(String lstatus) {
        this.lstatus = lstatus;
    }

    public String getGstatus() {
        return gstatus;
    }

    public void setGstatus(String gstatus) {
        this.gstatus = gstatus;
    }

    public String getJstatus() {
        return jstatus;
    }

    public void setJstatus(String jstatus) {
        this.jstatus = jstatus;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }
}
