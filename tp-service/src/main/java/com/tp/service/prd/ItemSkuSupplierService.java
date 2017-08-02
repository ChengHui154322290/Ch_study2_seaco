package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemSkuSupplierDao;
import com.tp.model.prd.ItemSkuSupplier;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuSupplierService;

@Service
public class ItemSkuSupplierService extends BaseService<ItemSkuSupplier> implements IItemSkuSupplierService {

	@Autowired
	private ItemSkuSupplierDao itemSkuSupplierDao;
	
	@Override
	public BaseDao<ItemSkuSupplier> getDao() {
		return itemSkuSupplierDao;
	}

}
