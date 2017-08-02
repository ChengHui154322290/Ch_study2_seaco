/**
 * 
 */
package com.tp.dto.ord;




/**
 * 优惠券信息DTO
 * yanji
 * @version 0.0.1
 * 
 */
public class CouponInfoDTO  implements BaseDTO {

	private static final long serialVersionUID = -5128690575018852258L;


	/** 优惠券类型 ：1,全场券 2,只能自营代销使用 3，只能第三方卖家使用券 */
	private Integer couponType;
	/** 消费金额要求 **/
	private Double consumeRequire;
	/** 优惠金额 */
	private Double discount ;
/*	*//** 优惠商家ID list *//*
	private List<Long> suppliersList;
	*//** 优惠商品ID list *//*
	private List<Long> itemsList;*/
	public Integer getCouponType() {
		return couponType;
	}
	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}
	public Double getConsumeRequire() {
		return consumeRequire;
	}
	public void setConsumeRequire(Double consumeRequire) {
		this.consumeRequire = consumeRequire;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
