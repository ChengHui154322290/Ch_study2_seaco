package com.tp.m.query.comment;

import java.util.List;

import com.tp.m.base.BaseQuery;

/**
 * 评论入参
 * @author zhuss
 * @2016年1月4日 下午5:55:47
 */
public class QueryComment extends BaseQuery{

	private static final long serialVersionUID = 1098290816890708078L;

	private String ordercode;
	private String sku;
	private String content;
	private String serscore;
	private String itemscore;
	private List<String> imglist;
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSerscore() {
		return serscore;
	}
	public void setSerscore(String serscore) {
		this.serscore = serscore;
	}
	public String getItemscore() {
		return itemscore;
	}
	public void setItemscore(String itemscore) {
		this.itemscore = itemscore;
	}
	public List<String> getImglist() {
		return imglist;
	}
	public void setImglist(List<String> imglist) {
		this.imglist = imglist;
	}
}
