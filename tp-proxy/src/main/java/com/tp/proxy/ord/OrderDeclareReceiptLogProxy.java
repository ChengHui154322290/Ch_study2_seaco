package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderDeclareReceiptLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderDeclareReceiptLogService;
/**
 * 订单申报回执流水日志代理层
 * @author szy
 *
 */
@Service
public class OrderDeclareReceiptLogProxy extends BaseProxy<OrderDeclareReceiptLog>{

	@Autowired
	private IOrderDeclareReceiptLogService orderDeclareReceiptLogService;

	@Override
	public IBaseService<OrderDeclareReceiptLog> getService() {
		return orderDeclareReceiptLogService;
	}
}
