package com.tp.ptm.dtos;

import java.io.Serializable;
/**
 * 热销商品推荐dto
 * @author beck
 *
 */
public class ItemHotDto implements Serializable{

	private static final long serialVersionUID = 8817795136461322681L;
	/** 商品市场价 */
	private Double original_price;
	/** 商品西客价 */
	private Double current_price;
	/** 库存 */
	private Integer inventory;
	/** 销量 */
	private Integer volume;
	/** 折扣 */
	private Double discount;
	/** 商品sku */
	private String sku;
	/** 活动id*/
	private Long topicId;
	/** 商品图片 */
	private String pic;
	/** 商品小类编号*/
	private String cat_code;
	/** 商品小类名称*/
	private String cat_name;
	/** 商品中类编号  */
	private String cat_middle_code;
	/** 商品中类名称 */
	private String cat_middle_name;
	/** 商品大类编号 */
	private String cat_large_code;
	/** 商品大类名称 */
	private String cat_large_name;
	
	public final String getCat_large_code() {
		return cat_large_code;
	}
	public final void setCat_large_code(String cat_large_code) {
		this.cat_large_code = cat_large_code;
	}
	public final String getCat_large_name() {
		return cat_large_name;
	}
	public final void setCat_large_name(String cat_large_name) {
		this.cat_large_name = cat_large_name;
	}
	public final String getCat_middle_code() {
		return cat_middle_code;
	}
	public final void setCat_middle_code(String cat_middle_code) {
		this.cat_middle_code = cat_middle_code;
	}
	public final String getCat_middle_name() {
		return cat_middle_name;
	}
	public final void setCat_middle_name(String cat_middle_name) {
		this.cat_middle_name = cat_middle_name;
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
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getCat_code() {
		return cat_code;
	}
	public void setCat_code(String cat_code) {
		this.cat_code = cat_code;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	
}
