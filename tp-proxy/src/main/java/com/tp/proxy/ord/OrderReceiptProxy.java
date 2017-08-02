package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderReceipt;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderReceiptService;
/**
 * 订单发票表代理层
 * @author szy
 *
 */
@Service
public class OrderReceiptProxy extends BaseProxy<OrderReceipt>{

	@Autowired
	private IOrderReceiptService orderReceiptService;

	@Override
	public IBaseService<OrderReceipt> getService() {
		return orderReceiptService;
	}
}
