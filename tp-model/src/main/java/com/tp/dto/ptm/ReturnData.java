/**
 * 
 */
package com.tp.dto.ptm;

import java.io.Serializable;

/**
 * 返回默认数据
 * 
 * @author ZhuFuhua
 * 
 */
public class ReturnData implements Serializable {

    private static final long serialVersionUID = -1620952831373453144L;

    /** 是否成功 */
    private final Boolean isSuccess;
    /** 错误编号 */
    private final Integer errorCode;
    /** 附带数据 */
    private final Object data;

    public ReturnData(Boolean isSuccess) {
        this(isSuccess, null, null);
    }

    public ReturnData(Boolean isSuccess, Integer errorCode) {
        this(isSuccess, errorCode, null);
    }

    public ReturnData(Boolean isSuccess, Object data) {
        this(isSuccess, null, data);
    }

    public ReturnData(Boolean isSuccess, Integer errorCode, Object data) {
        this.isSuccess = isSuccess;
        if(errorCode == null){
        	if(isSuccess)
        		this.errorCode = 0;
        	else
        		this.errorCode = -1;
        }
        else{
        	this.errorCode = errorCode;
        }
        this.data = data;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }
}
