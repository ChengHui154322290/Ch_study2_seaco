package com.tp.dto.ptm.salesorder;


import java.io.Serializable;

/**
 * 订单迷你DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class OrderMiniDTO implements Serializable {

	private static final long serialVersionUID = 823478536517129535L;

	/** 子单号 */
	private Long code;

	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	
}
