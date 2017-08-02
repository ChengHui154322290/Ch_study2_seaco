package com.tp.dao.ord;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.datasource.ContextHolder.DATA_SOURCE_KEY;
import com.tp.model.ord.OrderImportInfo;
import com.tp.model.ord.OrderImportLog;

public interface OrderImportInfoDao extends BaseDao<OrderImportInfo> {
	
}
