package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 迷你购物车
 * 2015-1-9
 */
public class Commodity  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6601621786168811034L;

	private String detail;
	
	private String SettleaccountsUrl;
	
	private String commodityImgSrc;
	
	private String commodityLink;

	private String commodityName;
	
	private String commodityDetail;
	
	private String commodityPrice;
	
	private String commodityCount;
	
	private String action;
	
	private String actionLink;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSettleaccountsUrl() {
		return SettleaccountsUrl;
	}

	public void setSettleaccountsUrl(String settleaccountsUrl) {
		SettleaccountsUrl = settleaccountsUrl;
	}

	public String getCommodityImgSrc() {
		return commodityImgSrc;
	}

	public void setCommodityImgSrc(String commodityImgSrc) {
		this.commodityImgSrc = commodityImgSrc;
	}

	public String getCommodityLink() {
		return commodityLink;
	}

	public void setCommodityLink(String commodityLink) {
		this.commodityLink = commodityLink;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityDetail() {
		return commodityDetail;
	}

	public void setCommodityDetail(String commodityDetail) {
		this.commodityDetail = commodityDetail;
	}

	public String getCommodityPrice() {
		return commodityPrice;
	}

	public void setCommodityPrice(String commodityPrice) {
		this.commodityPrice = commodityPrice;
	}

	public String getCommodityCount() {
		return commodityCount;
	}

	public void setCommodityCount(String commodityCount) {
		this.commodityCount = commodityCount;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionLink() {
		return actionLink;
	}

	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}
	
	
}
