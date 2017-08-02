package com.tp.dao.sup;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierShop;

import java.util.List;

public interface SupplierShopDao extends BaseDao<SupplierShop> {

    List<SupplierShop> queryBySupplierIds(List<Long> list);

}
