package com.tp.enums.common;

import com.tp.constant.common.ExceptionCode;

public interface OrderErrorCodeEnum{
	
	Integer ERROR_PREFIX = ExceptionCode.ORDER;
	/** 系统错误 */
	int SYSTEM_ERROR = 0;
	/** 入参错误 */
	int PARAM_ERROR = 1;
	public enum CART_ERROR implements XGCodeEnum{
		/** 购物车行数超过最大限制 */
		MAX_LINE_QUANTITY_ERROR(1002,"购物车行数超过最大限制"),/** 单个商品购买数量超过限制 */
		MAX_SKU_QUANTITY_ERROR(1003,"单个商品购买数量超过限制"),/** 商品已失效，加入购物车失败 */
		ADDCART_VALIDATE_SKU_ERROR(1004,"商品已失效，加入购物车失败"), /** 从商品模块获取商品列表信息为空 */
		GET_SKU_FROM_ITEM_EMPTY_ERROR(1005,"从商品模块获取商品列表信息为空"),/** 从redis获取商品列表信息为空 */
		GET_SKU_FROM_REDIS_ERROR(1006,"从redis获取商品列表信息为空"),/** 购物车中没有商品被选中 */
		SELECTED_SKU_EMPTY_ERROR(1007,"购物车中没有商品被选中"),/** 商品有效库存不足*/
		QUANTITY_SKU_VALIDATE_ERROR(1008,"商品有效库存不足"),/** 添加购物车参数错误 */
		PARAMETER_ADD_CART_ERROR(1009,"添加购物车参数错误"),/** 商品购买数量超过限购数量 */
		QUANTITY_SKU_RESTRICTION_ERROR(1010,"商品购买数量超过限购数量"),/** 专题不适用当前地区 */
		AREA_TOPIC_CONSISTENCY_ERROR(1011,"专题不适用当前地区"),/** 专题不适用当前平台 */
		PLATFORM_TOPIC_CONSISTENCY_ERROR(1012,"专题不适用当前平台"),/** 注册时间不符合限购要求 */
		REGISTIME_RESTRICTION_ERROR(1013,"注册时间不符合限购要求"),/** 此用户已达到购买上限 */
		LIMIT_USER_ERROR(1014,"此用户已达到购买上限"),/** 此IP已达到购买上限 */
		LIMIT_IP_ERROR(1015,"此IP已达到购买上限"),/** 找不到专题对应的商品 */
		TOPIC_SKU_CONSISTENCY_ERROR(1016,"找不到专题对应的商品"),/** 获取商品限购信息失败 */
		GET_TOPIC_INFO_ERROR(1017,"获取商品限购信息失败"),/** 专题已经结束 */
		TOPIC_OVERDUE_ERROR(1018,"专题已经结束"),/** 选择的部分商品失效，无法购买*/
		CHECKCART_VALIDATE_SKU_ERROR(1019,"选择的部分商品失效，无法购买"),/** 单个商品购买数量不能小于1*/
		CHECKCART_VALIDATE_SKU_QUANTITY_ERROR(1020,"单个商品购买数量不能小于1"),/**其它平台正在操作*/
		OTHER_PLATFORM_OPERATION(1021,"你正在其它手机或电脑上操作，请稍后"),
		/**user,ID错误*/
		CHECK_USERID_INFO_ERROR(2001,"user,ID错误*/"),/**购物车信息错误*/
		CHECK_CART_INFO_ERROR(2002,"购物车信息错误"),/**商品信息错误*/
		CHECK_SKU_INFO_ERROR(2003,"商品信息错误"),/**商品库存不足错误*/
		CHECK_SKU_STOCK_ERROR(2004,"商品库存不足错误"),/**收货地址信息错误*/
		CHECK_ADDRESS_INFO_ERROR(2005,"收货地址信息错误"),/**减库存调用接口错误*/
		REDUCE_STOCK_ERROR(2006,"减库存调用接口错误"),/**数据库插入错误*/
		DATABASE_INSERT_ERROR(2007,"数据库插入错误"),/**未付款订单取消错误*/
		CHECK_CANCEL_ERROR(2008,"未付款订单取消错误"),/**子订单不存在*/
		SUB_ORDER_NOT_EXIST(2009,"子订单不存在"),/**库存回滚调用接口错误*/
		ROLLBACK_STOCK_ERROR(2010,"库存回滚调用接口错误"),/**调用促销接口计算总价和运费错误*/
		CALCULATE_TOTALPRICE_FREIGHT_ERROR(2011,"调用促销接口计算总价和运费错误"),/**调用获取优惠券信息错误*/
		GET_COUPON_INFO_ERROR(2012,"调用获取优惠券信息错误"),/**调用更新优惠券信息接口错误*/
		UPDATE_COUPON_INFO_ERROR(2013,"调用更新优惠券信息接口错误"),/**订单编号不存在*/
		INVALID_ORDER_CODE(2014,"订单编号不存在"),/**传入参数信息错误*/
		VERIFY_ARGUMENT_INFO_ERROR(2015,"传入参数信息错误"),/**调用促销接口计算总价和运费返回商品对应优惠券ID为空*/
		CALCULATE_TOTALPRICE_RETURN_ERROR(2016,"调用促销接口计算总价和运费返回商品对应优惠券ID为空"),/**优惠券回滚错误*/
		ROLLBACK_COUPON_ERROR(2017,"优惠券回滚错误"),/**订单状态不对*/
		ORDER_STATUS_ERROR(2018,"订单状态不对"),/**订单状态错误*/
		CHECK_ORDER_STATUS_ERROR(2018,"订单状态错误"),/**订单发货时订单编号已存在*/
		ORDER_DELIVER_SUB_ORDER_CODE_EXIST(2019,"订单发货时订单编号已存在"),/**订单发货时运单号为空*/
		ORDER_DELIVER_PACKAGE_CODE_IS_NULL(2020,"订单发货时运单号为空"),/**订单发货时物流公司信息为空*/
		ORDER_DELIVER_LOGISTICS_COMPANY_INFO_IS_NULL(2021,"订单发货时物流公司信息为空"),/**调用仓库订单发货出库接口错误*/
		ORDER_DELIVER_CALL_ERROR(2022,"调用仓库订单发货出库接口错误"),/**订单处理中*/
		ORDER_CREATTING_ERROR(2023,"订单处理中"),/**推送支付返回空*/
		PUSH_PAYMENT_ERROR(2024,"推送支付返回空"),/**验证送货范围错误*/
		CHECK_WAREHOUSE_DELIVERY_ERROR(2025,"验证送货范围错误"),/**获取收货人大区ID错误*/
		GET_RECEIVER_AREA_ID_ERROR(2026,"获取收货人大区ID错误"),/**商品不再销售范围*/
		ITEM_BEYOND_DELIVERY_ERROR(2027,"商品不再销售范围"),/**订单号与会员不匹配*/
		ORDER_CODE_UNMATCH_MEMBER(2028,"订单号与会员不匹配"),/**订单处于不允许取消的状态*/
		NOT_ALLOW_CANCELE(2029,"订单处于不允许取消的状态"),/**该父订单不包含任何子订单*/
		NO_SUBORDER(2030,"该父订单不包含任何子订单"),/**该父订单为海淘订单，不允许取消*/
		SEA_ORDER_CANCEL(2031,"该父订单为海淘订单，不允许取消"),/**海淘实名认证错误*/
		REAL_VERIFY_ERROR(2032,"海淘实名认证错误"),/**取消订单错误*/
		ORDER_CANCEL_ERROR(2033,"取消订单错误"),/**海淘订单付款后30分钟不允许取消*/
		SEA_ORDER_CANCEL_ERROR(2034,"海淘订单付款后30分钟不允许取消"),/**海淘订单调促销接口计算价钱错误*/
		SEA_PROMOTION_ERROR(2035,"海淘订单调促销接口计算价钱错误"),/**海淘订单调供应商接口获取取海关备案信息错误*/
		SUPPLIER_CUSTOM_ERROR(2036,"海淘订单调供应商接口获取取海关备案信息错误"),/**海淘订单税费超过50*/
		TAXFEES_OVER_FIFTY(2037,"海淘订单税费超过50"),/**海淘订单金额超过1000*/
		TOTAL_OVER_THOUSAND(2038,"海淘订单金额超过1000"),/**商品已下架*/
		ITEM_OFF_SALE_ERROR(3001,"商品已下架"),/**用户未登录*/
		USER_UNLOGIN_ERROR(4001,"用户未登录"),/**收货人信息和用户不匹配*/
		CONSIGNEE_USER_NOT_MATCH(4002,"收货人信息和用户不匹配"),/**用户本次订购可用优惠券查询错误*/
		PROMOTION_USEFUL_CONPON_ERROR(5001,"用户本次订购可用优惠券查询错误"),/**用户达到限购*/
		PROMOTION_USERID_LIMIT_POLICY(5002,"用户达到限购"),/**IP达到限购*/
		PROMOTION_IP_LIMIT_POLICY(5003,"IP达到限购"),/**收货人手机号达到限购*/
		PROMOTION_MOBILE_LIMIT_POLICY(5004,"收货人手机号达到限购"),/** 没有立即购买商品 */
		BUYNOW_SKU_EMPTY_ERROR(8001,"没有立即购买商品 "),/** 商品失效，无法购买*/
		BUYNOW_VALIDATE_SKU_ERROR(8002,"商品失效，无法购买"),/**该订单已发货*/
		ALREADY_DELIVERY(9001, "该订单已发货"),/**订单查询记录数超出限制*/
		COUNT_LIMIT(10001, "订单查询记录数超出限制");
		public Integer code;
		public String message;
		
		private CART_ERROR(Integer code,String message){
			this.code = code;
			this.message = message;
		}
		
		@Override
		public Integer getCode() {
			return code;
		}

		@Override
		public String getMessage() {
			return message;
		}
		
	}
	
	/*-------------------------------------- 售后（6K） ------------------------------------------*/
	
	public enum CUSTOMER_SERVICE_ERROR_CODE implements XGCodeEnum{
		
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
		
		public String message;
		
		private CUSTOMER_SERVICE_ERROR_CODE(Integer code, String message) {
			this.code = code;
			this.message = message;
		}


		public static String getValue(Integer code){
			for (CUSTOMER_SERVICE_ERROR_CODE c : CUSTOMER_SERVICE_ERROR_CODE.values()) {
				if(c.code.intValue() == code.intValue()) return c.message;
			}
			return null;
		}
		
		@Override
		public Integer getCode() {
			return code;
		}

		@Override
		public String getMessage() {
			return message;
		} 
	}
	
	/*-------------------------------------- 支付（7K） ------------------------------------------*/
	public enum PAYMENT_ERROR_CODE implements XGCodeEnum{
		INFO_EMPTY(7000,"支付信息为空"),
		STATUS_ERROR(7001,"支付状态不对"),
		ALREADY_PAY(7002,"已支付完成");
		
		public Integer code;
		public String message;
		
		PAYMENT_ERROR_CODE(Integer code,String message){
			this.code = code;
			this.message = message;
		}
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(PAYMENT_ERROR_CODE entry:PAYMENT_ERROR_CODE.values()){
					if(entry.code.intValue()==code){
						return entry.message;
					}
				}
			}
			return null;
		}
		@Override
		public Integer getCode() {
			return code;
		}

		@Override
		public String getMessage() {
			return message;
		} 
	}
}
