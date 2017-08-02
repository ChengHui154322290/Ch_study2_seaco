package com.tp.dto.ord.remote;

import java.io.Serializable;
import java.util.List;

import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.ReceiptDetail;
import com.tp.model.ord.SubOrder;

/**
 * 列表订单DTO（用户）
 * 
 * @author szy
 * @version 0.0.1
 */
public class SubOrder4BackendDTO implements Serializable {
	
	private static final long serialVersionUID = 8538285237021129050L;
	
	/** 父订单 */
	private OrderInfo order;
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
	/** 会员实名 */
	private MemRealinfo memRealinfo;
	/** 发票明细 */
	private ReceiptDetail receiptDetail;
	/** 会员登录名 */
	private String loginName;
	/**卡券推广员*/
	private String promoterName;
	/**店铺名称*/
	private String shopPromoterName;
	/**线下推广*/
	private String scanPromoterName;
	
	public OrderInfo getOrder() {
		return order;
	}
	public void setOrder(OrderInfo order) {
		this.order = order;
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
	public String getPromoterName() {
		return promoterName;
	}
	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}
	public String getShopPromoterName() {
		return shopPromoterName;
	}
	public void setShopPromoterName(String shopPromoterName) {
		this.shopPromoterName = shopPromoterName;
	}
	public String getScanPromoterName() {
		return scanPromoterName;
	}
	public void setScanPromoterName(String scanPromoterName) {
		this.scanPromoterName = scanPromoterName;
	}
}
