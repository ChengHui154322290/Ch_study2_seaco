package com.tp.dto.ord.remote;

import java.io.Serializable;

/**
 * 发票明细dto
 * 
 * @author szy
 * @version 0.0.1
 */
public class ReceiptDetailDTO implements Serializable {
	
	private static final long serialVersionUID = 3187895719208995751L;
	
	private String typeStr;
	/** 发票号码csv */
	private String numberStr;

	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getNumberStr() {
		return numberStr;
	}
	public void setNumberStr(String numberStr) {
		this.numberStr = numberStr;
	}
}
