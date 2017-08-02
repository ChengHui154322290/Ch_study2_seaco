package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.StorageLogsDao;
import com.tp.model.stg.StorageLogs;
import com.tp.service.BaseService;
import com.tp.service.stg.IStorageLogsService;

@Service
public class StorageLogsService extends BaseService<StorageLogs> implements IStorageLogsService {

	@Autowired
	private StorageLogsDao storageLogsDao;
	
	@Override
	public BaseDao<StorageLogs> getDao() {
		return storageLogsDao;
	}

}
