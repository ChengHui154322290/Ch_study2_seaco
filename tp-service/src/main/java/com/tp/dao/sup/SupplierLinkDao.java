package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierLink;

public interface SupplierLinkDao extends BaseDao<SupplierLink> {

	void batchInsert(List<SupplierLink> supplierLinkList);

	void updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer no, @Param("updateUser")String updateUser);

}
