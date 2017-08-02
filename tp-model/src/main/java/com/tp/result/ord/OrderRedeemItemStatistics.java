package com.tp.result.ord;

import java.io.Serializable;

public class OrderRedeemItemStatistics implements Serializable {

	private static final long serialVersionUID = -1811552367026796622L;

	/**预计收入*/
	private Double expectIncome=0d;
	/**退款金额*/
	private Double refunedAmount=0d;
	/**实际收入*/
	private Double actualIncome=0d;
	
	public Double getExpectIncome() {
		return expectIncome;
	}
	public void setExpectIncome(Double expectIncome) {
		this.expectIncome = expectIncome;
	}
	public Double getRefunedAmount() {
		return refunedAmount;
	}
	public void setRefunedAmount(Double refunedAmount) {
		this.refunedAmount = refunedAmount;
	}
	public Double getActualIncome() {
		return actualIncome;
	}
	public void setActualIncome(Double actualIncome) {
		this.actualIncome = actualIncome;
	}
	
}
