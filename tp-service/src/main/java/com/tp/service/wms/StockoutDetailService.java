package com.tp.service.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockoutDetailDao;
import com.tp.model.wms.StockoutDetail;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockoutDetailService;

@Service
public class StockoutDetailService extends BaseService<StockoutDetail> implements IStockoutDetailService {

	@Autowired
	private StockoutDetailDao stockoutDetailDao;
	
	@Override
	public BaseDao<StockoutDetail> getDao() {
		return stockoutDetailDao;
	}

}
