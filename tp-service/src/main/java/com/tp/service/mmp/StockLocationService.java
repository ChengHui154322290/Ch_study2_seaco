package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.StockLocationDao;
import com.tp.model.mmp.StockLocation;
import com.tp.service.BaseService;
import com.tp.service.mmp.IStockLocationService;

@Service
public class StockLocationService extends BaseService<StockLocation> implements IStockLocationService {

	@Autowired
	private StockLocationDao stockLocationDao;
	
	@Override
	public BaseDao<StockLocation> getDao() {
		return stockLocationDao;
	}

}
