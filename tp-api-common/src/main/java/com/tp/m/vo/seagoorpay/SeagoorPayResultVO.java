package com.tp.m.vo.seagoorpay;

import com.tp.m.query.seagoorpay.SeagoorPayBaseVO;

/**
 * Created by ldr on 2016/11/23.
 */
public class SeagoorPayResultVO extends SeagoorPayBaseVO {

    private static final long serialVersionUID = 4248543963137040397L;

    private String payment_code;

    private String member_id;

    private String mer_trade_code;

    private Integer status;

    private String status_desc;

    private String device_info;

    private Integer total_fee;

    private String item_desc;

    private String item_detail;

    private String item_tag;

    private String attach;

    private String pay_time;


    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMer_trade_code() {
        return mer_trade_code;
    }

    public void setMer_trade_code(String mer_trade_code) {
        this.mer_trade_code = mer_trade_code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }
}
