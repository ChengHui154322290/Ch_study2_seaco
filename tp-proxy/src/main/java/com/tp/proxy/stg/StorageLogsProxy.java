package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.StorageLogs;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IStorageLogsService;
/**
 * storage数据库操作日志表代理层
 * @author szy
 *
 */
@Service
public class StorageLogsProxy extends BaseProxy<StorageLogs>{

	@Autowired
	private IStorageLogsService storageLogsService;

	@Override
	public IBaseService<StorageLogs> getService() {
		return storageLogsService;
	}
}
