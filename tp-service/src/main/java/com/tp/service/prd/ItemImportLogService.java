package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemImportLogDao;
import com.tp.model.prd.ItemImportLog;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemImportLogService;

@Service
public class ItemImportLogService extends BaseService<ItemImportLog> implements IItemImportLogService {

	@Autowired
	private ItemImportLogDao itemImportLogDao;
	
	@Override
	public BaseDao<ItemImportLog> getDao() {
		return itemImportLogDao;
	}

}
