package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemDetailImportDao;
import com.tp.model.prd.ItemDetailImport;
import com.tp.service.BaseService;

@Service
public class ItemDetailImportService extends BaseService<ItemDetailImport> implements IItemDetailImportService {

	@Autowired
	private ItemDetailImportDao itemDetailImportLogDao;
	
	@Override
	public BaseDao<ItemDetailImport> getDao() {
		return itemDetailImportLogDao;
	}

}
