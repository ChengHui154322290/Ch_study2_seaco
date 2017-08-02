package com.tp.m.vo.seagoorpay;

import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 2016/11/19.
 */
public class SeagoorPayCodeVO implements BaseVO {

    private String qrcode;

    private String barcode;

    private String code;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
