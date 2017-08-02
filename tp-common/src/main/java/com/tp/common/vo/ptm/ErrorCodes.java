package com.tp.common.vo.ptm;

/**
 * 错误编号
 * 
 * @author lei
 * @version 2014年12月24日 下午2:24:12
 */
public interface ErrorCodes {

    public enum SystemError {
        SYSTEM_ERROR(0, "系统内部错误"), 
        PARAM_ERROR(1, "入参错误"),
        MD5_ENCRYPT_ERROR(2,"MD5加密错误"),
        BUSINESS_PROCESS_ERROR(3,"业务处理失败"),
        SYSTEM_PARAM_NOT_NULL(100,"系统级别参数不可为空'"),
        PROCESS_SIZE_UNMATCH_LIMIT(101,"处理列表数量不符合限制"),
        ACCESS_OVERLOAD(102,"访问频率超过限制");
        
        public Integer code;
        public String cnName;

        SystemError(Integer code, String cnName) {
            this.code = code;
            this.cnName = cnName;
        }

        public static String getCnName(Integer code) {
            if (code != null) {
                for (SystemError entry : SystemError.values()) {
                    if (entry.code.intValue() == code) {
                        return entry.cnName;
                    }
                }
            }
            return null;
        }
    }

    public enum AuthError {
        VERIFY_EXCEPTION(1001, "权限验证处理异常"), 
        UNPASS_AUTH(1002, "权限验证不通过"),
        UNPASS_SIGN(1003,"SIGN签名验证不通过"),
        ACCESS_ILLEGAL_DATA(1004,"越权访问数据"),
        TIME_STAMP_EXPIRE(1005,"时间戳过期"),
        TIME_STAMP_PATTERN(1006,"时间戳格式错误");

        public Integer code;
        public String cnName;

        AuthError(Integer code, String cnName) {
            this.code = code;
            this.cnName = cnName;
        }

        public static String getCnName(Integer code) {
            if (code != null) {
                for (AuthError entry : AuthError.values()) {
                    if (entry.code.intValue() == code) {
                        return entry.cnName;
                    }
                }
            }
            return null;
        }
    }

    /*-------------------------------------- 快递信息异常（11K） ------------------------------------------*/
    public enum ExpressError {
        PARAM_ILLEGAL(11001, "请求参数不合法"),
        UPDATE_EXPRESSNO_EXCEPTION(11002, "更新快递单号失败"),
        ORIGINAL_EXPRESSNO_NOT_EXIT(11003, "原始快递单号错误");

        public Integer code;
        public String cnName;

        ExpressError(Integer code, String cnName) {
            this.code = code;
            this.cnName = cnName;
        }

        public static String getCnName(Integer code) {
            if (code != null) {
                for (ExpressError entry : ExpressError.values()) {
                    if (entry.code.intValue() == code) {
                        return entry.cnName;
                    }
                }
            }
            return null;
        }
    }

}
