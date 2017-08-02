package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InventoryOccupy;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInventoryOccupyService;
/**
 * 下单完成后库存占用（冻结）及取消（解冻）记录代理层
 * @author szy
 *
 */
@Service
public class InventoryOccupyProxy extends BaseProxy<InventoryOccupy>{

	@Autowired
	private IInventoryOccupyService inventoryOccupyService;

	@Override
	public IBaseService<InventoryOccupy> getService() {
		return inventoryOccupyService;
	}
}
