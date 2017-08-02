package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.InventorySnapshot;

public interface InventorySnapshotDao extends BaseDao<InventorySnapshot> {

	/***
	 * 查询要导出的快照信息的仓库集合
	 * @param snapshotDO
	 * @return
	 * @throws DAOException
	 */
	List<InventorySnapshot> queryDistinctWarehouseIdForExport(InventorySnapshot inventorySnapshot);
	
}
