package com.tp.common.vo.ord;
 /**
 * 取消单 常量
 * @author szy
 */
public interface CancelConstant{

	public enum CANCEL_STATUS{
		
		APPLY(1,"待审核"),
		AUDITED(2,"审核通过"),
		FAIL_AUDIT(3,"审核不通过"),
		REFUNDING(4,"等待退款"),
		FAIL_REFUND(5,"退款失败"),
		REFUNDED(6,"已退款");
		
		public Integer code;
		public String cnName;
		
		CANCEL_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}
	public enum LOG_ACTION_TYPE{
		APPLY(1,"创建取消单"),
		AUDIT(2,"审核"),
		RERUND(3,"退款");
		
		public Integer code;
		public String cnName;
		
		LOG_ACTION_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}
}
