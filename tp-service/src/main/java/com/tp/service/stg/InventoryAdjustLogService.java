package com.tp.service.stg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dao.stg.InventoryAdjustLogDao;
import com.tp.model.stg.InventoryAdjustLog;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryAdjustLogService;

@Service
public class InventoryAdjustLogService extends BaseService<InventoryAdjustLog> implements IInventoryAdjustLogService {

	@Autowired
	private InventoryAdjustLogDao inventoryAdjustLogDao;
	
	@Override
	public BaseDao<InventoryAdjustLog> getDao() {
		return inventoryAdjustLogDao;
	}
	
	/***
	 * 
	 * 导出盘盈盘亏信息
	 * @param adjustLogDO
	 * @return
	 * @throws DAOException
	 */
	public List<InventoryAdjustLog> queryAdjustLogForExport(InventoryAdjustLog inventoryAdjustLog){
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		whEntries.add(new WHERE_ENTRY("create_date", MYBATIS_SPECIAL_STRING.GT, inventoryAdjustLog.getStartDate()));
		whEntries.add(new WHERE_ENTRY("create_date", MYBATIS_SPECIAL_STRING.LT, inventoryAdjustLog.getEndDate()));
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		params.put("sku", inventoryAdjustLog.getSku());
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), inventoryAdjustLog.getExportCount());
		return queryByParamNotEmpty(params);
	}
}
