package com.tp.m.query.seagoorpay;

import com.tp.m.base.BaseQuery;

/**
 * Created by ldr on 2016/11/19.
 */
public class QuerySeagoorPayCode extends BaseQuery {

    private static final long serialVersionUID = -6000920663215108502L;

    private String precode;

    private String code;

    public String getPrecode() {
        return precode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrecode(String precode) {
        this.precode = precode;
    }
}
