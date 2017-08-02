package com.tp.dto.ord;

/**
 * 团购券兑换
 * @author szy
 *
 */
public class GroupCouponExchangeDto implements BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8494457549997096709L;
	
	private String exchangeCode;
	private String userMobile;
	private Long supplierId;
	private String supplierName;
	private Long fastUserId;
	private String userName;

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getFastUserId() {
		return fastUserId;
	}

	public void setFastUserId(Long fastUserId) {
		this.fastUserId = fastUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

}
