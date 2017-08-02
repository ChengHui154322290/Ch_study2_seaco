package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.OrderPointDao;
import com.tp.model.ord.OrderPoint;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderPointService;

@Service
public class OrderPointService extends BaseService<OrderPoint> implements IOrderPointService {

	@Autowired
	private OrderPointDao orderPointDao;
	
	@Override
	public BaseDao<OrderPoint> getDao() {
		return orderPointDao;
	}

}
