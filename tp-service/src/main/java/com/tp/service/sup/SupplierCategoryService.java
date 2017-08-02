package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierCategoryDao;
import com.tp.model.sup.SupplierCategory;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierCategoryService;

@Service
public class SupplierCategoryService extends BaseService<SupplierCategory> implements ISupplierCategoryService {

	@Autowired
	private SupplierCategoryDao supplierCategoryDao;
	
	@Override
	public BaseDao<SupplierCategory> getDao() {
		return supplierCategoryDao;
	}

}
