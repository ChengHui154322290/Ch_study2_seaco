package com.tp.m.vo.geetest;

import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 8/1/2016.
 */
public class GeetestResult implements BaseVO {

    private static final long serialVersionUID = 678842684584167957L;
    private int success;
    private String gt;
    private String challenge;
    private String randid;

    public int getSuccess() {

        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getRandid() {
        return randid;
    }

    public void setRandid(String randid) {
        this.randid = randid;
    }
}
