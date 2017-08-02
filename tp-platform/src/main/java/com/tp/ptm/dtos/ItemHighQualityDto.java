package com.tp.ptm.dtos;

import java.io.Serializable;

public class ItemHighQualityDto implements Serializable{
	
	private static final long serialVersionUID = -402576756948700186L;

//	/** 对应商品详情编码Id*/
//	private Long detailId;
	
	/** SPU+3位数流水码 */
	private String sku;
	
	/** 年龄段起始的key*/
	private Integer ageStartKey;
	
	/** 年龄段结束的key*/
	private Integer ageEndKey;

//	public final Long getDetailId() {
//		return detailId;
//	}
//
//	public final void setDetailId(Long detailId) {
//		this.detailId = detailId;
//	}

	public final String getSku() {
		return sku;
	}

	public final void setSku(String sku) {
		this.sku = sku;
	}

	public final Integer getAgeStartKey() {
		return ageStartKey;
	}

	public final void setAgeStartKey(Integer ageStartKey) {
		this.ageStartKey = ageStartKey;
	}

	public final Integer getAgeEndKey() {
		return ageEndKey;
	}

	public final void setAgeEndKey(Integer ageEndKey) {
		this.ageEndKey = ageEndKey;
	}
	
	

}
