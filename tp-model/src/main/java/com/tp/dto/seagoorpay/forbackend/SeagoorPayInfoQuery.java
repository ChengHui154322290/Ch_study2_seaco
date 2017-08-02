package com.tp.dto.seagoorpay.forbackend;


import java.util.Date;

/**
 * Created by ldr on 2016/12/1.
 */
public class SeagoorPayInfoQuery {

     Long memberId;

    String memberMobile;

    Integer status;

    String merchantId;

    String paymentCode;

    String merPayCode;

    String refundCode;

    String merRefundCode;

    Date startTime;
    
    Date endTime;
    
    Integer page;
    
    Integer size;


    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public String getMerRefundCode() {
        return merRefundCode;
    }

    public void setMerRefundCode(String merRefundCode) {
        this.merRefundCode = merRefundCode;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getMerPayCode() {
        return merPayCode;
    }

    public void setMerPayCode(String merPayCode) {
        this.merPayCode = merPayCode;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
