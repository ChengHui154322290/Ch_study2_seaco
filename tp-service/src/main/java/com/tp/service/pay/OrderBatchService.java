package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.OrderBatchDao;
import com.tp.model.pay.OrderBatch;
import com.tp.service.BaseService;
import com.tp.service.pay.IOrderBatchService;

@Service
public class OrderBatchService extends BaseService<OrderBatch> implements IOrderBatchService {

	@Autowired
	private OrderBatchDao orderBatchDao;
	
	@Override
	public BaseDao<OrderBatch> getDao() {
		return orderBatchDao;
	}

}
