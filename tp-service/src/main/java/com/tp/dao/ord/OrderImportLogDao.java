package com.tp.dao.ord;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.datasource.ContextHolder.DATA_SOURCE_KEY;
import com.tp.model.ord.OrderImportLog;

public interface OrderImportLogDao extends BaseDao<OrderImportLog> {

	void updateCountById(OrderImportLog itemImportLog);

	void deleteByUserId(OrderImportLog deleteLog);

	OrderImportLog selectByLastOne(@Param("importLog")OrderImportLog importLog,@Param(DAOConstant.DATA_SOURCE_KEY)DATA_SOURCE_KEY masterSaleOrderDataSource);


}
