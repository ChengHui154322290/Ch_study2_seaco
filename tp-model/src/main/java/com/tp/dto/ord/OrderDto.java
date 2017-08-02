package com.tp.dto.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;

public class OrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3784432436212694114L;

	private OrderInfo orderInfo;
	private List<SubOrder> subOrderList = new ArrayList<SubOrder>();
	private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
	private OrderConsignee orderConsignee = new OrderConsignee();
	private OrderReceipt orderReceipt = new OrderReceipt();
	private MemRealinfo memRealinfo;
	private List<OrderPromotion> orderPromotionList = new ArrayList<OrderPromotion>();
	private List<TopicLimitItem> topicLimitItemList = new ArrayList<TopicLimitItem>();
	private List<OrderStatusLog> orderStatusLogList = new ArrayList<OrderStatusLog>();
	private List<PayPaymentSimpleDTO> paymentInfoList = new ArrayList<PayPaymentSimpleDTO>();
	private OrderChannelTrack orderChannelTrack;
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public List<SubOrder> getSubOrderList() {
		return subOrderList;
	}
	public void setSubOrderList(List<SubOrder> subOrderList) {
		this.subOrderList = subOrderList;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public OrderConsignee getOrderConsignee() {
		return orderConsignee;
	}
	public void setOrderConsignee(OrderConsignee orderConsignee) {
		this.orderConsignee = orderConsignee;
	}
	public OrderReceipt getOrderReceipt() {
		return orderReceipt;
	}
	public void setOrderReceipt(OrderReceipt orderReceipt) {
		this.orderReceipt = orderReceipt;
	}
	public List<OrderPromotion> getOrderPromotionList() {
		return orderPromotionList;
	}
	public void setOrderPromotionList(List<OrderPromotion> orderPromotionList) {
		this.orderPromotionList = orderPromotionList;
	}
	public List<TopicLimitItem> getTopicLimitItemList() {
		return topicLimitItemList;
	}
	public void setTopicLimitItemList(List<TopicLimitItem> topicLimitItemList) {
		this.topicLimitItemList = topicLimitItemList;
	}
	public List<OrderStatusLog> getOrderStatusLogList() {
		return orderStatusLogList;
	}
	public void setOrderStatusLogList(List<OrderStatusLog> orderStatusLogList) {
		this.orderStatusLogList = orderStatusLogList;
	}
	public List<PayPaymentSimpleDTO> getPaymentInfoList() {
		return paymentInfoList;
	}
	public void setPaymentInfoList(List<PayPaymentSimpleDTO> paymentInfoList) {
		this.paymentInfoList = paymentInfoList;
	}
	public MemRealinfo getMemRealinfo() {
		return memRealinfo;
	}
	public void setMemRealinfo(MemRealinfo memRealinfo) {
		this.memRealinfo = memRealinfo;
	}
	public OrderChannelTrack getOrderChannelTrack() {
		return orderChannelTrack;
	}
	public void setOrderChannelTrack(OrderChannelTrack orderChannelTrack) {
		this.orderChannelTrack = orderChannelTrack;
	}
	
}
