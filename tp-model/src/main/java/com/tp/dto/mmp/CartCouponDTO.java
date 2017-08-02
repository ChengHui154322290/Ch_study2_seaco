package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.SeaOrderItemDTO;

public class CartCouponDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1468315160219686201L;

	private CartDTO cartDTO;
	
	private Map<Long, List<String>> cuidSkuMap;
	
	/**关联到的优惠券的详细信息*/
	private List<OrderCouponDTO> couponDtoList;
	
	/**
	 * 海淘的购物车信息
	 */
	private SeaOrderItemDTO seaOrderItemDTO;
	
	
	
	public SeaOrderItemDTO getSeaOrderItemDTO() {
		return seaOrderItemDTO;
	}

	public void setSeaOrderItemDTO(SeaOrderItemDTO seaOrderItemDTO) {
		this.seaOrderItemDTO = seaOrderItemDTO;
	}

	public List<OrderCouponDTO> getCouponDtoList() {
		return couponDtoList;
	}

	public void setCouponDtoList(List<OrderCouponDTO> couponDtoList) {
		this.couponDtoList = couponDtoList;
	}

	public CartDTO getCartDTO() {
		return cartDTO;
	}

	public void setCartDTO(CartDTO cartDTO) {
		this.cartDTO = cartDTO;
	}

	public Map<Long, List<String>> getCuidSkuMap() {
		return cuidSkuMap;
	}

	public void setCuidSkuMap(Map<Long, List<String>> cuidSkuMap) {
		this.cuidSkuMap = cuidSkuMap;
	} 
	
	
	
	
	
}
