package com.tp.dto.ptm.salesorder;


import java.io.Serializable;

/**
 * 快递日志记录查询请求参数实体
 * 
 * @author ZhuFuhua
 * 
 */
public class ExpressLogQueryParamDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 子订单编号 或者 退货单号 */
	private Long code;
	/** 快递单号 */
	private String packageNo;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
