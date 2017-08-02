package com.tp.m.query.search;

import com.tp.m.base.BaseQuery;

/**
 * 搜索查询条件
 * @author zhuss
 * @2016年3月2日 下午2:26:25
 */
public class QuerySearch extends BaseQuery{

	private static final long serialVersionUID = -4644362382624824840L;

	private String categoryid;//类目ID
	private String brandid;//导航品牌id
	private String key;
	private String sort;
	/**筛选**/
	private String country_id;//产地ID
	private String brand_id;//筛选条件
	private String isinstock;//是否只显示有库存的，1是，0否 默认0
	private String salespattern;//销售类型　9 线下团购
	private String couponid;//优惠券Id 用于查询优惠券可用商品

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public String getSalespattern() {
		return salespattern;
	}

	public void setSalespattern(String salespattern) {
		this.salespattern = salespattern;
	}

	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getBrandid() {
		return brandid;
	}
	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}
	public String getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getIsinstock() {
		return isinstock;
	}
	public void setIsinstock(String isinstock) {
		this.isinstock = isinstock;
	}
	public String getCountry_id() {
		return country_id;
	}
	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}
}
