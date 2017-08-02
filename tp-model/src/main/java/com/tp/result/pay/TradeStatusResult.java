package com.tp.result.pay;

import java.io.Serializable;

public class TradeStatusResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2924533115427545632L;
	private boolean isSuccess;
	private int status;
	private boolean canceled;
	private String errorMsg;
	private String tradeNo;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	@Override
	public String toString() {
		return "TradeStatusResult [isSuccess=" + isSuccess + ", status="
				+ status + ", canceled=" + canceled + ", errorMsg=" + errorMsg
				+ ", tradeNo=" + tradeNo + "]";
	}
	
}
