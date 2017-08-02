package com.tp.result.prd;

import java.io.Serializable;
import java.util.List;

/**
 * 发布促销活动时查询商品信息接口
 * @author szy
 * 2014年12月25日 下午7:39:58
 *
 */
public class SkuSalesResult implements Serializable{
	private static final long serialVersionUID = 2514590598731793772L;
	/** skuId */
	private Long id;
	/** 商户Id */
	private Long merchantId;
	/** 商户名称 */
	private String merchantName;
	/** sku编号*/
	private String sku;
	/** 商品名称*/
	private String detailName;
	/** 商品prdid维度信息id*/
	private Long detailId;
	/** 颜色id*/
	private Long colorId;
	/** 尺码id */
	private Long sizeId;
	/** 主图*/
	private String picture;
	/** 仓库id*/
	private Long storageId;
	/** 仓库名称*/
	private String storageName;
	/** 库存数量*/
	private Integer inventry;
	/** 商品规格 */
	List<DetailSpecResult> specList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public Long getColorId() {
		return colorId;
	}

	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	public Long getSizeId() {
		return sizeId;
	}

	public void setSizeId(Long sizeId) {
		this.sizeId = sizeId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public Integer getInventry() {
		return inventry;
	}

	public void setInventry(Integer inventry) {
		this.inventry = inventry;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public List<DetailSpecResult> getSpecList() {
		return specList;
	}

	public void setSpecList(List<DetailSpecResult> specList) {
		this.specList = specList;
	}
	
}
