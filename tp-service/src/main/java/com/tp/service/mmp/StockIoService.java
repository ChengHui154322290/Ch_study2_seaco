package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.StockIoDao;
import com.tp.model.mmp.StockIo;
import com.tp.service.BaseService;
import com.tp.service.mmp.IStockIoService;

@Service
public class StockIoService extends BaseService<StockIo> implements IStockIoService {

	@Autowired
	private StockIoDao stockIoDao;
	
	@Override
	public BaseDao<StockIo> getDao() {
		return stockIoDao;
	}

}
