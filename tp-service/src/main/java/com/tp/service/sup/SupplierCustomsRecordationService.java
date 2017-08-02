package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierCustomsRecordationDao;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierCustomsRecordationService;

@Service
public class SupplierCustomsRecordationService extends BaseService<SupplierCustomsRecordation> implements ISupplierCustomsRecordationService {

	@Autowired
	private SupplierCustomsRecordationDao supplierCustomsRecordationDao;
	
	@Override
	public BaseDao<SupplierCustomsRecordation> getDao() {
		return supplierCustomsRecordationDao;
	}

}
