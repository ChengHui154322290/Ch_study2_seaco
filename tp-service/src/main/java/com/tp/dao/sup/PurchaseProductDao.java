package com.tp.dao.sup;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.PurchaseProduct;

public interface PurchaseProductDao extends BaseDao<PurchaseProduct> {

	Integer updateAuditStatus(@Param("purchaseId")Long purchaseId, @Param("auditStatus")Integer auditStatus, @Param("updateUser")String updateUser);

	Integer updateStatus(@Param("purchaseId")Long purchaseId, @Param("status")Integer status, @Param("updateUser")String updateUser);
}
