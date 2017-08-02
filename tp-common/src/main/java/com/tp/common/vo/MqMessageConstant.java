package com.tp.common.vo;

/**
 * 消息常量
 * @author szy
 *
 */
public interface MqMessageConstant {
	
	/**会员注册成功*/
	public static final String REGISTER_PROMOTER_SUCCESS = "register_promoter_success";
	
	/** 订单发货成功 */
	public static final String ORDER_DELIVERY_SUCCESS = "order_delivery_success";
	
	/**退款成功*/
	public static final String REFUND_SUCCESS = "refund_success";
	
	/**收货成功*/
	public static final String RECEIVE_GOODS_SUCCESS = "receive_goods_success";
	
	/**民生银行注册会员*/
	public static final String CMBC_NEW_REGISTER = "cmbc_new_register";
	
	/**修改促销价格*/
	public static final String ITEM_SKU_MODIFY_TOPIC_PRICE = "item_sku_modify_topic_price";

}
