package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemLogs;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemLogsService;
/**
 * 商品操作日志代理层
 * @author szy
 *
 */
@Service
public class ItemLogsProxy extends BaseProxy<ItemLogs>{

	@Autowired
	private IItemLogsService itemLogsService;

	@Override
	public IBaseService<ItemLogs> getService() {
		return itemLogsService;
	}
}
