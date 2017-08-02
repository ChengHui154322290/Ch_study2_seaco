package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemDetailImport;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemDetailImportService;
/**
 * 商品详情导入日志表代理层
 * @author szy
 *
 */
@Service
public class ItemDetailImportProxy extends BaseProxy<ItemDetailImport>{

	@Autowired
	private IItemDetailImportService itemDetailImportLogService;

	@Override
	public IBaseService<ItemDetailImport> getService() {
		return itemDetailImportLogService;
	}
}
