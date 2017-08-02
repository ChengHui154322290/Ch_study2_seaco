package com.tp.dto.ptm.salesorder;


import java.io.Serializable;

/**
 * @author ZhuFuhua
 *
 */
public class ExpressLogQueryErrorDTO implements Serializable{
    private static final long serialVersionUID = -8804700984750002233L;
    /** 订单编号 */
    private Long orderCode;
    /** 错误编码 */
    private Integer errorCode;
    /** 错误信息 */
	private String errorMsg;

	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
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
