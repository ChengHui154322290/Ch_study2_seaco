package com.tp.m.query.seagoorpay;

import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/26.
 */
public class SeagoorPayRefundQueryVO  extends SeagoorPayBaseVO{

    private static final long serialVersionUID = 429686116196250444L;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String mer_trade_code;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String payment_code;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String mer_refund_code;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String refund_code;

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

    public String getRefund_code() {
        return refund_code;
    }

    public void setRefund_code(String refund_code) {
        this.refund_code = refund_code;
    }
}
