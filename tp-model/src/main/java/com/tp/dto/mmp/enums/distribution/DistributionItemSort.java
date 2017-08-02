package com.tp.dto.mmp.enums.distribution;

/**
 * Created by ldr on 2016/5/4.
 */
public enum DistributionItemSort {

    SORT_INDEX_ASC(1,"sort_index"),

    PRICE_ASC(2,"topic_price ASC"),

    PRICE_DESC(3,"topic_price DESC");

    private int code;

    private String value;

    DistributionItemSort(int code,String value){
        this.code = code;
        this.value = value;
    }


    public static String getValueByCode(Integer code){
        if(code == null) return SORT_INDEX_ASC.value;
        for(DistributionItemSort sort: DistributionItemSort.values()){
            if(sort.code == code){
                return sort.value;
            }
        }
        return SORT_INDEX_ASC.value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
