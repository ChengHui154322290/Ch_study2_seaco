/**
 * 
 */
package com.tp.dto.wms.sto;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class STOStockoutBackItemDto implements Serializable{

	private static final long serialVersionUID = 1493237146086303653L;

	/** 订单编号 */
	private String txLogisticID;
	
	/** 快递单号 */
	private String mailNo;
	
	/** 状态：0失败1成功 */
	private Integer status;

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
