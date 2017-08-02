package com.tp.dto.sch;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/4.
 */
public class SearchKeyCensusQuery extends BaseDO {

    private static final long serialVersionUID = -3170249246419995332L;

    private Integer searchType;

    private Date startTime;

    private Date endTime;


    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
