package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.InventoryLog;

public interface InventoryLogDao extends BaseDao<InventoryLog> {
	/***
	 * 批量插入log数据
	 * @param listDo
	 * @return
	 * @throws DAOException
	 */
	void insertBatch(List<InventoryLog> listDo);
	
	/**
	 * 查询满足导出条件的仓库ID
	 * @return
	 * @throws DAOException
	 */
	List<InventoryLog>  queryDistinctWareHouseIdForExport(InventoryLog inventoryLog);

	Integer queryCountByList(List<InventoryLog> inventoryLogs);
}
