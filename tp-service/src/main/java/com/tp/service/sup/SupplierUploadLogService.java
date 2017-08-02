package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierUploadLogDao;
import com.tp.model.sup.SupplierUploadLog;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierUploadLogService;

@Service
public class SupplierUploadLogService extends BaseService<SupplierUploadLog> implements ISupplierUploadLogService {

	@Autowired
	private SupplierUploadLogDao supplierUploadLogDao;
	
	@Override
	public BaseDao<SupplierUploadLog> getDao() {
		return supplierUploadLogDao;
	}

}
