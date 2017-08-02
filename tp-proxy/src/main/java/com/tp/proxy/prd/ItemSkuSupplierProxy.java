package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemSkuSupplier;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuSupplierService;
/**
 * 商品供应商信息，仅用于自营商品代理层
 * @author szy
 *
 */
@Service
public class ItemSkuSupplierProxy extends BaseProxy<ItemSkuSupplier>{

	@Autowired
	private IItemSkuSupplierService itemSkuSupplierService;

	@Override
	public IBaseService<ItemSkuSupplier> getService() {
		return itemSkuSupplierService;
	}
}
