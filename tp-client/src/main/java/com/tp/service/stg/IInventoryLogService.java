package com.tp.service.stg;

import java.util.List;

import com.tp.model.stg.InventoryLog;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 出入库流水信息表接口
  */
public interface IInventoryLogService extends IBaseService<InventoryLog>{
	
	void insertBatch(List<InventoryLog> listDo) throws Exception;	
	/***
	 * 导出出入库流水信息
	 * @param ido
	 * @return
	 * @throws ServiceException
	 */
	 List<InventoryLog> queryLogForExport(InventoryLog inventoryLog);
	 
	 /***
	  * 查询满足导出条件的不同仓库ID
	  * @return
	  * @throws ServiceException
	  */
	 List<InventoryLog> queryDistinctWareHouseIdForExport (InventoryLog inventoryLog);
}
