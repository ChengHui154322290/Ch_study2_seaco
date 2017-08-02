package com.tp.dto.mmp.groupbuy;

import com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupBuyStatus;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/16.
 */
public class GroupbuyDetail extends BaseDO {
    private static final long serialVersionUID = 473194908817049912L;

    /**
     * 团购对应的专场Id
     */
    private Long topicId;

    /**
     * 团购活动Id
     */
    private Long groupbuyId;

    /**
     * 参团Id
     */
    private Long groupId;

    /**
     * 商品信息
     */
    private InfoDetailDto item;

    /**
     * 参团剩余时间 单位秒
     */
    private Long leftSecond;

    /**
     * 计划人数
     */
    private Integer planAmount;

    /**
     * 当前人数
     */
    private Integer factAmount;

    /**
     * 团购详情
     */
    private String detail;

    /**
     * 专场状态
     * 1 未开始 3 进行中 5 结束
     */
    private Integer topicStats;

    /**
     * 发起团购状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupLaunchStatus
     */
    private Integer launchStatus;

    /**
     * 团状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupStatus
     */
    public Integer groupStatus;

    /**
     * 参团状态
     * @see com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupJoinStatus
     */
    public Integer joinStatus;

    /**
     * 支付状态
     * @see GroupbuyGroupBuyStatus
     */
    public Integer payStatus;

    /**
     * 返回信息
     */
    public String message;

    /**
     * 团购类型 1 普通 2 新人
     */
    public Integer groupType;

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Long getGroupbuyId() {
        return groupbuyId;
    }

    public void setGroupbuyId(Long groupbuyId) {
        this.groupbuyId = groupbuyId;
    }

    public Integer getTopicStats() {
        return topicStats;
    }

    public void setTopicStats(Integer topicStats) {
        this.topicStats = topicStats;
    }

    public Integer getLaunchStatus() {
        return launchStatus;
    }

    public void setLaunchStatus(Integer launchStatus) {
        this.launchStatus = launchStatus;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public InfoDetailDto getItem() {
        return item;
    }

    public void setItem(InfoDetailDto item) {
        this.item = item;
    }

    public Long getLeftSecond() {
        return leftSecond;
    }

    public void setLeftSecond(Long leftSecond) {
        this.leftSecond = leftSecond;
    }

    public Integer getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(Integer planAmount) {
        this.planAmount = planAmount;
    }

    public Integer getFactAmount() {
        return factAmount;
    }

    public void setFactAmount(Integer factAmount) {
        this.factAmount = factAmount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Integer groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Integer getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(Integer joinStatus) {
        this.joinStatus = joinStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
