package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemImportList;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemImportListService;
/**
 * 商品导入明细表代理层
 * @author szy
 *
 */
@Service
public class ItemImportListProxy extends BaseProxy<ItemImportList>{

	@Autowired
	private IItemImportListService itemImportListService;

	@Override
	public IBaseService<ItemImportList> getService() {
		return itemImportListService;
	}
}
