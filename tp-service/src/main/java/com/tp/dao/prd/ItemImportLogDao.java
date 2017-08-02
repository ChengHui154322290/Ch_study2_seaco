package com.tp.dao.prd;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.datasource.ContextHolder.DATA_SOURCE_KEY;
import com.tp.model.prd.ItemImportLog;

public interface ItemImportLogDao extends BaseDao<ItemImportLog> {

	void updateCountById(ItemImportLog itemImportLog);

	void deleteByUserId(ItemImportLog deleteLog);

	ItemImportLog selectByLastOne(@Param("importLog")ItemImportLog importLog,@Param(DAOConstant.DATA_SOURCE_KEY)DATA_SOURCE_KEY masterSaleOrderDataSource);

}
