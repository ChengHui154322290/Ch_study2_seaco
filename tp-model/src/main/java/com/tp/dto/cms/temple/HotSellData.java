/**
 * 
 */
package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 首页-热销榜单的dto实体类
 * 
 * @version 0.0.1
 * 
 */
public class HotSellData implements Serializable {

	private static final long serialVersionUID = 7926686745912105706L;

	
	/**商品图片**/
	private String goodimg;
	
	/**链接地址**/
	private String link;
	
	/**活动id**/
	private Long actityId;
	
	/**活动名称**/
	private String actityName;
	
	/**原价格**/
	private double olderprice;
	
	/**活动价**/
	private double price;
	
	/**折扣**/
	private String discount;
	
	/**sku**/
	private String sku;
	
	/**根据价格计算出折扣信息**/
	private String discountPrice;

	public String getGoodimg() {
		return goodimg;
	}

	public void setGoodimg(String goodimg) {
		this.goodimg = goodimg;
	}

	public Long getActityId() {
		return actityId;
	}

	public void setActityId(Long actityId) {
		this.actityId = actityId;
	}

	public String getActityName() {
		return actityName;
	}

	public void setActityName(String actityName) {
		this.actityName = actityName;
	}

	public double getOlderprice() {
		return olderprice;
	}

	public void setOlderprice(double olderprice) {
		this.olderprice = olderprice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	
}
