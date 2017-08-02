package com.tp.m.query.order;

import java.util.Date;

import com.tp.m.base.BaseQuery;

/**
 * 团购券
 * @author szy
 */
public class QueryRedeemItem extends BaseQuery{

	private static final long serialVersionUID = -121084212761334892L;

	/**团购项目*/
	private String skuCode;
	/**团购状态*/
	private Integer redeemCodeState;
	/**兑换券号*/
	private String redeemCode;
	/**购买日期开始*/
	private Date beginDate;
	/**购买日期结束*/
	private Date endDate;
	
	/**店铺id*/
	private Long warehouseId;
	
	private String mobile;
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Integer getRedeemCodeState() {
		return redeemCodeState;
	}
	public void setRedeemCodeState(Integer redeemCodeState) {
		this.redeemCodeState = redeemCodeState;
	}
	public String getRedeemCode() {
		return redeemCode;
	}
	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
