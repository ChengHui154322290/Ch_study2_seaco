package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InventoryManageLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInventoryManageLogService;
/**
 * 
 * @author szy
 *
 */
@Service
public class InventoryManageLogProxy extends BaseProxy<InventoryManageLog>{

	@Autowired
	private IInventoryManageLogService inventoryManageLogService;
	
	@Override
	public IBaseService<InventoryManageLog> getService() {
		return inventoryManageLogService;
	}


}
