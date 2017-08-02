package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.StockStatusDao;
import com.tp.model.mmp.StockStatus;
import com.tp.service.BaseService;
import com.tp.service.mmp.IStockStatusService;

@Service
public class StockStatusService extends BaseService<StockStatus> implements IStockStatusService {

	@Autowired
	private StockStatusDao stockStatusDao;
	
	@Override
	public BaseDao<StockStatus> getDao() {
		return stockStatusDao;
	}

}
