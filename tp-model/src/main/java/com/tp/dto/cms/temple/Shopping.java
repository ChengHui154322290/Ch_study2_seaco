package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 迷你购物车
 * @author szy
 * 2015-1-9
 */
public class Shopping  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4081257141186640366L;

	/**
	 * 
	 */
	private String count; //购物车总数量
	
	private String price; //购物车总价格
	
	private String detail; //购物车详细"订单满30元返100元无门槛现金券"
	
	private String settleaccountsUrl; //购物车结算地址 
	
	private String date; //多长时间清空 
	
	private List<Commodity> commodityList = new ArrayList<Commodity>(); //购物车商品

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<Commodity> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<Commodity> commodityList) {
		this.commodityList = commodityList;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSettleaccountsUrl() {
		return this.settleaccountsUrl;
	}

	public void setSettleaccountsUrl(String settleaccountsUrl) {
		this.settleaccountsUrl = settleaccountsUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
