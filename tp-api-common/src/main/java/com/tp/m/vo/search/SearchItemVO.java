package com.tp.m.vo.search;

import com.tp.m.base.BaseVO;

/**
 * 搜索结果 - 商品列表
 * @author zhuss
 * @2016年3月2日 下午3:28:15
 */
public class SearchItemVO implements BaseVO{

	private static final long serialVersionUID = 2911220046849709650L;

	private String sku;
	private String tid;
	private String name;
	private String imgurl;
	private String oldprice;
	private String price;
	private String countryname;
	private String channel;//渠道
	private String channelid;//渠道id
	private String status;//状态 1正常  2已抢光 3已下架
	private String statusdesc;//状态描述
	private String salescount;//销售数量
	private String salespattern;
	private String shopname;

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getSalespattern() {
		return salespattern;
	}

	public void setSalespattern(String salespattern) {
		this.salespattern = salespattern;
	}

	public String getSalescount() {
		return salescount;
	}

	public void setSalescount(String salescount) {
		this.salescount = salescount;
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
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getOldprice() {
		return oldprice;
	}
	public void setOldprice(String oldprice) {
		this.oldprice = oldprice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusdesc() {
		return statusdesc;
	}
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}
}
