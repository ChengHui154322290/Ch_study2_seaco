package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierBankAccount;

public interface SupplierBankAccountDao extends BaseDao<SupplierBankAccount> {

	void batchInsert(List<SupplierBankAccount> supplierBankaccountList);

	void updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

}
