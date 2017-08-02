package com.tp.query.mmp;

import java.io.Serializable;
import java.util.List;

import com.tp.dto.mmp.OrderLineItemDTO;

public class UsableCouponQueryDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6068035391454332561L;

	/** 商品行list**/
		
	private List<OrderLineItemDTO> orderLineList;
	private Long userId;
	
	/** 平台*/
	private Integer platformType;
	
	/** 商品类型，1 非海淘   2海淘 */
	private Integer isHitao;
	
	/** 团Id**/
	private Long groupId;
	
	
	public List<OrderLineItemDTO> getOrderLineList() {
		return orderLineList;
	}

	public void setOrderLineList(List<OrderLineItemDTO> orderLineList) {
		this.orderLineList = orderLineList;
	}

	public Integer getIsHitao() {
		return isHitao;
	}

	public void setIsHitao(Integer isHitao) {
		this.isHitao = isHitao;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}



	
	
}
