package com.tp.m.vo.product;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 商品基础类
 * @author zhuss
 * @2016年1月5日 上午11:11:17
 */
public class ProductVO implements BaseVO{

	private static final long serialVersionUID = 3741176107074288168L;

	//private String pid;
	private String name;
	private String sku;
	private String oldprice; //原价
	private String price;//优惠价
	private String status;//状态 1正常  2已抢光 3已下架 4未开始
	private String statusdesc;//状态描述
	private String discount;
	private String imgurl;
	private String tid;
	private String disamount;
	private String salescountdesc;
	
	/**用于购物车中*/
	private String selected;//是否选中 0否1是  - 
	private String taxrate;//税率
	private String taxfee;//税费
	private String taxdesc;//税费说明
	private String count;
	private String channel;//渠道
	private String channelid;//渠道id
	private String stock;//库存
	private String limitcount;//限购数量
	private String lineprice;//购物车行
	private String weight;//商品毛重

	private String commision;//佣金 for dss

	private String shopname;//店铺名称 for offline groupbuy

	private List<String> itemspecs; //商品规格
	
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getSalescountdesc() {
		return salescountdesc;
	}

	public void setSalescountdesc(String salescountdesc) {
		this.salescountdesc = salescountdesc;
	}

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}

	public String getDisamount() {
		return disamount;
	}

	public void setDisamount(String disamount) {
		this.disamount = disamount;
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
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
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
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getStatusdesc() {
		return statusdesc;
	}
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}
	public String getLimitcount() {
		return limitcount;
	}
	public void setLimitcount(String limitcount) {
		this.limitcount = limitcount;
	}
	public String getLineprice() {
		return lineprice;
	}
	public void setLineprice(String lineprice) {
		this.lineprice = lineprice;
	}

	public String getTaxfee() {
		return taxfee;
	}

	public void setTaxfee(String taxfee) {
		this.taxfee = taxfee;
	}

	public String getTaxdesc() {
		return taxdesc;
	}

	public void setTaxdesc(String taxdesc) {
		this.taxdesc = taxdesc;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<String> getItemspecs() {
		return itemspecs;
	}

	public void setItemspecs(List<String> itemspecs) {
		this.itemspecs = itemspecs;
	}
}
