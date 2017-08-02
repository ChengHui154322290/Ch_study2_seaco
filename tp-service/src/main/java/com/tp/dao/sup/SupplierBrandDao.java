package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierBrand;

public interface SupplierBrandDao extends BaseDao<SupplierBrand> {

	void batchInsert(List<SupplierBrand> supplierBrandList);

	void updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

}
