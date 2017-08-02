package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemModifyLogDao;
import com.tp.model.prd.ItemModifyLog;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemModifyLogService;

@Service
public class ItemModifyLogService extends BaseService<ItemModifyLog> implements IItemModifyLogService {

	@Autowired
	private ItemModifyLogDao itemModifyLogDao;
	
	@Override
	public BaseDao<ItemModifyLog> getDao() {
		return itemModifyLogDao;
	}

}
