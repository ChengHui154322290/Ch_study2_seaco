package com.tp.dto.sch.enums;


/**
 * Created by ldr on 2016/5/20.
 */
public enum SearchBlacklistType {

    TOPIC(1,"专场"),
    ITEM(2,"商品");

    private int code;
    private String desc;

    SearchBlacklistType(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getDescByCode(Integer code){
        if(code == null){
            return null;
        }
        for(SearchBlacklistType type: SearchBlacklistType.values()){
            if(type.code == code) return type.desc;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
