package com.tp.m.vo.seagoorpay;

import com.tp.m.base.BaseQuery;
import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 2016/11/28.
 */
public class SeagoorPayStatusVO implements BaseVO {

    private static final long serialVersionUID = 836335103362794653L;

    private String code;

    private String status;

    private String message;

    private Long memberid;

    private String consume;

    private String itemdesc;

    private String ordercode;

    private String time;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
