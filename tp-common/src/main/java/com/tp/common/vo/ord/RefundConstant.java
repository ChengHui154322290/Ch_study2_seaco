package com.tp.common.vo.ord;


 /**
 * 退款单 常量
 * @author szy
 */
public interface RefundConstant{
	
	/**
	 * 退款状态
	 * @author szy
	 *
	 */
	public enum REFUND_STATUS{
		APPLY(1,"等待打款"),
		AUDITING(2,"退款中"),
		AUDITED(3,"退款成功"),
		FAIL(4,"退款失败"),
		CANCEL(5,"取消退款");
		
		public Integer code;
		public String cnName;
		
		REFUND_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code){
			for(REFUND_STATUS entry:REFUND_STATUS.values()){
				if(entry.code.intValue()==code){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}

	/**
	 * 退款类型
	 * @author szy
	 *
	 */
	public enum REFUND_TYPE{
		CANCEL(1,"取消订单"),
		REJECT(2,"退货"),
		REPEAT_PAYMENT(3,"重复支付");
		
		public Integer code;
		public String cnName;
		
		REFUND_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code){
			for(REFUND_TYPE entry:REFUND_TYPE.values()){
				if(entry.code.intValue()==code){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	public enum BIZ_TYPE{
		ORDER(1,"订单"),
		SUBORDER(2,"子订单");
		
		public Integer code;
		public String cnName;
		
		BIZ_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code){
			for(BIZ_TYPE entry:BIZ_TYPE.values()){
				if(entry.code.intValue()==code){
					return entry.cnName;
				}
			}
			return null;
		}
	}
	/**
	 * 退款记录日志类型
	 * @author szy
	 *
	 */
	public enum REFUND_LOG_TYPE{
		APPLY(1),
		AUDIT(2),
		REFUND(3);
		
		public Integer code;
		
		REFUND_LOG_TYPE(Integer code){
			this.code = code;
		}
	}
}
