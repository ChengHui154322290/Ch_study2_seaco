/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * @author szy
 *
 */
public class CouponUserOrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3660231642530054856L;

	private String code;

	private Double payTotal;

	private Double discount;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the payTotal
	 */
	public Double getPayTotal() {
		return payTotal;
	}

	/**
	 * @param payTotal
	 *            the payTotal to set
	 */
	public void setPayTotal(Double payTotal) {
		this.payTotal = payTotal;
	}

	/**
	 * @return the discount
	 */
	public Double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(Double discount) {
		this.discount = discount;
	}

}
