package com.tp.dto.ptm.storage;


import java.io.Serializable;

/**
 * {订单发货信息} <br>
 * Create on : 2015年12月2日 下午11:50:41<br>
 * 
 * @author Administrator<br>
 * @version platform-common v0.0.1
 */
public class OutputOrderDto implements Serializable {

    private static final long serialVersionUID = -2584991510003988309L;

    /** 订单编号（子单） */
    private Long orderCode;

    /** 运单号,如果有多个订单号，则以半角逗号分隔 */
    private String packageNo;

    /** 物流公司名称 */
    private String companyName;

    /** 物流公司编码 */
    private String companyCode;

    /** 是否通过校验,校验失败：1：成功为1,失败：2 **/
    private int validateFlag;

    /** 校验失败的信息, **/
    private String validateMsg;

    /**
     * @return the subOrderCode
     */
    public Long getOrderCode() {
        return orderCode;
    }

    /**
     * @param subOrderCode the subOrderCode to set
     */
    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * @return the packageNo
     */
    public String getPackageNo() {
        return packageNo;
    }

    /**
     * @param packageNo the packageNo to set
     */
    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * @return the validateFlag
     */
    public int getValidateFlag() {
        return validateFlag;
    }

    /**
     * @param validateFlag the validateFlag to set
     */
    public void setValidateFlag(int validateFlag) {
        this.validateFlag = validateFlag;
    }

    /**
     * @return the validateMsg
     */
    public String getValidateMsg() {
        return validateMsg;
    }

    /**
     * @param validateMsg the validateMsg to set
     */
    public void setValidateMsg(String validateMsg) {
        this.validateMsg = validateMsg;
    }
}
