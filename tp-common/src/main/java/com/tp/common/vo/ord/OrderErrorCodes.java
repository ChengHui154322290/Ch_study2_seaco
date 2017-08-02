/**
 * Copyright (c) 2016 seagoor All Rights Reserved.
 */
package com.tp.common.vo.ord;

/**
 * 错误编号
 *
 * @author szy
 * @version 0.0.1
 */
public interface OrderErrorCodes {

	/** 系统错误 */
	int SYSTEM_ERROR = 0;
	/** 入参错误 */
	int PARAM_ERROR = 1;
	
	/* ------------------------------------- 购物车错误 （1K） ------------------------------------- */
	/** 购物车行数超过最大限制 */
	int MAX_LINE_QUANTITY_ERROR = 1002;
	/** 单个商品购买数量超过限制 */
	int MAX_SKU_QUANTITY_ERROR = 1003;
	/** 商品已失效，加入购物车失败 */
	int ADDCART_VALIDATE_SKU_ERROR = 1004;
	/** 从商品模块获取商品列表信息为空 */
	int GET_SKU_FROM_ITEM_EMPTY_ERROR = 1005;
	/** 从redis获取商品列表信息为空 */
	int GET_SKU_FROM_REDIS_ERROR = 1006;
	/** 购物车中没有商品被选中 */
	int SELECTED_SKU_EMPTY_ERROR = 1007;
	/** 商品有效库存不足*/
	int QUANTITY_SKU_VALIDATE_ERROR = 1008;
	/** 添加购物车参数错误 */
	int PARAMETER_ADD_CART_ERROR = 1009;
	/** 商品购买数量超过限购数量 */
	int QUANTITY_SKU_RESTRICTION_ERROR = 1010;
	/** 专题不适用当前地区 */
	int AREA_TOPIC_CONSISTENCY__ERROR = 1011;
	/** 专题不适用当前平台 */
	int PLATFORM_TOPIC_CONSISTENCY__ERROR = 1012;
	/** 注册时间不符合限购要求 */
	int REGISTIME_RESTRICTION_ERROR = 1013;
	/** 此用户已达到购买上限 */
	int LIMIT_USER_ERROR = 1014;
	/** 此IP已达到购买上限 */
	int LIMIT_IP_ERROR = 1015;
	/** 找不到专题对应的商品 */
	int TOPIC_SKU_CONSISTENCY_ERROR = 1016;
	/** 获取商品限购信息失败 */
	int GET_TOPIC_INFO_ERROR = 1017;
	/** 专题已经结束 */
	int TOPIC_OVERDUE_ERROR = 1018;
	/** 选择的部分商品失效，无法购买*/
	int CHECKCART_VALIDATE_SKU_ERROR = 1019;
	/** 单个商品购买数量不能小于1*/
	int CHECKCART_VALIDATE_SKU_QUANTITY_ERROR = 1020;
	
	/* ------------------------------------- 订单错误（2K） ------------------------------------- */
	
	/** user ID错误 */
	int CHECK_USERID_INFO_ERROR = 2001;
	/** 购物车信息错误 */
	int CHECK_CART_INFO_ERROR =2002;
	/** 商品信息错误  */
	int CHECK_SKU_INFO_ERROR =2003;
	/** 商品库存不足错误  */
	int CHECK_SKU_STOCK_ERROR =2004;
	/** 收货地址信息错误  */
	int CHECK_ADDRESS_INFO_ERROR =2005;
	/** 减库存调用接口错误 */
	int REDUCE_STOCK_ERROR =2006;
	/** 数据库插入错误 */
	int DATABASE_INSERT_ERROR =2007;
	/** 未付款订单取消错误 */
	int CHECK_CANCEL_ERROR =2008;
	/**子订单不存在*/
	int SUB_ORDER_NOT_EXIST=2009;
	/** 库存回滚调用接口错误 */
	int ROLLBACK_STOCK_ERROR =2010;
	/** 调用促销接口计算总价和运费错误 */
	int CALCULATE_TOTALPRICE_FREIGHT_ERROR =2011;
	/** 调用获取优惠券信息错误 */
	int GET_COUPON_INFO_ERROR =2012;
	/** 调用更新优惠券信息接口错误 */
	int UPDATE_COUPON_INFO_ERROR =2013;
	/** 订单编号不存在 */
	int INVALID_ORDER_CODE =2014;
	/**传入参数信息错误 */
	int VERIFY_ARGUMENT_INFO_ERROR =2015;
	/**调用促销接口计算总价和运费返回商品对应优惠券ID为空 */
	int CALCULATE_TOTALPRICE_RETURN_ERROR=2016;
	/**优惠券回滚错误 */
	int ROLLBACK_COUPON_ERROR=2017;
	/**订单状态不对*/
	int ORDER_STATUS_ERROR=2018;
	
	/**订单状态错误*/
	int CHECK_ORDER_STATUS_ERROR=2018;

	/**订单发货时订单编号已存在*/
	int ORDER_DELIVER_SUB_ORDER_CODE_EXIST=2019;
	/**订单发货时运单号为空*/
	int ORDER_DELIVER_PACKAGE_CODE_IS_NULL=2020;
	/**订单发货时物流公司信息为空*/
	int ORDER_DELIVER_LOGISTICS_COMPANY_INFO_IS_NULL=2021;
	
	/** 调用仓库订单发货出库接口错误 */
	int ORDER_DELIVER_CALL_ERROR =2022;
	/** 订单处理中 */
	int ORDER_CREATTING_ERROR = 2023;
	/** 推送支付返回空*/
	int PUSH_PAYMENT_ERROR = 2024;
	/** 验证送货范围错误*/
	int CHECK_WAREHOUSE_DELIVERY_ERROR= 2025;
	
	/** 获取收货人大区ID错误*/
	int GET_RECEIVER_AREA_ID_ERROR= 2026;

	/** 商品不再销售范围*/
	int ITEM_BEYOND_DELIVERY_ERROR= 2027;
	/** 订单号与会员不匹配*/
	int ORDER_CODE_UNMATCH_MEMBER= 2028;
	/** 订单处于不允许取消的状态 */
	int NOT_ALLOW_CANCELE= 2029;
	/** 该父订单不包含任何子订单 */
	int NO_SUBORDER= 2030;
	/** 该父订单为海淘订单，不允许取消 */
	int SEA_ORDER_CANCEL= 2031;
	/** 海淘实名认证错误 */
	int REAL_VERIFY_ERROR= 2032;
	/** 取消订单错误 */
	int ORDER_CANCEL_ERROR= 2033;
	/** 海淘订单付款后30分钟不允许取消 */
	int SEA_ORDER_CANCEL_ERROR= 2034;
	/** 海淘订单调促销接口计算价钱错误 */
	int SEA_PROMOTION_ERROR= 2035;
	/** 海淘订单调供应商接口获取取海关备案信息错误 */
	int SUPPLIER_CUSTOM_ERROR= 2036;
	/** 海淘订单税费超过50 */
	int TAXFEES_OVER_FIFTY= 2037;
	/** 海淘订单金额超过1000 */
	int TOTAL_OVER_THOUSAND= 2038;	
	
	/* ------------------------------------- 商品错误（3K） ------------------------------------- */
	
	/** 商品已下架 */
	int ITEM_OFF_SALE_ERROR = 3001;
	
	
	/* ------------------------------------- 用户错误（4K） ------------------------------------- */
	
	/** 用户未登录 */
	int USER_UNLOGIN_ERROR = 4001;
	/**收货人信息和用户不匹配*/
	int CONSIGNEE_USER_NOT_MATCH=4002;

	/* ------------------------------------- 促销错误（5K） ------------------------------------- */
	/**用户本次订购可用优惠券查询错误*/
	int PROMOTION_USEFUL_CONPON_ERROR=5001;
	/**用户达到限购*/
	int PROMOTION_USERID_LIMIT_POLICY=5002;
	/**IP达到限购*/
	int PROMOTION_IP_LIMIT_POLICY=5003;
	/**收货人手机号达到限购*/
	int PROMOTION_MOBILE_LIMIT_POLICY=5004;

	int PROMOTION_GROUPBUY_ERROR=5005;
	
	
	/*-------------------------------------- 售后（6K） ------------------------------------------*/
	
	public enum CUSTOMER_SERVICE_ERROR_CODE{
		
		/** 系统异常-6000*/
		SYSTEM_ERROR(6000,"系统异常"),
		/** 用户名为空-6001*/
		USER_NULL(6001,"用户名为空"),
		/** 退货单信息为空-6002*/
		REJECTINFO_NULL(6002,"退货单信息为空"),
		/** 退货商品信息为空-6003*/
		REJECTITEM_NULL(6003,"退货商品信息为空"),
		/**退货商品数量出错-6004*/
		REJEITEM_ITEMREFUNDQUANTITY_ERROR(6004,"退货商品数量出错"),
		/**所退总金额不合理-6005*/
		REJECTINFO_REFUNDAMOUNT_ERROR(6005,"所退总金额不合理"),
		/**退货次数超过三次-6006*/
		REJECTINFO_TIMES(6006,"单件商品退货申请最多申请3次"),
		/**退货金额错误-6007*/
		REJECTITEM_PRICE_ERROR(6007,"退货金额错误"),
		/**订单行不能为空-6008*/
		ORDERLINE_NULL(6008,"订单行为空"),
		/**仓库信息为空-6009*/
		WAREHOUSE_NULL(6009,"仓库信息为空"),
		/**退货ID为空-6010*/
		REJECTID_NULL(6010,"退货信息ID为空"),
		/**订单行ID不能为空-6011*/
		ORDERLINEID_NULL(6011,"订单行id为空"),
		/**操作者更新的不是本人的退货单-6012*/
		REJECTINFO_USER_ERROR(6012,"操作者更新的不是本人的退货单"),
		/**退货单号为空-6013*/
		REJECTNO_NULL(6013,"退货单号为空"),
		/**该物品正在退货中-6014*/
		REPEAT_APPLY_ERROR(6014,"不能重复申请退货"),
		/**退货日志信息为空-6015*/
		REJECTLOG_NULL(6015,"退货日志信息为空"),
		/**页面所传退货单号与数据库退货单号不一致-6016*/
		REJECTNO_ERROR(6016,"页面所传退货单号与数据库退货单号不一致"),
		/**已退款审核,不能再进行退货审核-6017*/
		REPEAT_AUDIT_ERROR(6017,"已退款审核,不能再进行退货审核"),
		/**退货状态发生改变，已不能取消退货-6018*/
		REJECTINFO_REJECTSTATUS_ERROR(6018,"退货状态发生改变，已不能取消退货"),
		/**不符合审核条件-6019*/
		REJECTINFO_AUDIT_ERROR(6019,"不符合审核条件"),
		/**用户id为空-6020*/
		USERID_NULL(6020,"用户id为空"),
		/**子订单subOrder为空-6021*/
		SUBORDER_NULL(6021,"子订单为空"),
		/**下单时间超过30天-6022*/
		ORDER_DONETIME_TIMEOUT(6022,"订单完成时间超过30天"),
		/**没有选中物流公司-6023*/
		COMPANYNAME_NULL(6023,"没有选中物流公司"),
		/**请输入正确的快递单号-6024*/
		EXPRESSNO_NULL(6024,"请输入正确的快递单号");
		
		public Integer code;
		
		public String value;
		
		private CUSTOMER_SERVICE_ERROR_CODE(Integer code, String value) {
			this.code = code;
			this.value = value;
		}


		public static String getValue(Integer code){
			for (CUSTOMER_SERVICE_ERROR_CODE c : CUSTOMER_SERVICE_ERROR_CODE.values()) {
				if(c.code.intValue() == code.intValue()) return c.value;
			}
			return null;
		}
	}
	
	/*-------------------------------------- 支付（7K） ------------------------------------------*/
	public enum PAYMENT_ERROR_CODE{
		INFO_EMPTY(7000,"支付信息为空"),
		STATUS_ERROR(7001,"支付状态不对"),
		ALREADY_PAY(7002,"已支付完成");
		
		public Integer code;
		public String cnName;
		
		PAYMENT_ERROR_CODE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(PAYMENT_ERROR_CODE entry:PAYMENT_ERROR_CODE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}

	/* ------------------------------------- 立即购买错误 （8K） ------------------------------------- */
	/** 没有立即购买商品 */
	int BUYNOW_SKU_EMPTY_ERROR = 8001;
	/** 商品失效，无法购买*/
	int BUYNOW_VALIDATE_SKU_ERROR = 8002;
	
	/*-------------------------------------- 取消订单异常（9K） ------------------------------------------*/
	public enum OrderCancelError {
		ALREADY_DELIVERY(9001, "该订单已发货");

		public Integer code;
		public String cnName;

		OrderCancelError(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			if (code != null) {
				for (OrderCancelError entry : OrderCancelError.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}

	/*-------------------------------------- 查询订单异常（10K） ------------------------------------------*/
	public enum OrderQueryError {
		COUNT_LIMIT(10001, "订单查询记录数超出限制");
		
		public Integer code;
		public String cnName;
		
		OrderQueryError(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			if (code != null) {
				for (OrderQueryError entry : OrderQueryError.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
}
