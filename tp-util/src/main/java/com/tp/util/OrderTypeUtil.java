package com.tp.util;

/**
 * 验证订单类型
 * @author szy
 *
 */
public final class OrderTypeUtil {
	
	public static Boolean isParentOrder(Long code){
		if(String.valueOf(code).startsWith(String.valueOf(CodeCreateUtil.BILL_TYPE.ORDER.code))){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
