package com.tp.ptm.dtos;


import java.io.Serializable;
/**
 * 商品精准推荐dto
 * @author beck
 *
 */
public class ItemDto implements Serializable{

	private static final long serialVersionUID = 8817795136461322681L;
	/** 网站展示标题 */
	private String title;
	/** 商品的卖点描述 */
	private String desc;
	/** 商品市场价 */
	private Double original_price;
	/** 商品西客价 */
	private Double current_price;
	/** 商品主图 */
	private String product_pic;
	/** 折扣 */
	private Double discount;
	/** 该商品pc端口访问地址 */
	private String pc_url;
	/** 该商品wap端访问地址 */
	private String wap_url;
	
	private String sku;
	/** 广告图片地址 */
	private String advImgUrl;
	
	/** 品牌名称 */
	private String brandName;
	/** 种类名称，默认中类 */
	private String categoryName;
	/** 置顶评论名称 */
	private String content;
	/** 评论用户昵称 */
	private String commnetUser;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(Double original_price) {
		this.original_price = original_price;
	}

	public Double getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(Double current_price) {
		this.current_price = current_price;
	}

	public String getProduct_pic() {
		return product_pic;
	}

	public void setProduct_pic(String product_pic) {
		this.product_pic = product_pic;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getPc_url() {
		return pc_url;
	}

	public void setPc_url(String pc_url) {
		this.pc_url = pc_url;
	}

	public String getWap_url() {
		return wap_url;
	}

	public void setWap_url(String wap_url) {
		this.wap_url = wap_url;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getAdvImgUrl() {
		return advImgUrl;
	}

	public void setAdvImgUrl(String advImgUrl) {
		this.advImgUrl = advImgUrl;
	}

	public String getCommnetUser() {
		return commnetUser;
	}

	public void setCommnetUser(String commnetUser) {
		this.commnetUser = commnetUser;
	}
	
}
