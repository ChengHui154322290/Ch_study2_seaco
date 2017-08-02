package com.tp.query.prd;

import java.io.Serializable;

/**
 * 发布促销活动时查询商品信息接口条件
 * @author szy
 * 2014年12月25日 下午7:46:32
 *
 */
public class SkuSalesQuery implements Serializable{
	
	private static final long serialVersionUID = 1133064727930677269L;
	/** sku编号 */
	private String sku;
	/** spu编号*/
	private String spu;
	/** prdid编号*/
	private String prdid;
	/** 商户名称*/
	private String merchantName;
	/** 供应商名称*/
	private String spName;
	/** 商品名称*/
	private String detailName;
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSpu() {
		return spu;
	}
	public void setSpu(String spu) {
		this.spu = spu;
	}
	public String getPrdid() {
		return prdid;
	}
	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getDetailName() {
		return detailName;
	}
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
}
