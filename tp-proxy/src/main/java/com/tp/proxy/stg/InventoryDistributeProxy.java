package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InventoryDistribute;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInventoryDistributeService;
/**
 * 库存分配记录表 可分配库存数量=真实库存数量-各业务系统分配的库存数量-占用库存数量，可销售库存=分配库存-已售出数量代理层
 * @author szy
 *
 */
@Service
public class InventoryDistributeProxy extends BaseProxy<InventoryDistribute>{

	@Autowired
	private IInventoryDistributeService inventoryDistributeService;

	@Override
	public IBaseService<InventoryDistribute> getService() {
		return inventoryDistributeService;
	}
}
