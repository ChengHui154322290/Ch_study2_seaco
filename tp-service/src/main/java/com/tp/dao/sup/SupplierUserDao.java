package com.tp.dao.sup;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierUser;

public interface SupplierUserDao extends BaseDao<SupplierUser> {

	Integer updateUserBySupplierId(SupplierUser supplierUser);

}
