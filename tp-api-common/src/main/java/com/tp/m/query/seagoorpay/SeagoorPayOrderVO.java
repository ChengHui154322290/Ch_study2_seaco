package com.tp.m.query.seagoorpay;

import java.io.Serializable;

import com.tp.m.base.BaseQuery;
import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/23.
 */
public class SeagoorPayOrderVO extends SeagoorPayBaseVO {

    private static final long serialVersionUID = -6460225136993231965L;

    @Verify(maxLength = 32)
    private String mer_trade_code;

    @Verify(nullable = true, maxLength = 32)
    private String device_info;

    @Verify(isInt = true)
    private Integer total_fee;

    @Verify(maxLength = 128)
    private String item_desc;

    @Verify(nullable = true, maxLength = 500)
    private String item_detail;

    @Verify(nullable = true, maxLength = 32)
    private String item_tag;

    @Verify(nullable = true, maxLength = 128)
    private String attach;

    @Verify(maxLength = 32)
    private String ip;

    @Verify(maxLength = 32)
    private String pay_code;

    public String getMer_trade_code() {
        return mer_trade_code;
    }

    public void setMer_trade_code(String mer_trade_code) {
        this.mer_trade_code = mer_trade_code;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_detail() {
        return item_detail;
    }

    public void setItem_detail(String item_detail) {
        this.item_detail = item_detail;
    }

    public String getItem_tag() {
        return item_tag;
    }

    public void setItem_tag(String item_tag) {
        this.item_tag = item_tag;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }
}
