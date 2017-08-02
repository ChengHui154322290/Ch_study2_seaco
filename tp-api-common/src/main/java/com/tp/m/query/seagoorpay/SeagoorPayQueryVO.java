package com.tp.m.query.seagoorpay;

import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/26.
 */
public class SeagoorPayQueryVO extends SeagoorPayBaseVO {

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String mer_trade_code;

    @Verify(nullable = true, maxLength = 32, combine = "code")
    private String payment_code;

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
