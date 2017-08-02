package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleOrderItemDao;
import com.tp.model.fin.SettleOrderItem;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleOrderItemService;

@Service
public class SettleOrderItemService extends BaseService<SettleOrderItem> implements ISettleOrderItemService {

	@Autowired
	private SettleOrderItemDao settleOrderItemDao;
	
	@Override
	public BaseDao<SettleOrderItem> getDao() {
		return settleOrderItemDao;
	}

}
