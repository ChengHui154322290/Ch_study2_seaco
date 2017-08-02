/**
 * 
 */
package com.tp.model.wms.jdz;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class JdzStockoutCancelOrderRequest implements Serializable{
	private static final long serialVersionUID = 6373441576509073851L;

	/** 配货单号 */
	private String orderNo;
	
	/** 货主名 */
	private String goodsOwner;
	
	/** 备注 */
	private String remark;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsOwner() {
		return goodsOwner;
	}

	public void setGoodsOwner(String goodsOwner) {
		this.goodsOwner = goodsOwner;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
