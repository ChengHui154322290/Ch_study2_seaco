package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.InventoryOccupyDao;
import com.tp.model.stg.InventoryOccupy;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryOccupyService;

@Service
public class InventoryOccupyService extends BaseService<InventoryOccupy> implements IInventoryOccupyService {

	@Autowired
	private InventoryOccupyDao inventoryOccupyDao;
	
	@Override
	public BaseDao<InventoryOccupy> getDao() {
		return inventoryOccupyDao;
	}

}
