package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierInvoice;

public interface SupplierInvoiceDao extends BaseDao<SupplierInvoice> {

	void batchInsert(List<SupplierInvoice> supplierInvoiceList);

	Integer updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

}