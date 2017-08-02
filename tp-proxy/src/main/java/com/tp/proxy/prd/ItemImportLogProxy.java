package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemImportLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemImportLogService;
/**
 * 商品导入日志表代理层
 * @author szy
 *
 */
@Service
public class ItemImportLogProxy extends BaseProxy<ItemImportLog>{

	@Autowired
	private IItemImportLogService itemImportLogService;

	@Override
	public IBaseService<ItemImportLog> getService() {
		return itemImportLogService;
	}
}
