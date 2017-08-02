package com.tp.m.query.seagoorpay;

import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/25.
 */
public class SeagoorPayRefundVO extends SeagoorPayBaseVO {

    private static final long serialVersionUID = -104962805320148601L;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String mer_trade_code;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String payment_code;

    @Verify(maxLength = 32)
    private String mer_refund_code;

    @Verify(isInt = true)
    private Integer total_fee;

    @Verify(isInt = true)
    private Integer refund_fee;

    @Verify(maxLength = 32)
    private String ip;

    @Verify(maxLength = 32)
    private String operator_id;

    @Verify(nullable = true, maxLength = 32)
    private String device_info;

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

    public String getMer_refund_code() {
        return mer_refund_code;
    }

    public void setMer_refund_code(String mer_refund_code) {
        this.mer_refund_code = mer_refund_code;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Integer refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
