package com.tp.m.vo.topic;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.vo.product.ProductVO;

/**
 * 首页 - 今日上新TO
 * @author zhuss
 * @2016年1月2日 下午7:32:10
 */
public class TopicVO implements BaseVO{
	private static final long serialVersionUID = 4967008597100547367L;
	
	private String tid;//专场ID
	private String name; //专场名称
	private String imgurl;//专场图片URL
	/***************单品团******************/
	private String countryimg;//国旗
	private String countryname;//国家名称
	private String feature;//特色
	private String sku;
	private String oldprice; //原价
	private String price;//优惠价
	private String channel;//渠道
	private String shareurl;//单品团分享链接
	private String type;//专场类型 1单品团 2主题团
    private String canUseXgMoney;//能否使用西客币 1 可以使用  0 不能使用
		
	private List<ProductVO> itemlist;
	
	private String commision;	//佣金 for dss

	private String topicpoint;

	private String notice;

	private String shoplogo;

	public String getShoplogo() {
		return shoplogo;
	}

	public void setShoplogo(String shoplogo) {
		this.shoplogo = shoplogo;
	}

	public String getTopicpoint() {
		return topicpoint;
	}

	public void setTopicpoint(String topicpoint) {
		this.topicpoint = topicpoint;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public TopicVO() {
		super();
	}
	public TopicVO(String tid, String name, String imgurl) {
		super();
		this.tid = tid;
		this.name = name;
		this.imgurl = imgurl;
	}
		
	public String getCommision() {
		return commision;
	}
	public void setCommision(String commision) {
		this.commision = commision;
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
	public String getCountryimg() {
		return countryimg;
	}
	public void setCountryimg(String countryimg) {
		this.countryimg = countryimg;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getShareurl() {
		return shareurl;
	}
	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ProductVO> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<ProductVO> itemlist) {
		this.itemlist = itemlist;
	}

	public String getCanUseXgMoney() {
		return canUseXgMoney;
	}

	public void setCanUseXgMoney(String canUseXgMoney) {
		this.canUseXgMoney = canUseXgMoney;
	}
	
}
