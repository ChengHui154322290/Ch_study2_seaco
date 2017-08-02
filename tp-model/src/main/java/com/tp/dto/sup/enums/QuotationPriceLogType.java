package com.tp.dto.sup.enums;

/**
 * Created by ldr on 2016/5/13.
 */
public enum QuotationPriceLogType {

    /**
     * 裸价
     */
    BASE_PRICE(1),

    /**
     * 代发价
     */
    SUM_PRICE(2),
    /**
     * 运费
     */
    FREIGHT(3),

    /**
     * 跨境综合税率
     */
    MUL_TAX_RATE(4),
    /**
     * 行邮税税率
     */
    TARRIF_TAX_TATE(5);



    private int code;

    QuotationPriceLogType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
