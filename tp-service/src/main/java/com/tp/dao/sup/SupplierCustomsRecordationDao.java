package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.query.sup.SupplierQuery;

public interface SupplierCustomsRecordationDao extends BaseDao<SupplierCustomsRecordation> {

	void batchInsert(List<SupplierCustomsRecordation> supplierCustomsRecordationList);

	void updateStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status, @Param("updateUser")String updateUser);

	List<SupplierCustomsRecordation> queryCustomsRecordations(List<SupplierQuery> queryParamList);

}
