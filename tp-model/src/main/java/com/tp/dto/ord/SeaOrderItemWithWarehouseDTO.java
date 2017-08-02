/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.util.List;

/**
 * <pre>
 * 海淘订单商品，根据仓库拆分
 * </pre>
 * 
 * @author szy
 * @time 2015-3-20 下午5:08:16
 */
public class SeaOrderItemWithWarehouseDTO implements BaseDTO {
	private static final long serialVersionUID = -2645511775715446304L;

	/** 仓库ID */
	private Long warehouseId;

	/** 仓库名称 */
	private String warehouseName;

	/** 仓库类型*/
	private Integer storageType;
	
	/** 海淘渠道 */
	private Long seaChannel;
	/** 海淘渠道编码 */
	private String seaChannelCode;
	/** 海淘渠道名称 */
	private String seaChannelName;

	/** 商品行（订单行）信息 */
	private List<CartLineDTO> cartLineList;

	/** 购买的商品总数量 */
	private Integer itemTotalQuantity;

	/** 购买的商品总金额 */
	private Double totalPrice;

	/** 商品总优惠金额 */
	private Double totalDiscount;

	/** 购买的商品总税费 */
	private Double totalTaxfee;
	
	/** 真实使用的税费，该税费会计进总价中 */
	private Double actualUsedTaxfee;

	/** 购买的商品总运费 */
	private Double totalFreight;

	/** 购买的商品应付总金额（总金额-优惠金额+税费+运费） */
	private Double totalPayPrice;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public List<CartLineDTO> getCartLineList() {
		return cartLineList;
	}

	public void setCartLineList(List<CartLineDTO> cartLineList) {
		this.cartLineList = cartLineList;
	}

	public Integer getItemTotalQuantity() {
		return itemTotalQuantity;
	}

	public void setItemTotalQuantity(Integer itemTotalQuantity) {
		this.itemTotalQuantity = itemTotalQuantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTaxfee() {
		return totalTaxfee;
	}

	public void setTotalTaxfee(Double totalTaxfee) {
		this.totalTaxfee = totalTaxfee;
	}

	public Double getTotalFreight() {
		return totalFreight;
	}

	public void setTotalFreight(Double totalFreight) {
		this.totalFreight = totalFreight;
	}

	public Double getTotalPayPrice() {
		return totalPayPrice;
	}

	public void setTotalPayPrice(Double totalPayPrice) {
		this.totalPayPrice = totalPayPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getSeaChannel() {
		return seaChannel;
	}

	public void setSeaChannel(Long seaChannel) {
		this.seaChannel = seaChannel;
	}

	public String getSeaChannelName() {
		return seaChannelName;
	}

	public void setSeaChannelName(String seaChannelName) {
		this.seaChannelName = seaChannelName;
	}
	
	public String getSeaChannelCode() {
		return seaChannelCode;
	}

	public void setSeaChannelCode(String seaChannelCode) {
		this.seaChannelCode = seaChannelCode;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public Double getActualUsedTaxfee() {
		return actualUsedTaxfee;
	}

	public void setActualUsedTaxfee(Double actualUsedTaxfee) {
		this.actualUsedTaxfee = actualUsedTaxfee;
	}

}
