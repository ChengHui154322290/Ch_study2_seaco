package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemDetailSpec;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemDetailSpecService;
/**
 * 商品销售规格信息代理层
 * @author szy
 *
 */
@Service
public class ItemDetailSpecProxy extends BaseProxy<ItemDetailSpec>{

	@Autowired
	private IItemDetailSpecService itemDetailSpecService;

	@Override
	public IBaseService<ItemDetailSpec> getService() {
		return itemDetailSpecService;
	}
}
