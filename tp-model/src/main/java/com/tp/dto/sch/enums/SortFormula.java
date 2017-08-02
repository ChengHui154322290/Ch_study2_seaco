package com.tp.dto.sch.enums;

/**
 * Created by ldr on 2016/3/7.
 */
public enum SortFormula {

    SEARCH(1,"search"),

    NAVIGATION(2,"navigation"),

    DEFAULT(3,"default");

    private int code;

    private String formula;

    SortFormula(int code, String formula){
        this.code = code;
        this.formula = formula;
    }

    public int getCode() {
        return code;
    }

    public String getFormula() {
        return formula;
    }
}
