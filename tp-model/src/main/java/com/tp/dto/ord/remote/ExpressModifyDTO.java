package com.tp.dto.ord.remote;


import java.io.Serializable;

/**
 * <pre>
 * 快递信息修改实体类
 * </pre>
 * 
 */
public class ExpressModifyDTO implements Serializable {

    private static final long serialVersionUID = -8607806333886072413L;

    /** 订单号 */
    private Long orderNo;

    /** 快递公司编号 */
    private String companyNo;

    /** 快递公司名称 */
    private String companyName;

    /** 新运单号 */
    private String newExpressNo;

    /** 原始运单号 */
    private String originalExpressNo;

    /** 变更人账号 */
    private String modifyAccount;

    /** 变更人类型 */
    private Integer userType;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNewExpressNo() {
        return newExpressNo;
    }

    public void setNewExpressNo(String newExpressNo) {
        this.newExpressNo = newExpressNo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getOriginalExpressNo() {
        return originalExpressNo;
    }

    public void setOriginalExpressNo(String originalExpressNo) {
        this.originalExpressNo = originalExpressNo;
    }

    public String getModifyAccount() {
        return modifyAccount;
    }

    public void setModifyAccount(String modifyAccount) {
        this.modifyAccount = modifyAccount;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
