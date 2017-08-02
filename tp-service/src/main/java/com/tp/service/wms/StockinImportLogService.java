package com.tp.service.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockinImportLogDao;
import com.tp.model.wms.StockinImportLog;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockinImportLogService;

@Service
public class StockinImportLogService extends BaseService<StockinImportLog> implements IStockinImportLogService {

	@Autowired
	private StockinImportLogDao stockinImportLogDao;
	
	@Override
	public BaseDao<StockinImportLog> getDao() {
		return stockinImportLogDao;
	}

}
