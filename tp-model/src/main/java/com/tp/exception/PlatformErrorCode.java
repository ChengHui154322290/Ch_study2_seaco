package com.tp.exception;


import org.apache.commons.lang3.StringUtils;

/**
 * 系统错误码枚举
 *
 * @date 2015年8月12日上午11:47:14
 */
public enum PlatformErrorCode {
    SUCCESS("SUCCESS", "6000000", "成功"),

    FAIL("FAIL", "6000001", "失败"),

    /******************************** 聚美通用错误码 1000000 ****************************************/
    ACCESS_FREQUENCY_ERROR("ACCESS_FREQUENCY_ERROR", "1000001", "访问频率异常错误"),

    DELIVERGOODS_ORDERNUMBER_ERROR("DELIVERGOODS_ORDERNUMBER_ERROR", "1000002", "发货订单数量超过100"),

    DELIVERGOODS_PARAMS_ERROR("DELIVERGOODS_PARAMS_ERROR", "1000003", "发货参数校验错误"),

    DELIVERGOODS_VERIFY_EXCEPTION_ERROR("DELIVERGOODS_VERIFY_EXCEPTION_ERROR", "1000004", "发货权限验证异常"),

    DELIVERGOODS_APPKEY_ERROR("DELIVERGOODS_APPKEY_ERROR", "1000005", "供应商appkey错误"),

    DELIVERGOODS_NOTEXIST_ERROR("DELIVERGOODS_NOTEXIST_ERROR", "1000006", "订单编号不存在"),

    /********************************* 通用验证错误码2000000 **********************************************************/
    REQUEST_PARAMS_ERROR("REQUEST_PARAMS_ERROR", "2000001", "请求参数不合法");

    private String errorCode;

    private String errorNumCode;

    private String errorDesc;

    private PlatformErrorCode(final String errorCode, final String errorNumCode, final String errorDesc) {
        this.errorCode = errorCode;
        this.errorNumCode = errorNumCode;
        this.errorDesc = errorDesc;
    }

    /**
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return
     */
    public String getErrorMsg() {
        return errorDesc;
    }

    /**
     * @return
     */
    public String getErrorNumCode() {
        return errorNumCode;
    }

    public static PlatformErrorCode getByCode(final String code) {
        for (PlatformErrorCode value : values()) {
            if (StringUtils.equals(value.getErrorCode(), code)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "errorCode[" + getErrorCode() + "],errorNumCode[" + getErrorNumCode() + "],errorMsg[" + getErrorMsg() + "]";
    }

}
