package com.tp.dto.prd;

import java.io.Serializable;


/*** 
 *   sku 对应detail  信息dto
 * @author caihui
 *
 */
public class SkuDetailInfoDto  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4979953781515403212L;
	
	
	/***sku**/
	private String sku;
	
	/**detilid***/
	private Long detailId;
	/**itemId**/
	private Long itemId;
	
	/** prdid code***/
	private String prdid;
	
	/**初始销售数量***/
	private Integer defaultSalesCount;
	
	/**实际销售数量***/
	private Integer relSalesCount;
	
	/**销售数量总和**/
	private Integer allSalesCount;
	
	/**分销比例**/
	private Double commisionRate;
	
	
	

	public Double getCommisionRate() {
		return commisionRate;
	}

	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public Integer getDefaultSalesCount() {
		return defaultSalesCount;
	}

	public void setDefaultSalesCount(Integer defaultSalesCount) {
		this.defaultSalesCount = defaultSalesCount;
	}

	public Integer getRelSalesCount() {
		return relSalesCount;
	}

	public void setRelSalesCount(Integer relSalesCount) {
		this.relSalesCount = relSalesCount;
	}

	public Integer getAllSalesCount() {
		return allSalesCount;
	}

	public void setAllSalesCount(Integer allSalesCount) {
		this.allSalesCount = allSalesCount;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
}
