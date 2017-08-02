package com.tp.m.to.order;

import java.util.List;

import com.tp.m.base.BaseTO;

/**
 * 订单行
 * @author zhuss
 * @2016年1月7日 下午6:38:24
 */
public class OrderLineTO implements BaseTO{

	private static final long serialVersionUID = 6903164098400003575L;

	private String lineid;//订单子项ID
	private String sku;
	private String tid;
	private String name;
	private String price;
	private String lineprice;
	private String imgurl;
	private String count;
	private List<String> specs;
	private String isreturn;//是否有退货 0否1是
	private String commision;	// 佣金
		
	public String getCommision() {
		return commision;
	}
	public void setCommision(String commision) {
		this.commision = commision;
	}
	public String getLineid() {
		return lineid;
	}
	public void setLineid(String lineid) {
		this.lineid = lineid;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLineprice() {
		return lineprice;
	}
	public void setLineprice(String lineprice) {
		this.lineprice = lineprice;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public List<String> getSpecs() {
		return specs;
	}
	public void setSpecs(List<String> specs) {
		this.specs = specs;
	}
	public String getIsreturn() {
		return isreturn;
	}
	public void setIsreturn(String isreturn) {
		this.isreturn = isreturn;
	}
}
