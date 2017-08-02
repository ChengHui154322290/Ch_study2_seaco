package com.tp.m.query.cart;

import java.util.List;

import com.tp.m.base.BaseQuery;
import com.tp.m.to.product.ParamProductTO;

public class QueryCart extends BaseQuery{

	private static final long serialVersionUID = 634221360265938942L;

	private String tid;//专题ID
	private String sku;//商品SKU
	private String count;//购买的商品的数量
	private String type;//操作类型
	private List<ParamProductTO> products;//商品集合
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ParamProductTO> getProducts() {
		return products;
	}
	public void setProducts(List<ParamProductTO> products) {
		this.products = products;
	}
}
