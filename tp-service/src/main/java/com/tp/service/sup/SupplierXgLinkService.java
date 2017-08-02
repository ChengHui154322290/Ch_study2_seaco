package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierXgLinkDao;
import com.tp.model.sup.SupplierXgLink;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierXgLinkService;

@Service
public class SupplierXgLinkService extends BaseService<SupplierXgLink> implements ISupplierXgLinkService {

	@Autowired
	private SupplierXgLinkDao supplierXgLinkDao;
	
	@Override
	public BaseDao<SupplierXgLink> getDao() {
		return supplierXgLinkDao;
	}

}
