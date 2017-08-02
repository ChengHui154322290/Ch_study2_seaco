package com.tp.dto.prd;

import java.io.Serializable;

/***
 * 商品详情信息Dto
 * @author 
 *
 */
public class ItemDetailForTransDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 */
	/**detailId****/
	private Long detailId;
	/***sku ID***/
	private Long skuId;
	/**sku**/
	private String sku;
	/***spu code*/
	private String spu;
	/***供应商ID**/
	private Long spId;
	
	/**品牌名称**/
	private String brandName;
	/**商品spu名称 **/
	private String spuName;
	
	/**商品名称 **/
	private String itemTitle;
	/**产品前台展示名称**/
	private String mainTitle;
	/**存储在仓库中的名称**/
	private String storageTitle;
	
	/***商品子标题(卖点描述)**/
	private String subTitle ;
	
	/**商品详情描述**/
	private String detailDesc;
	/**一级分类 id**/
	private Long firstCategoryId;
	/**一级分类名称**/
	private String firstCategoryName;
	/**二级分类 id**/
	private Long secondCategoryId;
	/**二级分类名称**/
	private String secondCategoryName;
	/**三级分类 id**/
	private Long thirdCategoryId;
	/**三级分类名称**/
	private String thirdCategoryName;
	/**图片链接**/
	private String picUrl;
	
	public Long getFirstCategoryId() {
		return firstCategoryId;
	}

	public void setFirstCategoryId(Long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public void setFirstCategoryName(String firstCategoryName) {
		this.firstCategoryName = firstCategoryName;
	}

	public Long getSecondCategoryId() {
		return secondCategoryId;
	}

	public void setSecondCategoryId(Long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public Long getThirdCategoryId() {
		return thirdCategoryId;
	}

	public void setThirdCategoryId(Long thirdCategoryId) {
		this.thirdCategoryId = thirdCategoryId;
	}

	public String getThirdCategoryName() {
		return thirdCategoryName;
	}

	public void setThirdCategoryName(String thirdCategoryName) {
		this.thirdCategoryName = thirdCategoryName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

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

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getStorageTitle() {
		return storageTitle;
	}

	public void setStorageTitle(String storageTitle) {
		this.storageTitle = storageTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	
}
