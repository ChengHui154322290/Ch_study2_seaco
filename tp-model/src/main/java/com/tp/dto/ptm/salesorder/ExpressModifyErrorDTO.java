package com.tp.dto.ptm.salesorder;


import java.io.Serializable;

/**
 * 快递信息修改处理失败信息
 * 
 * 
 */
public class ExpressModifyErrorDTO implements Serializable {
    private static final long serialVersionUID = 240719845317416370L;
    /** 订单编号 */
    private Long orderCode;
    /** 新运单号 */
    private String newExpressNo;
    /** 原始运单号 */
    private String originalExpressNo;
    /** 错误编码 */
    private Integer errorCode;
    /** 错误信息 */
    private String errorMsg;

    public Long getOrderNo() {
        return orderCode;
    }

    public void setOrderNo(Long orderNo) {
        this.orderCode = orderNo;
    }

    public String getNewExpressNo() {
        return newExpressNo;
    }

    public void setNewExpressNo(String newExpressNo) {
        this.newExpressNo = newExpressNo;
    }

    public String getOriginalExpressNo() {
        return originalExpressNo;
    }

    public void setOriginalExpressNo(String originalExpressNo) {
        this.originalExpressNo = originalExpressNo;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
