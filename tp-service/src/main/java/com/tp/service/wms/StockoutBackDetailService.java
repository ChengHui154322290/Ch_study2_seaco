package com.tp.service.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockoutBackDetailDao;
import com.tp.model.wms.StockoutBackDetail;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockoutBackDetailService;

@Service
public class StockoutBackDetailService extends BaseService<StockoutBackDetail> implements IStockoutBackDetailService {

	@Autowired
	private StockoutBackDetailDao stockoutBackDetailDao;
	
	@Override
	public BaseDao<StockoutBackDetail> getDao() {
		return stockoutBackDetailDao;
	}

}
