package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.OrderBatch;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IOrderBatchService;
/**
 * 订单合并支付表(业务类型为订单)代理层
 * @author szy
 *
 */
@Service
public class OrderBatchProxy extends BaseProxy<OrderBatch>{

	@Autowired
	private IOrderBatchService orderBatchService;

	@Override
	public IBaseService<OrderBatch> getService() {
		return orderBatchService;
	}
}
