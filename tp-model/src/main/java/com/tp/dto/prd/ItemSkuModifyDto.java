package com.tp.dto.prd;

import java.io.Serializable;

public class ItemSkuModifyDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2436445774179823266L;

	/**sku编码**/
	private String sku;
	
	/**网站主标题**/
	private String mainTitle;
	
	/**市场价**/
	private String basicPrice;
	
	/**商品详情市场价**/
	private String detailBasicPrice;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(String basicPrice) {
		this.basicPrice = basicPrice;
	}

	public String getDetailBasicPrice() {
		return detailBasicPrice;
	}

	public void setDetailBasicPrice(String detailBasicPrice) {
		this.detailBasicPrice = detailBasicPrice;
	}
}
