package com.tp.dto.ord.remote;

import java.io.Serializable;
import java.util.List;

import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.SubOrder;

/**
 * 列表订单DTO（用户）
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderDetails4UserDTO implements Serializable {
	
	private static final long serialVersionUID = 8538285237021129050L;
	
	
	/** 是否以父订单无主导 */
	private Boolean isParent;
	/** 父订单 */
	private OrderInfo orderInfo;
	/** 子订单 */
	private SubOrder subOrder;
	/** 订单行列表 */
	private List<OrderItem> orderItemList;
	/** 订单发票 */
	private OrderReceipt orderReceipt;
	/** 收货人 */
	private OrderConsignee orderConsignee;
	/** 物流信息 */
	private OrderDelivery orderDelivery;
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public SubOrder getSubOrder() {
		return subOrder;
	}
	public void setSubOrder(SubOrder subOrder) {
		this.subOrder = subOrder;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public OrderReceipt getOrderReceipt() {
		return orderReceipt;
	}
	public void setOrderReceipt(OrderReceipt orderReceipt) {
		this.orderReceipt = orderReceipt;
	}
	public OrderConsignee getOrderConsignee() {
		return orderConsignee;
	}
	public void setOrderConsignee(OrderConsignee orderConsignee) {
		this.orderConsignee = orderConsignee;
	}
	public OrderDelivery getOrderDelivery() {
		return orderDelivery;
	}
	public void setOrderDelivery(OrderDelivery orderDelivery) {
		this.orderDelivery = orderDelivery;
	}
}
