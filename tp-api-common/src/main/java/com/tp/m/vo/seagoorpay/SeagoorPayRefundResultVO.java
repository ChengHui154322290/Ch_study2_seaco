package com.tp.m.vo.seagoorpay;

import com.tp.m.query.seagoorpay.SeagoorPayBaseVO;

/**
 * Created by ldr on 2016/11/23.
 */
public class SeagoorPayRefundResultVO extends SeagoorPayBaseVO {

    private static final long serialVersionUID = 4248543963137040397L;

    private String payment_code;

    private String refund_code;

    private String mer_trade_code;

    private String mer_refund_code;

    private Integer total_fee;

    private Integer refund_fee;

    private Integer status;

    private String status_desc;

    private String operator_id;

    private String refund_time;

    private String device_info;

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getRefund_code() {
        return refund_code;
    }

    public void setRefund_code(String refund_code) {
        this.refund_code = refund_code;
    }

    public String getMer_trade_code() {
        return mer_trade_code;
    }

    public void setMer_trade_code(String mer_trade_code) {
        this.mer_trade_code = mer_trade_code;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
    }
}
