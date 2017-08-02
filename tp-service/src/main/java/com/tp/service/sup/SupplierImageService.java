package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierImageDao;
import com.tp.model.sup.SupplierImage;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierImageService;

@Service
public class SupplierImageService extends BaseService<SupplierImage> implements ISupplierImageService {

	@Autowired
	private SupplierImageDao supplierImageDao;
	
	@Override
	public BaseDao<SupplierImage> getDao() {
		return supplierImageDao;
	}

}
