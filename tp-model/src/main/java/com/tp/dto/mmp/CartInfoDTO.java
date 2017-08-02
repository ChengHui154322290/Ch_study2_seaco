package com.tp.dto.mmp;

import java.io.Serializable;

import com.tp.model.mmp.FullDiscount;
import com.tp.model.mmp.FullDiscountRule;

/**
 * 
 * @author szy
 *
 */
public class CartInfoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7596847016342327006L;

	/** 商品信息封装*/
	private ItemQueryDTO  itemQueryDTO;
	
	/** 价格*/
	private Double price;
	
	/** 数量 */
	private Integer quantity;
	
	/** 会员id */
	private Long memberId;
	
	/** 满减相关的信息*/
	private FullDiscount fullDiscount;
	
	/**满减规则*/
	private FullDiscountRule fullDiscountRule;
	
	/** 配置的地址url*/
	private String fullDiscountUrl;

	public ItemQueryDTO getItemQueryDTO() {
		return itemQueryDTO;
	}

	public void setItemQueryDTO(ItemQueryDTO itemQueryDTO) {
		this.itemQueryDTO = itemQueryDTO;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public FullDiscount getFullDiscount() {
		return fullDiscount;
	}

	public void setFullDiscount(FullDiscount fullDiscount) {
		this.fullDiscount = fullDiscount;
	}

	public FullDiscountRule getFullDiscountRule() {
		return fullDiscountRule;
	}

	public void setFullDiscountRule(FullDiscountRule fullDiscountRule) {
		this.fullDiscountRule = fullDiscountRule;
	}

	public String getFullDiscountUrl() {
		return fullDiscountUrl;
	}

	public void setFullDiscountUrl(String fullDiscountUrl) {
		this.fullDiscountUrl = fullDiscountUrl;
	}
}
