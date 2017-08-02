package com.tp.service.stg;

import java.util.List;

import com.tp.model.stg.InventoryAdjustLog;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 盘盈盘亏操作日志接口
  */
public interface IInventoryAdjustLogService extends IBaseService<InventoryAdjustLog>{
	/***
	 * 获取导出信息
	 * @param adjustLogDO
	 * @return
	 * @throws ServiceException
	 */
	List<InventoryAdjustLog> queryAdjustLogForExport(InventoryAdjustLog adjustLog);
}
