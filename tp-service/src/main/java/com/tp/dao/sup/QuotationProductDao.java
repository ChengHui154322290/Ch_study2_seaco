package com.tp.dao.sup;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.QuotationProduct;

import java.util.Map;

public interface QuotationProductDao extends BaseDao<QuotationProduct> {

	void updateAuditStatus(@Param("quoId")Long id, @Param("auditStatus")Integer auditStatus, @Param("status")Integer status,@Param("updateUser")String updateUser);

	void updateStatus(@Param("quoId")Long id, @Param("status")Integer status, @Param("updateUser")String updateUser);

	Integer deleteByIds(Map<String,Object> param);

}
