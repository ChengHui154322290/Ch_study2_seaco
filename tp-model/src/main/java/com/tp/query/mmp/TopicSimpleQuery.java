package com.tp.query.mmp;

import java.io.Serializable;

import com.tp.dto.mmp.enums.CmsForcaseType;
import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/1/9.
 */
public class TopicSimpleQuery extends BaseDO implements Serializable{

    private static final long serialVersionUID = 5353808814488977035L;
    /**查询的参数数据*/
    private String data;

    /***平台类型*/
    private Integer platformType;

    /***所在地区的id */
    private Integer areaId;

    /**专题类型*/
    private Integer topicType;

    private Integer pageId  = 1;

    /** 返回结果方式*/
    private CmsForcaseType forcaseType;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public CmsForcaseType getForcaseType() {
        return forcaseType;
    }

    public void setForcaseType(CmsForcaseType forcaseType) {
        this.forcaseType = forcaseType;
    }
}
