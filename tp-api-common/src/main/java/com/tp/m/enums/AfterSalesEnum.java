package com.tp.m.enums;

/**
 * 售后枚举
 * @author zhuss
 * @2016年2月26日 下午7:01:29
 */
public interface AfterSalesEnum {

	//售后 显示给用户看的售后状态
	public enum AFTERSALES_STATUS{
		ERROR("0","待客服审核"),
		WAIT_AUDIT("1","客服审核中"),
		AUDIT_FAIL("2","审核不通过"),
		AUDIT_PASS("3","审核通过"),
		CANCLE("4","取消退货"),
		RETURN("5","退货中"),
		REFUND("6","退款中"),
		REFUND_FINISH("7","退货完成"),
		REFUND_FAIL("8","退货失败");
		
		public String code;
		public String desc;
		
		AFTERSALES_STATUS(String code,String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static AFTERSALES_STATUS getShowStatus(String code){
			for (AFTERSALES_STATUS c : AFTERSALES_STATUS.values()) {
	            if (c.code .equals(code) ) {
	                return c;
	            }
	        }
			return null;
		}
	}
}
