/**
 * 
 */
package com.tp.dto.wms.sto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *	发货回执
 */
public class STOStockoutBackDto implements Serializable{

	private static final long serialVersionUID = 1564866974209783648L;
	/** 订单列表 */
	private List<STOStockoutBackItemDto> orderList;

	public List<STOStockoutBackItemDto> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<STOStockoutBackItemDto> orderList) {
		this.orderList = orderList;
	}
}
