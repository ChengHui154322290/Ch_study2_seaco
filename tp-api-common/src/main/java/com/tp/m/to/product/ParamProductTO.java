package com.tp.m.to.product;

import com.tp.m.base.BaseTO;

public class ParamProductTO implements BaseTO{

	private static final long serialVersionUID = -3859919833064909711L;

	private String tid;
	private String sku;
	private String price;
	private String count;
	
	public ParamProductTO() {
		super();
	}
	
	public ParamProductTO(String tid, String sku) {
		super();
		this.tid = tid;
		this.sku = sku;
	}

	public ParamProductTO(String tid, String sku, String count) {
		super();
		this.tid = tid;
		this.sku = sku;
		this.count = count;
	}
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
}
