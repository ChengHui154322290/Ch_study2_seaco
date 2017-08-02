package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemImportListDao;
import com.tp.model.prd.ItemImportList;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemImportListService;

@Service
public class ItemImportListService extends BaseService<ItemImportList> implements IItemImportListService {

	@Autowired
	private ItemImportListDao itemImportListDao;
	
	@Override
	public BaseDao<ItemImportList> getDao() {
		return itemImportListDao;
	}

}
