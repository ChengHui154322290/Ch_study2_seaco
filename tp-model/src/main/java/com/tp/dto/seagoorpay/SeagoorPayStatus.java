package com.tp.dto.seagoorpay;

/**
 * Created by ldr on 2016/11/23.
 */
public enum SeagoorPayStatus {

    NOT_PAY(1,"未支付"),

    SUCCESS(2,"支付成功"),

    PAYING(3,"支付中"),

    CLOSED(4,"订单已关闭"),

    PAY_ERROR(5,"支付错误");

    private int code;

    private String desc;

    SeagoorPayStatus(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code){
        if(code == null) return null;
        for(SeagoorPayStatus seagoorPayStatus: SeagoorPayStatus.values()){
            if(seagoorPayStatus.getCode() == code){
                return seagoorPayStatus.getDesc();
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
