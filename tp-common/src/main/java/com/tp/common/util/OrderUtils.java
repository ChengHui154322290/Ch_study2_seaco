package com.tp.common.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.util.DateUtil;

/**
 * 订单工具类
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderUtils {

	/**
	 * 是否是父订单号
	 * 
	 * @param code
	 * @return
	 */
	public static boolean isOrderCode(Long code) {
		if (code!=null) {
			return String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString());
		}
		return false;
	}

	/**
	 * 是否是子订单号
	 * 
	 * @param code
	 * @return
	 */
	public static boolean isSubOrderCode(Long code) {
		if (code!=null) {
			return String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString());
		}
		return false;
	}
	
	/**
	 * 父订单是否支付
	 * 
	 * @param order
	 * @return
	 */
	public static boolean isPayed(String payCode) {
		return StringUtils.isNotBlank(payCode);
	}


	/**
	 * 是否是海淘订单
	 * 
	 * @param sub
	 * @return
	 */
	public static boolean isSeaOrder(Integer type) {
		return OrderType.DOMESTIC.code.equals(type) || OrderType.BONDEDAREA.code.equals(type) || OrderType.OVERSEASMAIL.code.equals(type) || OrderType.COMMON_SEA.code.equals(type);
	}
	
	public static Long getFastOrderOverTime(Date orderPayedTime,Integer seconds){
		Date overTime = DateUtil.addSeconds(orderPayedTime, seconds);
		Date currentTime = new Date();
		return (overTime.getTime()-currentTime.getTime())/1000;
	}
}
