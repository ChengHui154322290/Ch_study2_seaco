package com.tp.dto.prd.mq;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <pre>
 * 	临时同步到促销系统的mq消息体
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class PromotionItemMqDto implements Serializable{
	
	private static final long serialVersionUID = 8452482896832992L;

	/**sku编码**/
	private String skuCode;
	
	/**主图**/
	private String mainPic;
	
	/**商品前台展示名称**/
	private String mainTitle;
	
	/** 市场价 */
	private Double basicPrice;
	
	/** 适用年龄**/
	private Long applyAgeId;
	
	/***sku上下架状态 @see ItemStatusEnum **/
	private Integer status;
	
	/**更新sku信息时间**/
	private Date updateSkuDate;
    
	/** 小类id */
	private Long smallCateId;
	
	/** 中类id */
	private Long middleCateId;
	
	/** 大类id */
	private Long largeCateId;
    
	/** 品牌id */
	private Long brandId;
	/**
	 * Getter method for property <tt>skuCode</tt>.
	 * 
	 * @return property value of skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * Setter method for property <tt>skuCode</tt>.
	 * 
	 * @param skuCode value to be assigned to property skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * Getter method for property <tt>mainPic</tt>.
	 * 
	 * @return property value of mainPic
	 */
	public String getMainPic() {
		return mainPic;
	}

	/**
	 * Setter method for property <tt>mainPic</tt>.
	 * 
	 * @param mainPic value to be assigned to property mainPic
	 */
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}

	/**
	 * Getter method for property <tt>mainTitle</tt>.
	 * 
	 * @return property value of mainTitle
	 */
	public String getMainTitle() {
		return mainTitle;
	}

	/**
	 * Setter method for property <tt>mainTitle</tt>.
	 * 
	 * @param mainTitle value to be assigned to property mainTitle
	 */
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	
	public Long getApplyAgeId() {
		return applyAgeId;
	}

	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	
	public Date getUpdateSkuDate() {
		return updateSkuDate;
	}

	public void setUpdateSkuDate(Date updateSkuDate) {
		this.updateSkuDate = updateSkuDate;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getSmallCateId() {
		return smallCateId;
	}

	public void setSmallCateId(Long smallCateId) {
		this.smallCateId = smallCateId;
	}

	public Long getMiddleCateId() {
		return middleCateId;
	}

	public void setMiddleCateId(Long middleCateId) {
		this.middleCateId = middleCateId;
	}

	public Long getLargeCateId() {
		return largeCateId;
	}

	public void setLargeCateId(Long largeCateId) {
		this.largeCateId = largeCateId;
	}

	@Override
	public String toString() {
		return "PromotionItemMqDto [skuCode=" + skuCode + ", mainPic="
				+ mainPic + ", mainTitle=" + mainTitle + ", basicPrice="
				+ basicPrice + ", applyAgeId=" + applyAgeId + ", status="
				+ status + ", updateSkuDate=" + updateSkuDate
				+ ", smallCateId=" + smallCateId + ", middleCateId="
				+ middleCateId + ", largeCateId=" + largeCateId + ", brandId="
				+ brandId + "]";
	}
}
