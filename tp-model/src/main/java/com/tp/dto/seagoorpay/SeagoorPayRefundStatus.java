package com.tp.dto.seagoorpay;

/**
 * Created by ldr on 2016/11/25.
 */
public enum SeagoorPayRefundStatus {

    PROCESSING(1,"退款中"),

    SUCCESS(2,"退款成功"),

    FAIL(3,"退款失败");

    private int code;

    private String desc;

    SeagoorPayRefundStatus(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code){
        if (code == null) return null;
        for(SeagoorPayRefundStatus seagoorPayRefundStatus: SeagoorPayRefundStatus.values()){
            if(seagoorPayRefundStatus.getCode() == code) return  seagoorPayRefundStatus.getDesc();
        }
        return null;
    }
}
