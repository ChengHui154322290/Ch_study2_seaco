/**
 * 
 */
package com.tp.dto.ord;

import java.util.Date;

import com.tp.common.vo.ord.CartConstant;

/**
 * 购物车行DTO（用于缓存传输）
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class CartLineSimpleDTO implements BaseDTO {

	private static final long serialVersionUID = -6436223080230518751L;

	/** sku code */
	private String skuCode;
	/** topic id */
	private Long topicId;
	/** area id */
	private Long areaId;
	/** platform id */
	private Integer platformId;
	/** 类型：1.普通 2.海淘 */
	private Integer type = CartConstant.TYPE_COMMON;
	/** 数量 */
	private Integer quantity = 0;
	/** 会员id */
	private Long memberId;
	/** 加入购物车时间 */
	private Date createTime = new Date();
	/** 是否选中 */
	private Boolean selected = true;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skuCode == null) ? 0 : skuCode.hashCode());
		result = prime * result + ((topicId == null) ? 0 : topicId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartLineSimpleDTO other = (CartLineSimpleDTO) obj;
		if (skuCode == null) {
			if (other.skuCode != null)
				return false;
		} else if (!skuCode.equals(other.skuCode))
			return false;
		if (topicId == null) {
			if (other.topicId != null)
				return false;
		} else if (!topicId.equals(other.topicId))
			return false;
		return true;
	}
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
