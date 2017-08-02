package com.tp.dto.mmp.groupbuy;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/3/16.
 */
public class GroupbuyJoinInfo extends BaseDO {

    private static final long serialVersionUID = -3091031972663847692L;

    public Long groupId;

    public Integer status;

    public Integer canJoin;

    public Integer canPay;

    public String message;

    public Long leftSecond;

    public Integer planAmount;

    private Integer currentAmount;

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

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCanJoin() {
        return canJoin;
    }

    public void setCanJoin(Integer canJoin) {
        this.canJoin = canJoin;
    }

    public Integer getCanPay() {
        return canPay;
    }

    public void setCanPay(Integer canPay) {
        this.canPay = canPay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
