package com.tp.m.vo.swt;

import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 2016/12/16.
 */
public class SwitchVO implements BaseVO{

    private static final long serialVersionUID = 2584029390635379136L;
    private String code;

    private String value;

    public SwitchVO(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
