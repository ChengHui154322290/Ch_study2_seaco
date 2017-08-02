package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemPushLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemPushLogService;
/**
 * 第三方推送库存和成本价接口日志表代理层
 * @author szy
 *
 */
@Service
public class ItemPushLogProxy extends BaseProxy<ItemPushLog>{

	@Autowired
	private IItemPushLogService itemPushLogService;

	@Override
	public IBaseService<ItemPushLog> getService() {
		return itemPushLogService;
	}
}
