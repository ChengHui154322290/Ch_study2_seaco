package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.InventoryManageLogDao;
import com.tp.model.stg.InventoryManageLog;
import com.tp.service.BaseService;

@Service
public class InventoryManageLogService extends BaseService<InventoryManageLog> implements IInventoryManageLogService {

	@Autowired
	private InventoryManageLogDao inventoryManageLogDao;
	@Override
	public BaseDao<InventoryManageLog> getDao() {
		return inventoryManageLogDao;
	}


}
