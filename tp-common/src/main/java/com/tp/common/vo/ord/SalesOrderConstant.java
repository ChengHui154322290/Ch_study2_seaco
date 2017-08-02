package com.tp.common.vo.ord;

/**
 * 订单 常量
 * 
 * @author szy
 */
public interface SalesOrderConstant {

    final static String CALC_PRICE_TYPE_DEFAULT = "0";
    final static String CALC_PRICE_TYPE_PROMOTION = "1";

    /** 销售模式：1-买断，2-代销，3-平台,4-海淘, 默认1 */
    final static int SALE_TYPE_OWN = 0;// 买断
    final static int SALE_TYPE_PLATFORM =1;// 平台
    final static int SALE_TYPE_SEA = 2;// 海淘

    
    /** 逻辑删除 - 是 */
    Integer DELETED_TRUE = 1;
    /** 逻辑删除 - 否 */
    Integer DELETED_FALSE = 2;

	
	/**
	 * 订单支付途径
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	@Deprecated
	public enum OrderPayWay implements BaseEnum {
		
		/** 1 - 支付宝支付 */
		ALIPAY(1, "支付宝支付"),
		/** 2 - 银联支付 */
		UNIONPAY(2, "银联支付"),
		/** 3 - 快钱 */
		BILL99(3, "快钱"),
		/** 4 - 微信支付 */
		WEIXIN(4, "微信支付"),
		/** 5 - 招商银行 */
		CMB(5, "招商银行"),
		/** 4 - 微信支付 */
		ALIINTERNATIONAL(6, "支付宝国际");
		
		public Integer code;
		public String cnName;
		
		OrderPayWay(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderPayWay entry : OrderPayWay.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
		
		@Override
		public boolean contains(int code) {
			for (OrderPayWay entry : values()) {
				if (entry.code.equals(code)) {
					return true;
				}
			}
			return false;
		}
		
	}

	/**
	 * 订单支付方式
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderPayType implements BaseEnum {
		
		/** 1 - 在线支付 */
		ONLINE(1, "在线支付"),
		/** 2 - 货到付款 */
		COD(2, "货到付款");
		
		public Integer code;
		public String cnName;
		
		OrderPayType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderPayType entry : OrderPayType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
		
		@Override
		public boolean contains(int code) {
			for (OrderPayType entry : values()) {
				if (entry.code.equals(code)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 订单是否开发票
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderIsReceipt {
		
		/** 1 - 开票 */
		YES(1, "开票"),
		/** 0 - 不开票 */
		NO(0, "不开票");
		
		public Integer code;
		public String cnName;
		
		OrderIsReceipt(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderIsReceipt entry : OrderIsReceipt.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 订单是否逻辑删除
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderIsDeleted {
		
		/** 1 - 删除 */
		YES(1, "删除"),
		/** 2 - 不删除 */
		NO(2, "不删除");
		
		public Integer code;
		public String cnName;
		
		OrderIsDeleted(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderIsDeleted entry : OrderIsDeleted.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}
}
