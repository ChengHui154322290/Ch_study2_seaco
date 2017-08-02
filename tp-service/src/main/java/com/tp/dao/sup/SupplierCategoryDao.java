package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierCategory;

public interface SupplierCategoryDao extends BaseDao<SupplierCategory> {

	void batchInsert(List<SupplierCategory> supplierCategoryList);

	Integer updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

}
