package com.tp.model.sch;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/1.
 */
public class TimeStamp extends BaseDO {

    private static final long serialVersionUID = 2976871565493347408L;

    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
