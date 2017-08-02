package com.tp.dao.sup;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.PurchaseWarehouse;

import java.util.Map;

public interface PurchaseWarehouseDao extends BaseDao<PurchaseWarehouse> {

    Integer updateAuditStatusByIds(Map<String, Object> param);
}
