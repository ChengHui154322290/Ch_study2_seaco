package com.tp.m.to.product;

import java.util.List;

import com.tp.m.base.BaseTO;

/**
 * SKU
 * @author zhuss
 * @2016年1月5日 下午3:22:23
 */
public class SkuTO implements BaseTO{

	private static final long serialVersionUID = -1995837164755248768L;
	private String sku;
	private List<SkuDetailTO> skudetails;
	
	
	public SkuTO() {
		super();
	}
	public SkuTO(String sku, List<SkuDetailTO> skudetails) {
		super();
		this.sku = sku;
		this.skudetails = skudetails;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public List<SkuDetailTO> getSkudetails() {
		return skudetails;
	}
	public void setSkudetails(List<SkuDetailTO> skudetails) {
		this.skudetails = skudetails;
	}

}