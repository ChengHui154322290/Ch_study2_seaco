package com.tp.m.query.geetest;

import com.tp.m.base.BaseQuery;

/**
 * Created by ldr on 8/1/2016.
 */
public class Geetest extends BaseQuery {

    private static final long serialVersionUID = -4420329625203896696L;
    private String geetest_challenge;

    private String geetest_seccode;

    private String geetest_validate;

    private String randid;

    private String tel;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGeetest_challenge() {
        return geetest_challenge;
    }

    public void setGeetest_challenge(String geetest_challenge) {
        this.geetest_challenge = geetest_challenge;
    }

    public String getGeetest_seccode() {
        return geetest_seccode;
    }

    public void setGeetest_seccode(String geetest_seccode) {
        this.geetest_seccode = geetest_seccode;
    }

    public String getGeetest_validate() {
        return geetest_validate;
    }

    public void setGeetest_validate(String geetest_validate) {
        this.geetest_validate = geetest_validate;
    }

    public String getRandid() {
        return randid;
    }

    public void setRandid(String randid) {
        this.randid = randid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
