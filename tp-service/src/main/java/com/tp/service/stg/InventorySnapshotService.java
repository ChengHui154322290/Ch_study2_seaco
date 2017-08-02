package com.tp.service.stg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dao.stg.InventorySnapshotDao;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventorySnapshot;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IInventorySnapshotService;

@Service
public class InventorySnapshotService extends BaseService<InventorySnapshot> implements IInventorySnapshotService {

	@Autowired
	private InventorySnapshotDao inventorySnapshotDao;

	@Autowired
	private IInventoryService inventoryService;
	
	@Override
	public BaseDao<InventorySnapshot> getDao() {
		return inventorySnapshotDao;
	}
	private Logger logger = LoggerFactory.getLogger(InventorySnapshotService.class);
	
	@Override
	public void backUpInventorySnapShot() {
		long allCount = inventoryService.queryByObjectCount(new Inventory());
		int searchPer = 1000;
		Integer cycle = (int) Math
				.ceil(Double.valueOf(String.valueOf(allCount)) / Double.valueOf(String.valueOf(searchPer)));
		try {
			for (int i = 0; i < cycle; i++) {
				Inventory searchObj = new Inventory();
				searchObj.setStartPage(i + 1);
				searchObj.setPageSize(searchPer);
				PageInfo<Inventory> pageInfo = inventoryService.queryPageByObject(searchObj, new PageInfo<Inventory>(i + 1, searchPer));
				List<Inventory> listCopy = pageInfo.getRows();
				if (CollectionUtils.isNotEmpty(listCopy)) {
					for (Inventory inventoryE : listCopy) {
						InventorySnapshot inventorySnapshotE = new InventorySnapshot();
						BeanUtils.copyProperties(inventorySnapshotE, inventoryE);
						inventorySnapshotE.setId(null);
						inventorySnapshotE.setSnapTime(new Date());
						inventorySnapshotDao.insert(inventorySnapshotE);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}

	@Override
	public List<InventorySnapshot> querySnapshotForExport(InventorySnapshot inventorySnapshot){
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		if (null != inventorySnapshot.getCreateBeginTime()) {
			whEntries.add(new WHERE_ENTRY("snap_time", MYBATIS_SPECIAL_STRING.GT, inventorySnapshot.getCreateBeginTime()));
		}
		if (null != inventorySnapshot.getCreateEndTime()) {
			whEntries.add(new WHERE_ENTRY("snap_time", MYBATIS_SPECIAL_STRING.LT, inventorySnapshot.getCreateEndTime()));
		}
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		String sku = inventorySnapshot.getSku();
		if (StringUtils.isNotEmpty(sku)) {
			params.put("sku", inventorySnapshot.getSku());	
		}		
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), inventorySnapshot.getExportCount());
		return getDao().queryPageByParamNotEmpty(params);
	}

	@Override
	public List<InventorySnapshot> queryDistinctWarehouseIdForExport(InventorySnapshot inventorySnapshot) {
		return inventorySnapshotDao.queryDistinctWarehouseIdForExport(inventorySnapshot);
	}
}
