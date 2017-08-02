package com.tp.dto.pay.tonglian;

import java.io.Serializable;

public class TonglianPayRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TxInfo txInfo;
	
	private String signMsg;

	public TxInfo getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(TxInfo txInfo) {
		this.txInfo = txInfo;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	
	

}
