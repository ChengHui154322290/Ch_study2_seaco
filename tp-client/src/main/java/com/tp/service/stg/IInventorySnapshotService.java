package com.tp.service.stg;

import java.util.List;

import com.tp.model.stg.InventorySnapshot;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 库存信息表 记录sku的总库存、总销售占用库存信息接口
  */
public interface IInventorySnapshotService extends IBaseService<InventorySnapshot>{
	/***批量插入快照表**/
	public void  backUpInventorySnapShot();

	/**
	 * 导出库存快照信息
	 * @param sdo
	 * @return
	 * @throws ServiceException
	 */
	List<InventorySnapshot>  querySnapshotForExport(InventorySnapshot inventorySnapshot);
	
	/***
	 * 查询导出的仓库信息
	 * @param sdo
	 * @return
	 * @throws ServiceException
	 */
	List<InventorySnapshot>  queryDistinctWarehouseIdForExport(InventorySnapshot inventorySnapshot);
}
