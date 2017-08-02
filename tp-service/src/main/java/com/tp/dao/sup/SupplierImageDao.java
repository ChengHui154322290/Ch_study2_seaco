package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SupplierImage;

public interface SupplierImageDao extends BaseDao<SupplierImage> {

	Integer updateImageStatusBySupplierId(@Param("supplierId")Long supplierId, @Param("status")Integer status,@Param("imgType")String imageType,@Param("updateUser") String updateUser);

	void batchInsert(List<SupplierImage> supplierImageDOs);

}
