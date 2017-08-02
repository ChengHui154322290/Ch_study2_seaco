package com.tp.service.stg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dao.stg.InventoryLogDao;
import com.tp.model.stg.InventoryLog;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryLogService;

@Service
public class InventoryLogService extends BaseService<InventoryLog> implements IInventoryLogService {

	@Autowired
	private InventoryLogDao inventoryLogDao;
	
	@Override
	public BaseDao<InventoryLog> getDao() {
		return inventoryLogDao;
	}
	

	@Override
	public void insertBatch(List<InventoryLog> inventoryLogs){
		inventoryLogDao.insertBatch(inventoryLogs);
	}

	@Override
	public List<InventoryLog> queryLogForExport(InventoryLog inventoryLog){
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, inventoryLog.getCreateBeginTime()));
		whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, inventoryLog.getCreateEndTime()));
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		if (StringUtils.isNotEmpty(inventoryLog.getSku())) {
			params.put("sku", inventoryLog.getSku());	
		}
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), inventoryLog.getExportCount());
		return getDao().queryPageByParamNotEmpty(params);
	}

	@Override
	public List<InventoryLog> queryDistinctWareHouseIdForExport(InventoryLog inventoryLog){
		return inventoryLogDao.queryDistinctWareHouseIdForExport(inventoryLog);
	}
}
