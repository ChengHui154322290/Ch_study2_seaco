package com.tp.dto.ord.remote;

import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.model.ord.OrderStatusLog;

/**
 * 订单状态日志DTO
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderStatusLogDTO extends OrderStatusLog {

	private static final long serialVersionUID = 7070872784217813262L;

	public String getPreStatusStr() {
		return ORDER_STATUS.getCnName(getPreStatus());
	}

	public String getCurrStatusStr() {
		return ORDER_STATUS.getCnName(getCurrStatus());
	}
}
