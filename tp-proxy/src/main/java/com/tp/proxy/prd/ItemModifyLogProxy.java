package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemModifyLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemModifyLogService;
/**
 * 商品修改日志代理层
 * @author szy
 *
 */
@Service
public class ItemModifyLogProxy extends BaseProxy<ItemModifyLog>{

	@Autowired
	private IItemModifyLogService itemModifyLogService;

	@Override
	public IBaseService<ItemModifyLog> getService() {
		return itemModifyLogService;
	}
}
