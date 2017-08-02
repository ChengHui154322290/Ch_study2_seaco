package com.tp.common.vo;

import org.apache.commons.lang3.StringUtils;

import com.tp.util.StringUtil;

/**
 * 支付常量
 * 
 * @author szy
 *
 */
public final class PaymentConstant {

	/**
	 * ping++支付渠道代码 alipay: 支付宝手机支付<br/>
	 * alipay_wap:支付宝手机网页支付<br/>
	 * alipay_pc_direct: 支付宝 PC 网页支付<br/>
	 * alipay_qr: 支付宝扫码支付<br/>
	 * apple_pay: Apple Pay<br/>
	 * bfb:百度钱包移动快捷支付<br/>
	 * bfb_wap: 百度钱包手机网页支付<br/>
	 * upacp:银联全渠道支付<br/>
	 * upacp_wap: 银联全渠道手机网页支付<br/>
	 * upacp_pc:银联 PC 网页支付<br/>
	 * upmp:银联手机支付<br/>
	 * upmp_wap:银联手机网页支付<br/>
	 * wx:微信支付<br/>
	 * wx_pub:微信公众账号支付<br/>
	 * wx_pub_qr: 微信公众账号扫码支付<br/>
	 * yeepay_wap:易宝手机网页支付<br/>
	 * jdpay_wap:京东手机网页支付<br/>
	 * 
	 * @author szy
	 */
	public enum PINGXX_GATEWAY_CODE {
		alipay("01", "支付宝手机支付"), 
		alipay_wap("02", "支付宝手机网页支付"), 
		alipay_pc_direct("", "支付宝 PC 网页支付"), 
		alipay_qr("", "支付宝扫码支付"),
		apple_pay("", "Apple Pay"), 
		bfb("", "百度钱包移动快捷支付"), 
		bfb_wap("", "百度钱包手机网页支付"), 
		upacp("","银联全渠道支付"), 
		upacp_wap("", "银联全渠道手机网页支付"), 
		upacp_pc("", "银联 PC 网页支付"), 
		upmp("","银联手机支付"), 
		upmp_wap("", "银联手机网页支付"), 
		wx("03", "微信支付"), 
		wx_pub("04","微信公众账号支付"), 
		wx_pub_qr("", "微信公众账号扫码支付"), 
		yeepay_wap("","易宝手机网页支付"),
		jdpay_wap("", "京东手机网页支付");
		public String code;
		public String cnName;

		PINGXX_GATEWAY_CODE(String code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static Boolean isValid(String name) {
			if (StringUtils.isBlank(name))
				return false;
			for (PINGXX_GATEWAY_CODE entry : PINGXX_GATEWAY_CODE.values()) {
				if (entry.toString().equals(name))
					return true;
			}
			return false;
		}

		public static String getCode(String name) {
			for (PINGXX_GATEWAY_CODE entry : PINGXX_GATEWAY_CODE.values()) {
				if (entry.name().equals(name)) {
					return entry.code;
				}
			}
			return null;
		}
	}

	/**
	 * ping++相关通知事件
	 * 
	 * @author szy
	 *
	 */
	public enum WEBHOOKS_TYPE {
		charge("charge.succeeded", "支付成功通知事件");

		private String code;
		private String msg;

		WEBHOOKS_TYPE(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	/**
	 * 货币枚举
	 * 
	 * @version 0.0.1
	 */
	public enum CurrencyEnum {

		GBP("gbp", "英镑"),

		HKD("hkd", "港币"),

		USD("usd", "美元"),

		CHF("chf", "瑞士法郎"),

		SGD("sgd", "新加坡元"),

		SEK("sek", "瑞典克朗"),

		DKK("dkk", "丹麦克朗"),

		NOK("nok", "挪威克朗"),

		JPY("jpy", "日元"),

		CAD("cad", "加拿大元"),

		AUD("aud", "澳大利亚元"),

		EUR("eur", "欧元"),

		NZD("nzd", "新西兰元"),

		THB("thb", "泰铢");

		/** 枚举编号 */
		private String code;
		/** 枚举详情 */
		private String description;

		private CurrencyEnum(String code, String description) {
			this.code = code;
			this.description = description;
		}

		/**
		 * 根据code取得枚举值
		 * 
		 * @param code
		 *            枚举编号
		 * @return 枚举对象
		 */
		public static CurrencyEnum getByCode(String code) {
			for (CurrencyEnum s : CurrencyEnum.values()) {
				if (StringUtils.equals(s.getCode(), code)) {
					return s;
				}
			}
			return null;
		}

		/**
		 * Getter method for property <tt>code</tt>.
		 *
		 * @return property value of code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * Setter method for property <tt>code</tt>.
		 *
		 * @param code
		 *            value to be assigned to property code
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * Getter method for property <tt>description</tt>.
		 *
		 * @return property value of description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Setter method for property <tt>description</tt>.
		 *
		 * @param description
		 *            value to be assigned to property description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
	}

	/**
	 * 支付信息 常量
	 * 
	 * @author szy
	 */
	public static final Long PAYMENT_GATEWAY_TOP_LEVEL = 0l;

	public final static String PAYMENT_INFO_STATUS_QUEUE = "payment_order_p2p_queue";

	/**
	 * 支付业务类型
	 * 
	 * @author szy
	 *
	 */
	public enum BIZ_TYPE {

		/** 订单-1 */
		ORDER("订单", 1),
		/** 子订单-3 */
		SUBORDER("子订单付款", 3),
		
		DSS("分销员付款", 4),
		MERGEORDER("合并支付", 5),
		/** 退款-2 */
		REFUND("退款", 2);
		
		public String cnName;
		public Integer code;

		BIZ_TYPE(String cnName, Integer code) {
			this.cnName = cnName;
			this.code = code;
		}

		public static String getCnName(Integer code) {
			if (code == null) {
				return null;
			}
			for (BIZ_TYPE entry : BIZ_TYPE.values()) {
				if (code.intValue() == entry.code) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 支付类型
	 * 
	 * @author szy
	 *
	 */
	public static enum PAYMENT_TYPE {
		/** 订单付款 */
		ORDER("订单付款", 1),

		/** 充值付款 */
		RECHARGE("充值付款", 2);

		public String cnName;
		public Integer code;

		PAYMENT_TYPE(String name, Integer code) {
			this.cnName = name;
			this.code = code;
		}

		public static String getCnName(Integer code) {
			if (code == null) {
				return null;
			}
			for (PAYMENT_TYPE item : PAYMENT_TYPE.values()) {
				if (item.code.intValue() == code) {
					return item.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 支付状态
	 * 
	 * @author szy
	 *
	 */
	public enum PAYMENT_STATUS {
		NO_PAY(0, "未支付"), PAYING(1, "支付中"), PAYED(2, "已支付"), FAIL_PAY(3, "支付失败");

		public Integer code;
		public String cnName;

		PAYMENT_STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			if (code == null) {
				return null;
			}
			for (PAYMENT_STATUS item : PAYMENT_STATUS.values()) {
				if (item.code.intValue() == code) {
					return item.cnName;
				}
			}
			return null;
		}
	}
	
	/**
	 * 兑换码状态
	 * 
	 * @author zgf
	 *
	 */
	public enum REDEEM_CODE_STATUS {
		NO_USE(1, "未使用"), USED(2, "已使用"), OUT_TIME(3, "已到期退款"),BACK_PAY (4, "取消已退款");

		public Integer code;
		public String cnName;

		REDEEM_CODE_STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			if (code == null) {
				return null;
			}
			for (REDEEM_CODE_STATUS item : REDEEM_CODE_STATUS.values()) {
				if (item.code.intValue() == code) {
					return item.cnName;
				}
			}
			return null;
		}
	}
	
	
	

	/**
	 * 网关状态
	 * 
	 * @author szy
	 *
	 */
	public enum PAY_GATEWAY_STATUS {
		/** 启用-1 */
		ENABLE(1),
		/** 禁用-0 */
		DISABLE(0);

		public Integer code;

		PAY_GATEWAY_STATUS(Integer code) {
			this.code = code;
		}
	}

	/**
	 * 交易种类
	 * 
	 * @author szy
	 *
	 */
	public enum TRADE_TYPE {
		/** PC端支付 */
		PC(1),
		/** WAP(mobile)端支付 */
		WAP(2),
		/** APP(mobile)端支付 */
		APP(3);

		public Integer code;

		TRADE_TYPE(Integer code) {
			this.code = code;
		}
	}

	 /**
	 * 支付业务日志 常量
	 * @author szy
	 */
	public enum PAY_ACTION_NAME{
		CREATE("创建支付信息"),
		UPDATE("更新支付信息"),
		CANCEL("订单取消信息"),
		PAY("跳转到支付平台"),
		CALLBACK("支付平台回调"),
		REFUND_CREATE("创建退款信息"),
		REFUND_UPDATE("更新退款信息"),
		REFUND_CANCEL("退款取消信息"),
		REFUND_SUBMIT("提交退款信息"),
		REFUND("退款跳转到支付平台"),
		PUT_CUSTOM("推送支付单"),
		REFUND_CALLBACK("支付平台回调");
		public String cnName;
		PAY_ACTION_NAME(String cnName){
			this.cnName = cnName;
		}
	}
	
	public enum OBJECT_TYPE{
		PAYMENT(1),
		REFUND(2);
		public Integer code;
		OBJECT_TYPE(Integer code){
			this.code = code;
		}
	}
	
	 /**
	 * 退款支付明细 常量
	 */
	public final static String REFUND_INFO_STATUS_QUEUE = "refund_order_p2p_queue";
	
	/**
	 * 支付类型
	 * @author szy
	 *
	 */
	public static enum REFUND_STATUS {
		/** 未退款 */
		NOT_REFUNDING("未退款",0),
		/** 正在退款 */
		REFUNDING("正在退款",1),
		/** 退款完成 */
		REFUNDED("退款完成",2),
		/** 退款失败 */
		FAIL_REFUND("退款失败",3),
		/** 待确认退款 */
		TO_CONFIRM("待确认退款",4);
		
		public String cnName;
		public Integer code;
		REFUND_STATUS(String name,Integer code){
			this.cnName=name;
			this.code = code;
		}
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(PAYMENT_TYPE item:PAYMENT_TYPE.values()){
				if(item.code.intValue() == code)
				{
					return item.cnName;
				}
			}
			return null;
		}
	}

	public static enum NOTIFY_STATUS{
		UNNOTIFY(0),
		NOTIFIED(1);
		
		public Integer code;
		
		NOTIFY_STATUS(Integer code){
			this.code = code;
		}
	}
	
	public static enum GATEWAY_TYPE {
		ALIPAY("alipayDirect","支付宝支付"),
		MEGER_ALIPAY("mergeAlipay","支付宝支付"),
		WEIXIN("weixin", "微信支付"),
		WEIXIN_EXTERNAL("weixinExternal","微信境外支付");
		public String code;
		public String desc;
		private GATEWAY_TYPE(String code,String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static boolean check(String code){
			if(StringUtil.isNotBlank(code)){
				for(GATEWAY_TYPE gateway : GATEWAY_TYPE.values()){
					if(gateway.code.equals(code)) return true;
				}
			}
			return false;
		}
	}
	
	public enum CUSTOM_STATUS{
    	NEW(0),
    	SUCCESS(1),
    	PROCESSING(2),
    	FAIL(3);
    	public Integer code;
        
    	CUSTOM_STATUS(Integer code){
            this.code = code;
        }
    }
	
	/**
	 * 是否可进行合并支付
	 * @author szy
	 *
	 */
	public enum MERGE_PAY_TYPE{
		TRUE(1),
		FLASE(0);
		public Integer code;
		
		MERGE_PAY_TYPE(Integer code){
			 this.code = code;
		}
	}
}
