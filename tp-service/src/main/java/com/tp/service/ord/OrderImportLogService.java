package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.OrderImportLogDao;
import com.tp.model.ord.OrderImportLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderImportLogService;

@Service
public class OrderImportLogService extends BaseService<OrderImportLog> implements IOrderImportLogService {

	@Autowired
	private OrderImportLogDao orderImportLogDao;
	
	@Override
	public BaseDao<OrderImportLog> getDao() {
		return orderImportLogDao;
	}

}
