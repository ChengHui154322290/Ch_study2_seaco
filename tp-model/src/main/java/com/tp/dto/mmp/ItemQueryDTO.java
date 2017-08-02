package com.tp.dto.mmp;

import java.io.Serializable;


/*****
 *
 * 商品查询满减信息查询条件
 * @author szy
 *
 *
 */
public class ItemQueryDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455613305240914954L;

	/***专题Id **/
	private Long topicId; 
	
	/** 商品sku***/
	private String sku;
	
	/** 使用平台**/
	private Integer platform;
	
	/**   2-海淘 3-非海淘 */
	private Integer hitao;
	
	/** 销售类型： 使用范围 : 2 - 自营+代销  3 - 代发 **/
	private Integer saleType;
	
	/** 地区**/
	private Integer areaId;
	
	/** 供应商id */
	private Long supplierId;
	
	private Long brandId;
	
	private Long categoryTopId;
	
	private Long categoryMiddleId;
	
	private Long categorySmallId;
	
	
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Integer getHitao() {
		return hitao;
	}
	public void setHitao(Integer hitao) {
		this.hitao = hitao;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getCategoryTopId() {
		return categoryTopId;
	}
	public void setCategoryTopId(Long categoryTopId) {
		this.categoryTopId = categoryTopId;
	}
	public Long getCategoryMiddleId() {
		return categoryMiddleId;
	}
	public void setCategoryMiddleId(Long categoryMiddleId) {
		this.categoryMiddleId = categoryMiddleId;
	}
	public Long getCategorySmallId() {
		return categorySmallId;
	}
	public void setCategorySmallId(Long categorySmallId) {
		this.categorySmallId = categorySmallId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemQueryDTO [topicId=");
		builder.append(topicId);
		builder.append(", sku=");
		builder.append(sku);
		builder.append(", platform=");
		builder.append(platform);
		builder.append(", hitao=");
		builder.append(hitao);
		builder.append(", saleType=");
		builder.append(saleType);
		builder.append(", areaId=");
		builder.append(areaId);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", brandId=");
		builder.append(brandId);
		builder.append(", categoryTopId=");
		builder.append(categoryTopId);
		builder.append(", categoryMiddleId=");
		builder.append(categoryMiddleId);
		builder.append(", categorySmallId=");
		builder.append(categorySmallId);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	
}
