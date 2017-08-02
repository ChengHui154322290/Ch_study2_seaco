package com.tp.dto.ord;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;

/**
 * <pre>
 * 订单插入数据库相关信息DTO
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrderInsertInfoDTO implements Serializable {
	private static final long serialVersionUID = 8111799305189138488L;

	// 父订单DTO
	OrderInfo orderInfo;
	// 子订单和订单行map
	Map<SubOrder, List<OrderItem>> subOrderMapOrderItem;
	// 定义订单行和促销优惠对应map
	Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion;
	// 收货地址DTO
	OrderConsignee orderConsignee;
	// 发票信息DTO
	OrderReceipt orderReceipt;
	// 海淘实名认证信息DTO
	MemRealinfo memRealinfo;
	// 限购信息
	List<TopicLimitItem> topicLimitItems;
	//团Id
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public Map<SubOrder, List<OrderItem>> getSubOrderMapOrderItem() {
		return subOrderMapOrderItem;
	}

	public void setSubOrderMapOrderItem(
			Map<SubOrder, List<OrderItem>> subOrderMapOrderItem) {
		this.subOrderMapOrderItem = subOrderMapOrderItem;
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

	public Map<OrderItem, List<OrderPromotion>> getOrderItemMapPromotion() {
		return orderLineMapPromotion;
	}

	public void setOrderItemMapPromotion(
			Map<OrderItem, List<OrderPromotion>> orderLineMapPromotion) {
		this.orderLineMapPromotion = orderLineMapPromotion;
	}

	public MemRealinfo getMemRealinfo() {
		return memRealinfo;
	}

	public void setMemRealinfo(MemRealinfo memRealinfo) {
		this.memRealinfo = memRealinfo;
	}

	public List<TopicLimitItem> getTopicLimitItems() {
		return topicLimitItems;
	}

	public void setTopicLimitItems(List<TopicLimitItem> topicLimitItems) {
		this.topicLimitItems = topicLimitItems;
	}

}
