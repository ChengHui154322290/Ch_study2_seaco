package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierXgLink;

public interface SupplierXgLinkDao extends BaseDao<SupplierXgLink> {

	void batchInsert(List<SupplierXgLink> supplieXgLinkList);

	Integer updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

}
