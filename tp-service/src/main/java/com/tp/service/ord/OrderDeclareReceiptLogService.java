package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.OrderDeclareReceiptLogDao;
import com.tp.model.ord.OrderDeclareReceiptLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderDeclareReceiptLogService;

@Service
public class OrderDeclareReceiptLogService extends BaseService<OrderDeclareReceiptLog> implements IOrderDeclareReceiptLogService {

	@Autowired
	private OrderDeclareReceiptLogDao orderDeclareReceiptLogDao;
	
	@Override
	public BaseDao<OrderDeclareReceiptLog> getDao() {
		return orderDeclareReceiptLogDao;
	}

}
