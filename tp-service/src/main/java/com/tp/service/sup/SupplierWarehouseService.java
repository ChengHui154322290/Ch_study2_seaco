package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierWarehouseDao;
import com.tp.model.sup.SupplierWarehouse;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierWarehouseService;

@Service
public class SupplierWarehouseService extends BaseService<SupplierWarehouse> implements ISupplierWarehouseService {

	@Autowired
	private SupplierWarehouseDao supplierWarehouseDao;
	
	@Override
	public BaseDao<SupplierWarehouse> getDao() {
		return supplierWarehouseDao;
	}

}
