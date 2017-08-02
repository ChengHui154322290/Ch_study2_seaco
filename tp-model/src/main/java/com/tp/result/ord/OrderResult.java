package com.tp.result.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.ReceiptDetail;
import com.tp.model.ord.SubOrder;

/**
 * 列表订单DTO（用户）
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderResult implements Serializable {
	
	private static final long serialVersionUID = 8538285237021129050L;
	
	/** 父订单 */
	private OrderInfo orderInfo;
	/** 子订单 */
	private SubOrder subOrder;
	/** 订单行列表 */
	private List<OrderItem> OrderItemList=new ArrayList<OrderItem>();
	/** 订单发票 */
	private OrderReceipt receipt;
	/** 收货人 */
	private OrderConsignee consignee;
	/** 物流信息 */
	private OrderDelivery delivery;
	/** 会员实名 */
	private MemRealinfo memRealinfo;
	/** 发票明细 */
	private ReceiptDetail receiptDetail;
	
	private List<OrderPromotion> orderPromotionList = new ArrayList<OrderPromotion>();
	
	private List<OrderStatusLog> orderStatusLogList = new ArrayList<OrderStatusLog>();
 	/** 会员登录名 */
	private String loginName;
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
		return OrderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		OrderItemList = orderItemList;
	}
	public OrderReceipt getReceipt() {
		return receipt;
	}
	public void setReceipt(OrderReceipt receipt) {
		this.receipt = receipt;
	}
	public OrderConsignee getConsignee() {
		return consignee;
	}
	public void setConsignee(OrderConsignee consignee) {
		this.consignee = consignee;
	}
	public OrderDelivery getDelivery() {
		return delivery;
	}
	public void setDelivery(OrderDelivery delivery) {
		this.delivery = delivery;
	}
	public MemRealinfo getMemRealinfo() {
		return memRealinfo;
	}
	public void setMemRealinfo(MemRealinfo memRealinfo) {
		this.memRealinfo = memRealinfo;
	}
	public ReceiptDetail getReceiptDetail() {
		return receiptDetail;
	}
	public void setReceiptDetail(ReceiptDetail receiptDetail) {
		this.receiptDetail = receiptDetail;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public List<OrderPromotion> getOrderPromotionList() {
		return orderPromotionList;
	}
	public void setOrderPromotionList(List<OrderPromotion> orderPromotionList) {
		this.orderPromotionList = orderPromotionList;
	}
	public List<OrderStatusLog> getOrderStatusLogList() {
		return orderStatusLogList;
	}
	public void setOrderStatusLogList(List<OrderStatusLog> orderStatusLogList) {
		this.orderStatusLogList = orderStatusLogList;
	}
	
}
