package com.tp.result.pay.wexin;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 8/24/2016.
 */
public class WeixinBaseResult extends BaseDO {

    private static final long serialVersionUID = -8195825551278778293L;

    private String return_code;

    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
