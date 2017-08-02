package com.tp.m.query.seagoorpay;

import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/26.
 */
public class SeagoorPayQueryListVO extends SeagoorPayBaseVO {

    private String mer_trade_code;

    private String payment_code;

    private Integer status;

   private String begin_time;

   private String end_time;

    private Integer cur_page;

    private Integer page_size;

    public Integer getCur_page() {
        return cur_page;
    }

    public void setCur_page(Integer cur_page) {
        this.cur_page = cur_page;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getMer_trade_code() {
        return mer_trade_code;
    }

    public void setMer_trade_code(String mer_trade_code) {
        this.mer_trade_code = mer_trade_code;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }
}
