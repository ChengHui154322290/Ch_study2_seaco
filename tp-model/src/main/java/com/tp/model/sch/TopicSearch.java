package com.tp.model.sch;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/16.
 */
public class TopicSearch extends BaseDO {

    private static final long serialVersionUID = 7686267141375338285L;

    private Long topicId;

    private Date topicStart;

    private Date topicEnd;

    private String platform;

    private Integer topicType;

    private Integer salesPattern;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSalesPattern() {
        return salesPattern;
    }

    public void setSalesPattern(Integer salesPattern) {
        this.salesPattern = salesPattern;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Date getTopicStart() {
        return topicStart;
    }

    public void setTopicStart(Date topicStart) {
        this.topicStart = topicStart;
    }

    public Date getTopicEnd() {
        return topicEnd;
    }

    public void setTopicEnd(Date topicEnd) {
        this.topicEnd = topicEnd;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
