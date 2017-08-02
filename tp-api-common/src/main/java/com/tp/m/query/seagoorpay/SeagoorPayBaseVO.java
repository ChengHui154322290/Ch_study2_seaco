package com.tp.m.query.seagoorpay;

import java.io.Serializable;

import com.tp.m.query.seagoorpay.annotation.Verify;

/**
 * Created by ldr on 2016/11/24.
 */
public class SeagoorPayBaseVO implements Serializable {

    private static final long serialVersionUID = -4473284770065527272L;

    @Verify(maxLength = 32)
    private String merchant_id;

    @Verify(maxLength = 32)
    private String rand_str;

    @Verify(maxLength = 32)
    private String sign;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getRand_str() {
        return rand_str;
    }

    public void setRand_str(String rand_str) {
        this.rand_str = rand_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
