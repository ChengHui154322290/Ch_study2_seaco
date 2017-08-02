package com.tp.common.vo.pay;

import com.tp.common.vo.PaymentConstant.PAYMENT_TYPE;

 /**
 * 退款支付明细 常量
 * @author szy
 */
public interface PaymentRefundInfoConstant{
	
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
}
