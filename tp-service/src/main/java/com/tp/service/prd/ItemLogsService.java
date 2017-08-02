package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemLogsDao;
import com.tp.model.prd.ItemLogs;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemLogsService;

@Service
public class ItemLogsService extends BaseService<ItemLogs> implements IItemLogsService {

	@Autowired
	private ItemLogsDao itemLogsDao;
	
	@Override
	public BaseDao<ItemLogs> getDao() {
		return itemLogsDao;
	}

}
