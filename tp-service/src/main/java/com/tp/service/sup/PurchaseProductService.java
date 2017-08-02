package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.PurchaseProductDao;
import com.tp.model.sup.PurchaseProduct;
import com.tp.service.BaseService;
import com.tp.service.sup.IPurchaseProductService;

@Service
public class PurchaseProductService extends BaseService<PurchaseProduct> implements IPurchaseProductService {

	@Autowired
	private PurchaseProductDao purchaseProductDao;
	
	@Override
	public BaseDao<PurchaseProduct> getDao() {
		return purchaseProductDao;
	}

}
