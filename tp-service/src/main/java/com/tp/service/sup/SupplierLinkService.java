package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierLinkDao;
import com.tp.model.sup.SupplierLink;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierLinkService;

@Service
public class SupplierLinkService extends BaseService<SupplierLink> implements ISupplierLinkService {

	@Autowired
	private SupplierLinkDao supplierLinkDao;
	
	@Override
	public BaseDao<SupplierLink> getDao() {
		return supplierLinkDao;
	}

}
