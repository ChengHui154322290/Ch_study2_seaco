package com.tp.dto.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

public class PromotionDto extends BaseDO implements Serializable {


	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -4482016894589710439L;
	

	private Long topicId;
	
	private String skuCode;
	
	/** 上线时间 **/
	private Date onlineDate;
	
	/** 下线时间 **/
	private Date offlineDate;
	
	/** 促销活动名称 **/
	private String promotionName;
	
	/** 促销活动是否已过期 **/
	private boolean isExpiry;
	
	/** 促销活动图片url **/
	private String imgUrl;
	
	private Long attentionId;
	
	/**
	 * 活动状态 0 正常 1未开始2是结束
	 */
	private Integer expiryStatus = 0;

	public Date getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	public Date getOfflineDate() {
		return offlineDate;
	}

	public void setOfflineDate(Date offlineDate) {
		this.offlineDate = offlineDate;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public boolean isExpiry() {
		return isExpiry;
	}

	public void setExpiry(boolean isExpiry) {
		this.isExpiry = isExpiry;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getAttentionId() {
		return attentionId;
	}

	public void setAttentionId(Long attentionId) {
		this.attentionId = attentionId;
	}

	public Integer getExpiryStatus() {
		return expiryStatus;
	}

	public void setExpiryStatus(Integer expiryStatus) {
		this.expiryStatus = expiryStatus;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
}
