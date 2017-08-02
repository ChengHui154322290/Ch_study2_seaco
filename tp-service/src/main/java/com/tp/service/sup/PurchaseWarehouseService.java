package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.PurchaseWarehouseDao;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.service.BaseService;
import com.tp.service.sup.IPurchaseWarehouseService;

import java.util.Map;

@Service
public class PurchaseWarehouseService extends BaseService<PurchaseWarehouse> implements IPurchaseWarehouseService {

	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	
	@Override
	public BaseDao<PurchaseWarehouse> getDao() {
		return purchaseWarehouseDao;
	}


	@Override
	public Integer updateAuditStatusByIds(Map<String, Object> params) {
		return purchaseWarehouseDao.updateAuditStatusByIds( params);
	}
}
