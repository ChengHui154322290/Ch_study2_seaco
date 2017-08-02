package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierBrandDao;
import com.tp.model.sup.SupplierBrand;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierBrandService;

@Service
public class SupplierBrandService extends BaseService<SupplierBrand> implements ISupplierBrandService {

	@Autowired
	private SupplierBrandDao supplierBrandDao;
	
	@Override
	public BaseDao<SupplierBrand> getDao() {
		return supplierBrandDao;
	}

}
