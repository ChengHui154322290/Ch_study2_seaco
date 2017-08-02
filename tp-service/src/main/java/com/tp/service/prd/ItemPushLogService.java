package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemPushLogDao;
import com.tp.model.prd.ItemPushLog;
import com.tp.service.BaseService;

@Service
public class ItemPushLogService extends BaseService<ItemPushLog> implements IItemPushLogService {

	@Autowired
	private ItemPushLogDao itemPushLogDao;
	
	@Override
	public BaseDao<ItemPushLog> getDao() {
		return itemPushLogDao;
	}

}
